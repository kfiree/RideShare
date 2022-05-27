package osmProcessing;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Parser {
    private static OGraph graph;
    private static Point2D.Double topRight = new Point2D.Double(32.10070229573369, 34.84550004660088), bottomLeft = new Point2D.Double(32.10070229573369, 34.84550004660088);

    public static void setBounds(Point2D.Double upRight, Point2D.Double downLeft){
        topRight = upRight;
        bottomLeft = downLeft;

    }
    public static void parseMapWays(ArrayList<OMapWay> ways, Map<Long, MapObject> objects) {
        graph = OGraph.getInstance();
        Map<String, OEdge> edges = graph.getEdges();
        for (OMapWay way: ways) {

            // Create first edge between the first and the last objects:
            HashMap<Long, MapObject> objectsOnWay = (HashMap<Long, MapObject>) way.getObjects();

            if (objectsOnWay.isEmpty() == false && objectsOnWay.size() >= 2) {//TODO check if node have irrelevant tags

                if(!(inBound(way.getFirst()) && inBound(way.getLast()))) {
                    System.out.println("parser node out of bound");
                    break;
                }
                ONode start = createNodeForEdge(way.getFirst(), way);
                ONode target = createNodeForEdge(way.getLast(), way);

                OEdge edge = new OEdge(way, start, target);
                edges.put(UUID.randomUUID().toString(), edge);

                // iterate through other objects on way (first and last one polled):
                for (MapObject object: way.getObjectsList()) {
                    // check whether the object was referenced by other ways:
                    Integer nodeReferenceNum = OGraph.getInstance().nodesQuantity.get(object.getID());
                    if (object.linkCounter > 1) {
                        // if so, split way into two edges at this point:
                        edge = splitEdgeAt(object, edge, way);
                    }

                }
            }
        }

        // add edges to nodes and calculate final distance:
        for (OEdge e: edges.values()) {
            if(!e.getStartNode().isAdjacent(e.getEndNode())) {
                // TODO make edge bi-directional if needed
                e.getStartNode().addEdge(e);
                e.getEndNode().addEdge(e);
            }

            // calculate distance:
            e.calculateDistance();
        }

    }

    private static boolean inBound(MapObject mapObject){
//        if((mapObject.getLatitude() == 32.0755374 && mapObject.getLongitude() == 34.783446600000005) ||
//                (mapObject.getLongitude() == 32.0755374 && mapObject.getLatitude() == 34.783446600000005)){
//            int a = 1;
//        }
////        return true;
//        return mapObject.getLongitude() <= topRight.y && mapObject.getLongitude() >= bottomLeft.y &&
//            mapObject.getLatitude() <= topRight.x && mapObject.getLatitude() >= bottomLeft.x;

        return true;
    }
    private static ONode createNodeForEdge(MapObject mapObject, OMapWay way){
        ONode node = selectNode(mapObject);

        node.addTags(way.getTags());
        node.addWayID(way.getID());

        return node;
    }

    private static ONode selectNode(MapObject object) {
//        Map<Long, ONode> nodes = graph.getNodes();
        Long junctionID = Reader.getJunctions().get(object.getID());
        ONode node = graph.getNode(object.getID());

        if(junctionID == null){
            if(node == null){
                node = graph.addNode(new ONode(object), object.getID());
//                node = new ONode(object);
//                nodes.put(object.getID(), node);
            }
        }else{
            // node is part of a junction
            node = graph.getNode(junctionID);
//            node = nodes.get(junctionID);
            if(node == null){
                node = graph.getJunctionNodes().get(junctionID);

                if(node == null){
                    node = new ONode(object);
                    graph.getJunctionNodes().put(junctionID, node);
                    graph.addNode(node, object.getID());
//                    nodes.put(object.getID(), node);
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
//        Map<Long, OEdge> edges = graph.getEdges();

        // remove old edge between two nodes:
        graph.removeEdge(calculateEdgeId(edge));

        // get node at the middle:
        ONode node = selectNode(obj);

        // connect it to start and target of edge:
        OEdge leftEdge = new OEdge(baseWay, edge.getStartNode(), node);
        OEdge rightEdge = new OEdge(baseWay, node, edge.getEndNode());

        graph.addEdge(UUID.randomUUID().toString(), leftEdge);
        graph.addEdge(UUID.randomUUID().toString(), rightEdge);

        return rightEdge;
    }

    /**
     * custom created edges have no id
     * TODO find more memory-efficient way to id edges
     * TODO e.g. by using hash values or something
     * @param edge
     * @return
     */
    private static Long calculateEdgeId(OEdge edge) {
        return (long)(edge.getStartNode().getOsm_Id()+edge.getEndNode().getOsm_Id());
    }

}
