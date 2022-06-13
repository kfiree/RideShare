package controller.osmProcessing;

import model.OEdge;
import model.OGraph;
import model.ONode;

import java.util.*;

public class Parser {
    private static OGraph graph = OGraph.getInstance();
//    private static List<Long> longs = jsonHandler.readList("removed.json");
//    private static Point2D.Double topRight = new Point2D.Double(32.10070229573369, 34.84550004660088),
//              bottomLeft = new Point2D.Double(32.10070229573369, 34.84550004660088);

//    public static void setBounds(Point2D.Double upRight, Point2D.Double downLeft){
//        topRight = upRight;
//        bottomLeft = downLeft;
//
//    }
    private static HashMap<OEdge, OMapWay> _waysMap = new HashMap<>();
    private static Set<OEdge> _edges = new HashSet<>();
//    private static Set<ONode> _nodes = new HashSet<>();

    private static void clean(){
         _waysMap = new HashMap<>();
         _edges = new HashSet<>();
//         _nodes = new HashSet<>();
    }
    public static void parseMapWays(ArrayList<OMapWay> ways, Map<Long, MapObject> objects) {
//        Set<OEdge> edges = graph.getEdges();

        for (OMapWay way: ways) {
            // Create first edge between the first and the last objects:
            HashMap<Long, MapObject> objectsOnWay = (HashMap<Long, MapObject>) way.getObjects();

            if (objectsOnWay.isEmpty() == false && objectsOnWay.size() >= 2) {//TODO check if node have irrelevant tags

//                if(!isPartOfMainComponent(way)){
//                    continue;
//                }

                ONode start = createNodeForEdge(way.getFirst(), way);
                ONode target = createNodeForEdge(way.getLast(), way);

//                if((start.getOsm_Id() == 560149987l && target.getOsm_Id() ==  560149995l) || (start.getOsm_Id() == 560149995l && target.getOsm_Id() == 560149987l)){
//                    System.out.println("stop");
//                }


//                OEdge edge = new OEdge(way, start, target);

                OEdge edge = addEdge(start, target, way);
                _waysMap.put(edge, way);

                // iterate through other objects on way (first and last one polled):
                for (MapObject object: way.getObjectsList()) {

                    // check whether the object was referenced by other ways:
                    if (object.linkCounter > 1) {
                        // if so, split way into two edges at this point:
                        splitEdgeAt(object, edge, way);
                    }

                }
            }
        }

        // add edges to nodes and calculate final distance:
        for (OEdge e: _edges) {
            ONode start = e.getStartNode(), target = e.getEndNode();

//            if((start.getOsm_Id() == 560149987l && target.getOsm_Id() ==  560149995l) || (start.getOsm_Id() == 560149995l && target.getOsm_Id() == 560149987l)){
//                System.out.println("stop");
//            }

            if(!(target.isAdjacent(start) || start.isAdjacent(target)) ){
                start.addEdge(e);
                target.addEdge(e);
                addEdge(start, target, _waysMap.get(e));
            }

            e.getLength();


//            setNodeEdge(e);

//            if(!e.getStartNode().isAdjacent(e.getEndNode())) {
//                // TODO make edge bi-directional if needed
//                e.getStartNode().addEdge(e);
//                e.getEndNode().addEdge(e);
//            }
//
//            // calculate distance:
//            e.getLength();// 560149995 560149987 560149985
        }

        graph.setEdges(_edges);
//        clean();
    }

//    private static boolean isPartOfMainComponent(OMapWay way){
//        return true;
////        return !(longs.contains(way.getFirst().getID()) || longs.contains(way.getLast().getID()));
//    }

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
    private static void splitEdgeAt(MapObject obj, OEdge edge, OMapWay baseWay) {
        // remove old edge between two nodes:
        if(obj.getID() == edge.getStartNode().getOsm_Id() || obj.getID() == edge.getEndNode().getOsm_Id())
            return;

        // get node at the middle:
        ONode node = selectNode(obj);

//        if(longs.contains(edge.getStartNode().getOsm_Id()) || longs.contains(edge.getEndNode().getOsm_Id()) || longs.contains(node.getOsm_Id())){
        OEdge leftEdge = addEdge(edge.getEndNode(), node, baseWay);
        OEdge rightEdge = addEdge(node, edge.getStartNode(), baseWay);

        if(leftEdge != null) _edges.add(leftEdge);
        if(rightEdge != null) _edges.add(rightEdge);//TODO check if need to switch nodes

        _edges.remove(edge);
//        GraphUtils.addEdgeToMap(edge.getEndNode(), node, edge);
//        GraphUtils.addEdgeToMap(node, edge.getStartNode(), edge);
//        }

        // connect it to start and target of edge:
//        OEdge leftEdge = new OEdge(baseWay, edge.getStartNode(), node);
//        OEdge rightEdge = new OEdge(baseWay, node, edge.getEndNode());

//        graph.addEdge(baseWay, edge.getStartNode(), node);
//        graph.addEdge(baseWay, node, edge.getEndNode());

//        return rightEdge;
    }

    private static OEdge addEdge(ONode src, ONode dst,  OMapWay way){
//        public OEdge addEdge(ONode src, ONode dst, OMapWay way){
        boolean srcToDst = src.isAdjacent(dst);
        boolean dstToSrc = dst.isAdjacent(src);

        OEdge edge = new OEdge(way, src, dst);
//        if((src.getOsm_Id() == 560149987l && dst.getOsm_Id() ==  560149995l) || (src.getOsm_Id() == 560149995l && dst.getOsm_Id() == 560149987l)){
//            System.out.println("");
//        }
//        if(srcToDst && dstToSrc){
//            return src.getEdgeTo(dst);
//        }else
        if(dstToSrc){
            edge = dst.getEdgeTo(src);
        }

//        if(edge != null){
        src.addEdge(edge);
        _edges.add(edge);
//        }

        return edge;

    }

    private static void setNodeEdge(OEdge e){
        if(!e.getStartNode().isAdjacent(e.getEndNode())) {
            // TODO make edge bi-directional if needed
            e.getStartNode().addEdge(e);
            e.getEndNode().addEdge(e);
        }

        e.getLength();
    }
}
