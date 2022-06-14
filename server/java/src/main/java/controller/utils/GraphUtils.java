package controller.utils;

import model.OEdge;
import model.OGraph;
import model.ONode;
import model.OPath;

import java.util.*;
import java.util.stream.Collectors;

public class GraphUtils {
    private static List<OPath> _paths =  new ArrayList<>();
    private static Hashtable<OPath, String> labeledPaths = new Hashtable<>();
    private static Map<Long, ONode> _riders = new HashMap<>();

    public static boolean addPath(List<Object> pathNodes){
        return _paths.add(new OPath(pathNodes));
    }

    public static String addLabeledPath(List<Object> pathNodes, String label){
        return labeledPaths.put(new OPath(pathNodes), label);
    }

    public static Hashtable<OPath, String> getLabeledPaths(){
        return labeledPaths;
    }

    public static void setPaths(List<OPath> paths) {
        _paths = paths;
    }

    public static List<OPath> getPaths() {
        return _paths;
    }

    public static boolean setRiders(Map<Long, Double[]> Riders){

        Riders.entrySet().forEach(entry -> {
            ONode node = new ONode(null,entry.getKey(),entry.getValue()[0], entry.getValue()[1], ONode.userType.Rider);
            node.setUser(ONode.userType.Rider);
            _riders.put(entry.getKey(), node);
        });

        return true;
    }

    public static Map<Long, ONode> getRiders() {
        return _riders;
    }

    /**
     * create uuid
     * @param object
     * @return
     */
    public static String generateId(Object object) {
        String uid = UUID.randomUUID().toString();

//        switch(object.getClass().getSimpleName()) {
//            case "ONode":
//                uid = "n-" +uid.substring(2);
//                break;
//            case "OEdge":
//                uid = "e-" +uid.substring(2);
//                break;
//            case "OPath":
//                uid = "p-" +uid.substring(2);
//                break;
//            case "ORider":
//                uid = "r-" +uid.substring(2);
//                break;
//            case "ODriver":
//                uid = "d-" +uid.substring(2);
//                break;
//            default:
//                uid = "u-" +uid.substring(2);
//        }
        return uid;
    }
}

//    public static Map<Long, Map<Long, OEdge>> getNodeEdges() {
//        return nodeEdges;
//    }

//    public static void addEdgeToMap(ONode n1, ONode n2, OEdge e){
//        addEdgeHelper(n2, n1, e);
//        addEdgeHelper(n1, n2, e);
//    }
//
//    private static void addEdgeHelper(ONode n1, ONode n2, OEdge e) {
//        if(!nodeEdges.containsKey(n2.getOsm_Id())){
//            Map<Long, OEdge> n2Map = new HashMap<>();
//            n2Map.put(n1.getOsm_Id(),e);
//            nodeEdges.put(n2.getOsm_Id(), n2Map);
//        }else{
//            nodeEdges.get(n2.getOsm_Id()).put(n1.getOsm_Id(), e);
//        }
//    }