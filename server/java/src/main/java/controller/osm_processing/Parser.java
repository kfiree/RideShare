package controller.osm_processing;

import controller.utils.GraphAlgo;
import controller.utils.MapUtils;
import model.RoadMap;
import model.Node;

import java.util.*;
import java.util.stream.Collectors;

import static controller.utils.LogHandler.LOGGER;

/**
 *      |==================================|
 *      |==========| OSM PARSER  |=========|
 *      |==================================|
 *
 *  parse 'OsmWay' and 'OsmObject' to 'RoadMap.java'
 *
 * @author  Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 1.0
 * @since   2021-06-20
 */
public final class Parser {
    private final Map<OsmObject, Map<OsmObject, OsmWay>> AdjacentMap = new HashMap<>();

    public void parseMapWays(ArrayList<OsmWay> ways){
        initAdjacentMap(ways);

        buildRoadMap();
    }

    private void buildRoadMap(){
        RoadMap map = RoadMap.getInstance();

        AdjacentMap.forEach((srcObj, adjacentMap)-> adjacentMap.forEach((dstObj, way)->{
            Node src = map.addNode(srcObj);
            Node dst = map.addNode(dstObj);
            map.addEdge(src, dst, way);
        }));

    }

    private void initAdjacentMap(ArrayList<OsmWay> ways){
        for (OsmWay way: ways) {

            List<OsmObject> objectsOnWay = way.getObjectsOnWay();
            OsmObject srcObj , dstObj;

            if (objectsOnWay.size() >= 2) {

                boolean directed = isDirected(way);

                srcObj = objectsOnWay.get(0);
                for (int i = 1; i < objectsOnWay.size()-1; i++) {

                    dstObj = objectsOnWay.get(i);

                    if(dstObj.isPartOfAnotherWay()){
                        saveWay(srcObj, dstObj, way, directed);
                        srcObj = dstObj;
                    }
                }
                saveWay(srcObj,  objectsOnWay.get(objectsOnWay.size() -1), way, directed); //todo check its last
//                OsmObject src = way.getFirst();
//                OsmObject dst = way.getLast();

//                saveWay(src, dst, way);

//                Node src = getOrCreateNode(way.getFirst());
//                Node dst = getOrCreateNode(way.getLast());

//                removeEdge
//                Edge edge = map.addEdge(src, dst, way);
//                WAYS.put(edge, way);

                // iterate through other objects on way (first and last one polled):
//                for (OsmObject objOnWay: way.getObjectsList()) {
//
//                    // check whether the object was referenced by other ways:
//                    if (objOnWay.isPartOfAnotherWay()) {
//                        // if so, split way into two edges at this point:
//                        splitEdgeAt(objOnWay, src, dst, way);
//                    }
//
//                }
            }else{
//                System.out.println("way with no objects on it");
                //todo make way a node ?
            }
        }
    }

    private boolean isDirected(OsmWay way){
        String oneWay = way.getTags().get("oneWay");
        return oneWay != null && oneWay.equals("yes");
    }

    private void saveWay(OsmObject first, OsmObject last, OsmWay way, boolean directed){
        MapUtils.validate(first != null && last != null, "can't create edge because edge's node is null. node1 - "+ first + ", node2 - "+ last+".");
        saveDirectedWay(first, last, way);
        if(!directed){
            saveDirectedWay(last, first, way);
        }
    }

    private void saveDirectedWay(OsmObject src, OsmObject dst, OsmWay way){
        Map<OsmObject, OsmWay> srcMap = AdjacentMap.get(src);

        if(srcMap == null){
            srcMap = new HashMap<>();
            srcMap.put(dst, way);
            AdjacentMap.put(src,srcMap);
        }else if(!srcMap.containsKey(dst)){
            srcMap.put(dst, way);
        }
    }

//    /**
//     * @param obj object at which edge will be split:
//     * @param edge in input: start--target connection
//     * @param baseWay base for new edge
//     * Method creates start-X-target such connection
//     * Where X is a new node created from @param obj
//     *
//     * returns the right part of above connection: X-target
//     */
//    private void splitEdgeAt(OsmObject obj, Edge edge, OsmWay baseWay) {
//        // remove old edge between two nodes:
//        if(obj.getID() == edge.getNode1().getOsmID() || obj.getID() == edge.getNode2().getOsmID()) {
//            return;
//        }
//
//        // get node at the middle:
//        Node node = getOrCreateNode(obj);
//
////        Edge leftEdge = addEdge(edge.getNode2(), node, baseWay);
////        Edge rightEdge = addEdge(node, edge.getNode1(), baseWay);
//
//        String leftEdgeKey = edge.getNode2().getOsmID().toString() + node.getOsmID();
//        String rightEdgeKey = node.getOsmID() + edge.getNode1().getOsmID().toString();
//
//        if(AdjacentMap.containsKey(leftEdgeKey)) {
//            add
//        }
////        if(leftEdge != null && EDGES.containsKey()) {
////            EDGES.add(leftEdge);
////        }
////        if(rightEdge != null && EDGES.containsKey(node.getOsmID() + edge.getNode1().getOsmID().toString())){
////            EDGES.add(rightEdge);
////        }// add to do  check if need to switch nodes
//
//        AdjacentMap.remove(edge);
//    }

//    private Edge addEdge(Node src, Node dst, OsmWay way){
//
//        String leftEdgeKey = src.getOsmID().toString() + dst.getOsmID();
//
//        if(EDGES.containsKey(leftEdgeKey)) {
//            EDGES.put(src.getOsmID().toString() + dst.getOsmID(), );
//        }
//        boolean dstToSrc = dst.isAdjacent(src);
////        if(src.getOsmID() == 3646651491l || src.getOsmID() == 3646651491l){
////            System.out.println("stop");
////        }
////        if(src.getOsmID() == 3646651491l || src.getOsmID() == 3646651491l){
////            System.out.println("stop");
////        }
//        Edge edge = new Edge(way, src, dst);
//
//        if(dstToSrc){
//            edge = dst.getEdgeTo(src);
//        }
//        src.addEdge(edge);
//        EDGES.add(edge);
//
//        return edge;
//    }

//    private void setNodeEdge(OEdge e){
//        if(!e.getStartNode().isAdjacent(e.getEndNode())) {
//            // TODO make edge bi-directional if needed
//            e.getStartNode().addEdge(e);
//            e.getEndNode().addEdge(e);
//        }
//
//        e.getLength();
//    }
//    private Node getOrCreateNode(OsmObject object) {
//        RoadMap map = RoadMap.getInstance();
//        Long junctionID = Reader.getJunctions().get(object.getID());
//        Node node = map.getNode(object.getID());
//
//        if(junctionID == null){
//            if(node == null){
//                node = map.addNode(object);
////                node = map.addNode(new Node(object), object.getID());
//            }
//        }else{
//            // node is part of a junction
//            node = map.getNode(junctionID);
//            if(node == null){
//                node = map.getJunctionNodes().get(junctionID);
//
//                if(node == null){
//                    node = new Node(object);
//                    map.getJunctionNodes().put(junctionID, node);
//                    map.addNode(node, object.getID());
//                }
//            }
//        }
//        return node;
//    }
}
