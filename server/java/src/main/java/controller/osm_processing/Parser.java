package controller.osm_processing;

import model.Edge;
import model.RoadMap;
import model.Node;

import java.util.*;

/**
 *      |==================================|
 *      |==========| OSM PARSER  |=========|
 *      |==================================|
 *
 *
 *  static class that parse 'OsmWay' objects to 'model.Edge' & 'model.Node'
 *
 * @author  Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 1.0
 * @since   2021-06-20
 */
public final class Parser {
    private static final HashMap<Edge, OsmWay> WAYS = new HashMap<>();
    private static final Set<Edge> EDGES = new HashSet<>();

    private Parser() {}

    public static void parseMapWays(ArrayList<OsmWay> ways){

        for (OsmWay way: ways) {
            // Create first edge between the first and the last objects:
            HashMap<Long, OsmObject> objectsOnWay = (HashMap<Long, OsmObject>) way.getObjects();

            if (!objectsOnWay.isEmpty() && objectsOnWay.size() >= 2) {  //TODO check if node have irrelevant tags

                Node start = createNodeForEdge(way.getFirst(), way);
                Node target = createNodeForEdge(way.getLast(), way);

                Edge edge = addEdge(start, target, way);
                WAYS.put(edge, way);

                // iterate through other objects on way (first and last one polled):
                for (OsmObject object: way.getObjectsList()) {

                    // check whether the object was referenced by other ways:
                    if (object.getLinkCounter() > 1) {
                        // if so, split way into two edges at this point:
                        splitEdgeAt(object, edge, way);
                    }

                }
            }
        }

        // add edges to nodes and calculate final distance:
        for (Edge e: EDGES) {
            Node start = e.getStartNode(), target = e.getEndNode();
            if(!(target.isAdjacent(start) || start.isAdjacent(target)) ){
                start.addEdge(e);
                target.addEdge(e);
                addEdge(start, target, WAYS.get(e));
            }

            e.getLength();

            // TODO make edge bi-directional if needed

        }

        RoadMap.getInstance().setEdges(EDGES);
    }

    private static Node createNodeForEdge(OsmObject osmObject, OsmWay way){
        Node node = selectNode(osmObject);
        node.addTags(way.getTags());

        return node;
    }

    private static Node selectNode(OsmObject object) {
        RoadMap map = RoadMap.getInstance();
        Long junctionID = Reader.getJunctions().get(object.getID());
        Node node = map.getNode(object.getID());

        if(junctionID == null){
            if(node == null){
                node = map.addNode(new Node(object), object.getID());
            }
        }else{
            // node is part of a junction
            node = map.getNode(junctionID);
            if(node == null){
                node = map.getJunctionNodes().get(junctionID);

                if(node == null){
                    node = new Node(object);
                    map.getJunctionNodes().put(junctionID, node);
                    map.addNode(node, object.getID());
                }
            }
        }
        return node;
    }

    /**
     * @param obj object at which edge will be split:
     * @param edge in input: start--target connection
     * @param baseWay base for new edge
     * Method creates start-X-target such connection
     * Where X is a new node created from @param obj
     *
     * returns the right part of above connection: X-target
     */
    private static void splitEdgeAt(OsmObject obj, Edge edge, OsmWay baseWay) {
        // remove old edge between two nodes:
        if(obj.getID() == edge.getStartNode().getOsmID() || obj.getID() == edge.getEndNode().getOsmID()) {
            return;
        }

        // get node at the middle:
        Node node = selectNode(obj);

        Edge leftEdge = addEdge(edge.getEndNode(), node, baseWay);
        Edge rightEdge = addEdge(node, edge.getStartNode(), baseWay);

        if(leftEdge != null) { EDGES.add(leftEdge); }
        if(rightEdge != null){ EDGES.add(rightEdge);}//TODO check if need to switch nodes

        EDGES.remove(edge);
    }

    private static Edge addEdge(Node src, Node dst, OsmWay way){
        boolean dstToSrc = dst.isAdjacent(src);

        Edge edge = new Edge(way, src, dst);

        if(dstToSrc){
            edge = dst.getEdgeTo(src);
        }
        src.addEdge(edge);
        EDGES.add(edge);

        return edge;
    }

//    private static void setNodeEdge(OEdge e){
//        if(!e.getStartNode().isAdjacent(e.getEndNode())) {
//            // TODO make edge bi-directional if needed
//            e.getStartNode().addEdge(e);
//            e.getEndNode().addEdge(e);
//        }
//
//        e.getLength();
//    }
}
