package controller.utils;

import model.GeoLocation;
import model.Path;
import model.RoadMap;
import model.Node;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static controller.utils.LogHandler.LOGGER;

/**
 *      |==================================|
 *      |==========| GRAPH ALGO |==========|
 *      |==================================|
 *
 *   algorithms on graph:
 *      - find nearest Node to
 *      - get distance between nodes in multiply formats
 *      - get connected component of a given node
 *      - get path using A*
 *      - remove nodes that are not part of a given node connected component
 *
 *
 * @author  Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 1.0
 * @since   2021-06-20
 */
public final class GraphAlgo {
    private GraphAlgo() {}


//    public static double distance(double lat1, double lon1, double lat2, double lon2, String... unit) {
//        String unit1 = unit.length > 0 ? unit[0] : "K";
//
//        double theta = lon1 - lon2;
//        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
//        dist = Math.acos(dist);
//        dist = rad2deg(dist);
//        dist = dist * 60 * 1.1515;
//        if (unit1.equals("K")) {
//            dist = dist * 1.609344;
//        } else if (unit1.equals("N")) {
//            dist = dist * 0.8684;
//        }
//        return (dist);
//    }

    /**
     * Function to find the closest node to a given point
     * @param location- coordinates
     * @param nodes - collection of nodes
     * @return the closest node to the given coordinates
     */
    public static Node findClosestNode(GeoLocation location, Collection<Node> nodes){
    //Get the coordinates of the node
    Double latitude = location.getLatitude();
    Double longitude = location.getLongitude();

    //Assign default variables
    AtomicReference<Double> minDistance = new AtomicReference<>(Double.MAX_VALUE);
    AtomicReference<Node> closestNode = new AtomicReference<>();


    //Loop through all the nodes, and find the closest one
    nodes.forEach(other -> {
                double dist = distance(other.getCoordinates(), location);
                if(dist < minDistance.get()){
                    minDistance.set(dist);
                    closestNode.set(other);
                }
            });

    return closestNode.get();
}

//    /**
//     * This function will be adressed by all distance calculations
//     * between two nodes in space
//     * Current calculation method: Haversine Method
//     * @param lat1 start node latitude
//     * @param lon1 start node longitude
//     * @param lat2 end node latitude
//     * @param lon2 end node longitude
//     * @return calculated distance between two nodes
//     */
//
//
//    /**
//     This function will be adressed by all distance calculations
//     * between two nodes in space
//     * Current calculation method: Haversine Method
//     *
//     * @param node1
//     * @param node2
//     * @param unit
//     * @return calculated distance between two nodes by coordinates
//     */
//    public static double distance(Node node1, Node node2, String... unit) {
//        double lat1 = node1.getLatitude(), lon1 = node1.getLongitude(), lat2 = node2.getLatitude(), lon2 = node2.getLongitude();
//
//        String unit1 = unit.length > 0 ? unit[0] : "K";
//
//        double theta = lon1 - lon2;
//        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
//        dist = Math.acos(dist);
//        dist = rad2deg(dist);
//        dist = dist * 60 * 1.1515;
//        if (unit1.equals("K")) {
//            dist = dist * 1.609344;
//        } else if (unit1.equals("N")) {
//            dist = dist * 0.8684;
//        }
//        return (dist);
//    }

    public static double distance(GeoLocation location1, GeoLocation location2){
        return distance(location1.getLatitude(), location1.getLongitude(), location2.getLatitude(), location2.getLongitude());
    }
    public static double distance(double lat1, double lon1,double lat2,double lon2){


        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return dist;
    }

    /**
     * get all nodes connected to 'node'
     * with bfs
     * @param node
     * @return
     */
    public static List<Node> getConnectedComponent(Node node){
        // Mark all the vertices as not visited(By default
        // set as false)
        HashSet<Node> visited = new HashSet<>();

        // Create a queue for BFS
        LinkedList<Node> queue = new LinkedList<>();

        // Mark the current node as visited and enqueue it
        queue.add(node);

        while (queue.size() != 0)
        {
            // Dequeue a vertex from queue and print it
            node = queue.poll();
            for(Node n : node.getAdjacentNodes()){
                if(!visited.contains(n)){
                    queue.add(n);
                    visited.add(n);
                }
            }
        }
        LOGGER.fine("BFS from node "+ node.getOsmID());
        return visited.stream().toList();
    }

    private static HashMap<Node, Double> NodesH = new HashMap<>(), NodesG = new HashMap<>();

    private static void setH(Node node, Node other){ NodesH.put(node, node.distanceTo(other)); }

    private static void setG(Node node, double g){ NodesG.put(node, g); }

    private static Double getH(Node node){ return NodesH.get(node); }

    private static Double getG(Node node){ return NodesG.get(node); }

    /**
     * calculate shortest path using A*
     *
     * @param src origin
     * @param dst destination
     * @return shortest path from src to dest
     */
    public static Path getShortestPath(Node src, Node dst){

        Hashtable<String, Node> CloseSet = new Hashtable<>();
        Hashtable<String, Node> OpenSet = new Hashtable<>();
        PriorityQueue<Node> PQ_OpenSet = new PriorityQueue<>();
        Hashtable<Node, Node> cameFrom = new Hashtable<>();

        setH(src,dst);
        setG(src, 0);
        src.setF(getG(src) + getH(src));

        OpenSet.put(src.getId(), src);
        PQ_OpenSet.add(src);

        while(!OpenSet.isEmpty()){
            Node current = PQ_OpenSet.poll();
            if(current.equals(dst)){
                LOGGER.fine("Path between "+src.getOsmID()+" and "+ dst.getOsmID() +" found using A*.");
                return reconstructPath(cameFrom, dst);
            }

            OpenSet.remove(current.getId());
            CloseSet.put(current.getId(), current);
            for(Node neighbor : current.getAdjacentNodes()){
                if(CloseSet.containsKey(neighbor.getId())){
                    continue;
                }
                double TentativeGScore = getG(current) + current.distanceTo(neighbor);//TODO use weight instead of distance
                if(!OpenSet.containsKey(neighbor.getId()) || TentativeGScore < getG(neighbor)){
                    cameFrom.put(neighbor, current);
                    setG(neighbor, TentativeGScore);
                    setH(neighbor, dst);
                    neighbor.setF(getG(neighbor) + getH(neighbor));
                    if(!OpenSet.containsKey(neighbor.getId())){
                        OpenSet.put(neighbor.getId(), neighbor);
                        PQ_OpenSet.add(neighbor);
                    }
                }
            }
        }
        LOGGER.severe("couldn't find path between nodes, src "+ src+", dst "+ dst);
        return null;

    }

    private static Path reconstructPath(Hashtable<Node, Node> cameFrom, Node n){
        List<Node> pathNodes = new ArrayList<>();
        while(cameFrom.containsKey(n)){
            n = cameFrom.get(n);
            pathNodes.add(n);
        }
        Collections.reverse(pathNodes);
        Path path =  new Path(pathNodes);
        return path;
    }

    public static void removeNodesThatNotConnectedTo(Node src){
        RoadMap map = RoadMap.getInstance();
        //delete node that aren't connected to src
        List<Node> connectedComponent = getConnectedComponent(src);

        List<Node> notPartOfComponent = map.getNodes().stream()
                .filter(node -> !connectedComponent.contains(node))
                .collect(Collectors.toList());

        LOGGER.info(notPartOfComponent.size() + " nodes that are not part of main component are found and being removed.");

        map.removeNodes(notPartOfComponent);
    }

    /**
     :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     ::  This function converts decimal degrees to radians            ::
     :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */
    private static double deg2rad(double deg) {
        return deg * Math.PI / 180.0;
    }

    /**
     :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     ::  This function converts radians to decimal degrees             :
     :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */
    private static double rad2deg(double rad) {
        return rad * 180.0 / Math.PI;
    }

    public static double hourToSeconds(double hour){
        return hour * 3600;
    }

    public static void main(String[] args) {
        GeoLocation g1 = new GeoLocation(32.07797575620036, 34.79729189827567);
        GeoLocation g2 = new GeoLocation(32.05726675523634, 34.75974355641115);

        Random random = new Random(1);

        for (int i = 0; i < 10; i++) {
            System.out.println(random.nextInt(20));
        }


        // //check if duplicate
        //    public boolean isOpposite(Edge other){
        //        return getNode1().getOsmID().equals(other.getNode2().getOsmID()) && getNode2().getOsmID().equals(other.getNode1().getOsmID());
        //    }
    }
}

