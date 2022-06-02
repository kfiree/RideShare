package controller.osmProcessing;

import controller.GraphUtils;
import controller.RDS.jsonHandler;
import model.OEdge;
import model.OGraph;
import model.ONode;

import java.util.*;

public class Parser {
    private static OGraph graph;
    private static List<Long> longs = jsonHandler.readList("removed.json");
//    private static Point2D.Double topRight = new Point2D.Double(32.10070229573369, 34.84550004660088),
//              bottomLeft = new Point2D.Double(32.10070229573369, 34.84550004660088);

//    public static void setBounds(Point2D.Double upRight, Point2D.Double downLeft){
//        topRight = upRight;
//        bottomLeft = downLeft;
//
//    }
    public static void parseMapWays(ArrayList<OMapWay> ways, Map<Long, MapObject> objects) {
        graph = OGraph.getInstance();
        Set<OEdge> edges = graph.getEdges();
        for (OMapWay way: ways) {
            // Create first edge between the first and the last objects:
            HashMap<Long, MapObject> objectsOnWay = (HashMap<Long, MapObject>) way.getObjects();

            if (objectsOnWay.isEmpty() == false && objectsOnWay.size() >= 2) {//TODO check if node have irrelevant tags

                if(!isPartOfMainComponent(way)){
                    continue;
                }

                ONode start = createNodeForEdge(way.getFirst(), way);
                ONode target = createNodeForEdge(way.getLast(), way);
                OEdge edge = new OEdge(way, start, target);

                if(longs.contains(start.getOsm_Id()) || longs.contains(target.getOsm_Id())){
                    GraphUtils.addEdgeToMap(start, target, edge);
                }

                edges.add(edge);

                // iterate through other objects on way (first and last one polled):
                for (MapObject object: way.getObjectsList()) {

                    // check whether the object was referenced by other ways:
                    Integer nodeReferenceNum = OGraph.getInstance().getNodeQuantity(object.getID());

                    if (object.linkCounter > 1) {
                        // if so, split way into two edges at this point:
                        edge = splitEdgeAt(object, edge, way);
                    }

                }
            }
        }

        // add edges to nodes and calculate final distance:
        for (OEdge e: edges) {
            setNodeEdge(e);
//            if(!e.getStartNode().isAdjacent(e.getEndNode())) {
//                // TODO make edge bi-directional if needed
//                e.getStartNode().addEdge(e);
//                e.getEndNode().addEdge(e);
//            }
//
//            // calculate distance:
//            e.calculateDistance();// 560149995 560149987 560149985
        }

    }

    private static boolean isPartOfMainComponent(OMapWay way){
        return !(longs.contains(way.getFirst().getID()) || longs.contains(way.getLast().getID()));
    }

    private static ONode createNodeForEdge(MapObject mapObject, OMapWay way){
        ONode node = selectNode(mapObject);

        node.addTags(way.getTags());
        node.addWayID(way.getID());

        return node;
    }

    private static ONode selectNode(MapObject object) {
        Long junctionID = Reader.getJunctions().get(object.getID());
        ONode node = graph.getNode(object.getID());

        if(junctionID == null){
            if(node == null){
                node = graph.addNode(new ONode(object), object.getID());
            }
        }else{
            // node is part of a junction
            node = graph.getNode(junctionID);
            if(node == null){
                node = graph.getJunctionNodes().get(junctionID);

                if(node == null){
                    node = new ONode(object);
                    graph.getJunctionNodes().put(junctionID, node);
                    graph.addNode(node, object.getID());
                }
            }
        }
        return node;
    }

    /**
     * @param obj object at which edge will be splitted:
     * @param edge in input: start--target connection
     * @param baseWay base for new edge
     * Method creates start-X-target such connection
     * Where X is a new node created from @param obj
     * @return the right part of above connection: X-target
     */
    private static OEdge splitEdgeAt(MapObject obj, OEdge edge, OMapWay baseWay) {
        // remove old edge between two nodes:
        if(obj.getID() == edge.getStartNode().getOsm_Id() || obj.getID() == edge.getEndNode().getOsm_Id())
            return edge;

        graph.removeEdge(edge);

        // get node at the middle:
        ONode node = selectNode(obj);

        if(longs.contains(edge.getStartNode().getOsm_Id()) || longs.contains(edge.getEndNode().getOsm_Id()) || longs.contains(node.getOsm_Id())){
            GraphUtils.addEdgeToMap(edge.getStartNode(), node, edge);
            GraphUtils.addEdgeToMap(node, edge.getStartNode(), edge);
        }

        // connect it to start and target of edge:
        OEdge leftEdge = new OEdge(baseWay, edge.getStartNode(), node);
        OEdge rightEdge = new OEdge(baseWay, node, edge.getEndNode());

        graph.addEdge(leftEdge);
        graph.addEdge(rightEdge);

        setNodeEdge(leftEdge);
        setNodeEdge(rightEdge);

        return rightEdge;
    }

    private static void setNodeEdge(OEdge e){
        if(!e.getStartNode().isAdjacent(e.getEndNode())) {
            // TODO make edge bi-directional if needed
            e.getStartNode().addEdge(e);
            e.getEndNode().addEdge(e);
        }

        e.calculateDistance();
    }
}
