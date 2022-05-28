package osmProcessing;

import java.util.*;

public class GraphUtils {

    private static GraphUtils instance = new GraphUtils();
    private List<OPath> paths;
    private Hashtable<OPath, String> labeledPaths;
    private Map<Long, ONode> riders;
    private OGraph graph;

    private GraphUtils() {
        paths = new ArrayList<>();
        riders = new HashMap<>();
        graph = OGraph.getInstance();
        labeledPaths = new Hashtable<>();
    }

    public static synchronized GraphUtils getInstance() {
        return instance;
    }

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
    //Function to find distance between two earth coordinates
    public double distance(double lat1, double lon1, double lat2, double lon2, String... unit) {
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

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public boolean addPath(List<Object> pathNodes){
        return paths.add(new OPath(pathNodes));
    }

    public String addLabeledPath(List<Object> pathNodes, String label){
        return labeledPaths.put(new OPath(pathNodes), label);
    }

    public void setPaths(List<OPath> paths) {
        this.paths = paths;
    }

    public List<OPath> getPaths() {
        return paths;
    }

    public Hashtable<OPath, String> getLabeledPaths(){
        return labeledPaths;
    }

    public boolean setRiders(Map<Long, Double[]> Riders){

        Riders.entrySet().forEach(entry -> {
            ONode node = new ONode(entry.getKey(), entry.getValue(), ONode.userType.Rider);
            node.setUser(ONode.userType.Rider);
            riders.put(entry.getKey(), node);
        });

        return true;
    }

    public Map<Long, ONode> getRiders() {
        return riders;
    }

    public List<ONode> AStar(ONode start, ONode end){
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
            for(ONode neighbor : current.getAdjacentNodes()){
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

//    public List<ONode> AStar(ONode start, ONode end){
//        Hashtable<String, ONode> C = new Hashtable<>();
//        Hashtable<String, ONode> L1 = new Hashtable<>();
//        PriorityQueue<ONode> L = new PriorityQueue<>();
//        Hashtable<ONode, ONode> cameFrom = new Hashtable<>();
//
//        start.setH(end);
//        start.setG(0);
//        start.setF(start.getG() + start.getH());
//
//        L.add(start);
//        L1.put(start.getNode_id(), start);
//        while(!L.isEmpty()){
//            ONode n = L.poll();
//            if(n.equals(end)){
//                return reconstructPath(n);
//            }
//            C.put(n.getNode_id(), n);
//
//            for(ONode m : n.getAdjacentNodes()){
//                ONode neighbor = m;
//                neighbor.setH(end);
//                neighbor.setG(n.getG() + distance(n.getLatitude(), n.getLongitude(), neighbor.getLatitude(), neighbor.getLongitude()));
//                neighbor.setF(neighbor.getG() + neighbor.getH());
//                if(!C.containsKey(neighbor.getNode_id()) && !L1.containsKey(neighbor.getNode_id())){
//                    L.add(neighbor);
//                    L1.put(neighbor.getNode_id(), neighbor);
//                }
//                else if(L1.containsKey(neighbor.getNode_id())){
//                    if(L1.get(neighbor.getNode_id()).getF() > neighbor.getF()){
//                        L.remove(neighbor);
//                        L.add(neighbor);
//                        L1.put(neighbor.getNode_id(), neighbor);
//                    }
//                }
//            }
//        }
//        return  null;
//    }

    private List<ONode> reconstructPath(Hashtable<ONode, ONode> cameFrom, ONode n){
        List<ONode> path = new ArrayList<>();
        while(cameFrom.containsKey(n)){
            n = cameFrom.get(n);
            path.add(n);
        }
        Collections.reverse(path);
        return path;
    }
}
