package controller;

import model.*;

import java.util.*;

public class GraphUtils {
    private static List<OPath> _paths =  new ArrayList<>();
    private static Hashtable<OPath, String> labeledPaths = new Hashtable<>();
    private static Map<Long, ONode> _riders = new HashMap<>();
    private static Map<String, ONode> locations = new HashMap<>();
    private static Map<String, Drive> drives = new HashMap<>();
    private static OGraph graph = OGraph.getInstance();

    /**
     * This function will be adressed by all distance calculations
     * between two nodes in space
     * Current calculation method: Haversine Method
     * @param lat1 start node latitude
     * @param lon1 start node longitude
     * @param lat2 end node latitude
     * @param lon2 end node longitude
     * @return calculated distance between two nodes
     */
    public static double distance(double lat1, double lon1, double lat2, double lon2, String... unit) {
        String unit1 = unit.length > 0 ? unit[0] : "K";

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit1.equals("K")) {
            dist = dist * 1.609344;
        } else if (unit1.equals("N")) {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /**
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            ::  This function converts decimal degrees to radians            ::
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /**
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            ::  This function converts radians to decimal degrees             :
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    private static Map<Long, Map<Long, OEdge>>  nodeEdges = new HashMap<>();

    public static boolean addPath(List<Object> pathNodes){
        return _paths.add(new OPath(pathNodes));
    }

    public static String addLabeledPath(List<Object> pathNodes, String label){
        return labeledPaths.put(new OPath(pathNodes), label);
    }

    public static Hashtable<OPath, String> getLabeledPaths(){
        return labeledPaths;
    }

    public static Map<Long, Map<Long, OEdge>> getNodeEdges() {
        return nodeEdges;
    }

    public static void addEdgeToMap(ONode n1, ONode n2, OEdge e){
        addEdgeHelper(n2, n1, e);
        addEdgeHelper(n1, n2, e);
    }

    private static void addEdgeHelper(ONode n1, ONode n2, OEdge e) {
        if(!nodeEdges.containsKey(n2.getOsm_Id())){
            Map<Long, OEdge> n2Map = new HashMap<>();
            n2Map.put(n1.getOsm_Id(),e);
            nodeEdges.put(n2.getOsm_Id(), n2Map);
        }else{
            nodeEdges.get(n2.getOsm_Id()).put(n1.getOsm_Id(), e);
        }
    }

    public static Map<String, ONode> getLocations() {
        return locations;
    }

    public static void setLocations(Map<String, ONode> locations) {
        GraphUtils.locations = locations;
    }

    public static void setPaths(List<OPath> paths) {
        _paths = paths;
    }

    public static List<OPath> getPaths() {
        return _paths;
    }

    public static boolean setRiders(Map<Long, Double[]> Riders){

        Riders.entrySet().forEach(entry -> {
            ONode node = new ONode(entry.getKey(), entry.getValue(), ONode.userType.Rider);
            node.setUser(ONode.userType.Rider);
            _riders.put(entry.getKey(), node);
        });

        return true;
    }

    public static Map<Long, ONode> getRiders() {
        return _riders;
    }

    /**
     * get all nodes connected to 'node'
     * with bfs
     * @param node
     * @return
     */
    public static List<ONode> getConnectedComponent(ONode node){
        // Mark all the vertices as not visited(By default
        // set as false)
        HashSet<ONode> visited = new HashSet<>();

        // Create a queue for BFS
        LinkedList<ONode> queue = new LinkedList<>();

        // Mark the current node as visited and enqueue it
        queue.add(node);

        while (queue.size() != 0)
        {
            // Dequeue a vertex from queue and print it
            node = queue.poll();
            for(ONode n : node.getAdjacentNodes()){
                if(!visited.contains(n)){
                    queue.add(n);
                    visited.add(n);
                }
            }
        }

        return visited.stream().toList();
    }

    public static List<Object> AStar(ONode start, ONode end){
        Hashtable<String, ONode> CloseSet = new Hashtable<>();
        Hashtable<String, ONode> OpenSet = new Hashtable<>();
        PriorityQueue<ONode> PQ_OpenSet = new PriorityQueue<>();
        Hashtable<ONode, ONode> cameFrom = new Hashtable<>();

        start.setH(end);
        start.setG(0);
        start.setF(start.getG() + start.getH());

        OpenSet.put(start.getNode_id(), start);
        PQ_OpenSet.add(start);

        while(!OpenSet.isEmpty()){
            ONode current = PQ_OpenSet.poll();
            if(current.equals(end)){
                return reconstructPath(cameFrom, end);
            }

            OpenSet.remove(current.getNode_id());
            CloseSet.put(current.getNode_id(), current);
            for(ONode neighbor : current.getAdjacentNodesFromGraph()){
                if(CloseSet.containsKey(neighbor.getNode_id())){
                    continue;
                }
                double TentativeGScore = current.getG() + distance(current.getLatitude(), current.getLongitude(), neighbor.getLatitude(), neighbor.getLongitude());
                if(!OpenSet.containsKey(neighbor.getNode_id()) || TentativeGScore < neighbor.getG()){
                    cameFrom.put(neighbor, current);
                    neighbor.setG(TentativeGScore);
                    neighbor.setH(end);
                    neighbor.setF(neighbor.getG() + neighbor.getH());
                    if(!OpenSet.containsKey(neighbor.getNode_id())){
                        OpenSet.put(neighbor.getNode_id(), neighbor);
                        PQ_OpenSet.add(neighbor);
                    }
                }
            }
        }
        return null;
    }

    private static List<Object> reconstructPath(Hashtable<ONode, ONode> cameFrom, ONode n){
        List<Object> path = new ArrayList<>();
        while(cameFrom.containsKey(n)){
            n = cameFrom.get(n);
            path.add(n);
        }
        Collections.reverse(path);
        return path;
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
