package app.model.utils;

import app.model.graph.Path;
import app.model.graph.RoadMap;
import app.model.graph.Node;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static utils.logs.LogHandler.LOGGER;
import static utils.Utils.validate;

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
    public static Node findClosestNode(Coordinates location, Collection<Node> nodes){
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

//    /*
//     * This function will be adressed by all distance calculations
//     * between two nodes in space
//     * Current calculation method: Haversine Method
//     * @param lat1 choose node latitude
//     * @param lon1 choose node longitude
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


    public static Node getClosestNode(Node node, ArrayList<Node> nodes){
        return nodes.stream().min(Comparator.comparingDouble(node::distanceTo)).orElse(null);
    }

    public static double distance(Coordinates location1, Coordinates location2){
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
     * get all nodes connected to 'node' with bfs.
     *
     * TODO replace with getBiggestConnectedComponent()  method (no need to specify node)
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
        LOGGER.fine("BFS from node "+ node.getId());
        return visited.stream().toList();
    }

    private static List<Node> getCC(Node node, List<Node> nodes){
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
                    nodes.remove(n);
                }
            }
        }
        LOGGER.fine("BFS from node "+ node.getId());
        return visited.stream().toList();
    }

    private static List<Node> getLargestCC(){
        List<Node> nodes = new ArrayList<>(RoadMap.INSTANCE.getNodes());

        Node componentNode = nodes.remove(0);
        List<Node> largestCC = getCC(componentNode, nodes);

        while(!nodes.isEmpty() && nodes.size() > largestCC.size()){
            componentNode = nodes.remove(0);
            List<Node> CC = getCC(componentNode, nodes);
            if(CC.size() > largestCC.size()){
                largestCC = CC;
            }
        }

        return largestCC;
    }

    /**
     * delete from graph all nodes that aren't part of the largest connected component.
     */
    public static void extractLargestCC(){
        RoadMap.INSTANCE.removeAllNodesBut(getLargestCC());
    }


    public static Path getShortestPathAlgoNodesVersion(AlgoNode src, AlgoNode dst) {

        Hashtable<Long, AlgoNode> CloseSeta = new Hashtable<>();
        Hashtable<Long, AlgoNode> OpenSeta = new Hashtable<>();
        PriorityQueue<AlgoNode> PQ_OpenSeta = new PriorityQueue<>();
        Hashtable<Node, Node> cameFroma = new Hashtable<>();

        src.setH(src.getNode().distanceTo(dst.getNode()));
        src.setG(0);
        src.setF(src.getH() + src.getG());

        OpenSeta.put(src.getNode().getId(), src);
        PQ_OpenSeta.add(src);

        while(!OpenSeta.isEmpty()){
            AlgoNode current = PQ_OpenSeta.poll();
            if(current.equals(dst)){
//                LOGGER.fine("Path between "+src.getNode().getId()+" and "+ dst.getNode().getId() +" found using A*.");
                return reconstructPath(cameFroma, dst.getNode());
            }

            OpenSeta.remove(current.getNode().getId());
            CloseSeta.put(current.getNode().getId(), current);

            List<AlgoNode> algoNodeNeighbors = nodesToAlgoNodes(current.getNode().getAdjacentNodes());
            for(AlgoNode neighbor : algoNodeNeighbors){
                if(CloseSeta.containsKey(neighbor.getNode().getId())){
                    continue;
                }
                double TentativeGScore = current.getG() + current.getNode().getEdgeTo(neighbor.getNode()).getWeight(); //TODO use weight instead of distance
                if(!OpenSeta.containsKey(neighbor.getNode().getId()) || TentativeGScore < neighbor.getG()){
                    cameFroma.put(neighbor.getNode(), current.getNode());
                    neighbor.setG(TentativeGScore);
                    neighbor.setH(neighbor.getNode().distanceTo(dst.getNode()));
                    neighbor.setF(neighbor.getH() + neighbor.getG());
                    if(!OpenSeta.containsKey(neighbor.getNode().getId())){
                        OpenSeta.put(neighbor.getNode().getId(), neighbor);
                        PQ_OpenSeta.add(neighbor);
                    }
                }
            }
        }
        LOGGER.severe("couldn't find path between nodes, src "+ src+", dst "+ dst);
        return null;

    }

    /**
     * calculate shortest path using A*
     *
     * @param src origin
     * @param dst destination
     * @return shortest path from src to dest
     */
    public static Path getShortestPath(Node src, Node dst){

        AlgoNode algoSrc = new AlgoNode(src);
        AlgoNode algoDst = new AlgoNode(dst);

        return getShortestPathAlgoNodesVersion(algoSrc, algoDst);

//        Hashtable<Long, Node> CloseSet = new Hashtable<>();
//        Hashtable<Long, Node> OpenSet = new Hashtable<>();
//        PriorityQueue<Node> PQ_OpenSet = new PriorityQueue<>();
//        Hashtable<Node, Node> cameFrom = new Hashtable<>();
//
//        setH(src,dst);
//        setG(src, 0);
//        src.setF(getG(src) + getH(src));
//        AlgoNode aSrc = new AlgoNode(src);
//        OpenSet.put(aSrc.getNode().getId(), src);
//        PQ_OpenSet.add(src);
//
//        while(!OpenSet.isEmpty()){
//            Node current = PQ_OpenSet.poll();
//            if(current.equals(dst)){
//                LOGGER.fine("Path between "+src.getId()+" and "+ dst.getId() +" found using A*.");
//                return reconstructPath(cameFrom, dst);
//            }
//
//            OpenSet.remove(current.getId());
//            CloseSet.put(current.getId(), current);
//            for(Node neighbor : current.getAdjacentNodes()){
//                if(CloseSet.containsKey(neighbor.getId())){
//                    continue;
//                }
//                double TentativeGScore = getG(current) + current.getEdgeTo(neighbor).getWeight(); //TODO use weight instead of distance
//                if(!OpenSet.containsKey(neighbor.getId()) || TentativeGScore < getG(neighbor)){
//                    cameFrom.put(neighbor, current);
//                    setG(neighbor, TentativeGScore);
//                    setH(neighbor, dst);
//                    neighbor.setF(getG(neighbor) + getH(neighbor));
//                    if(!OpenSet.containsKey(neighbor.getId())){
//                        OpenSet.put(neighbor.getId(), neighbor);
//                        PQ_OpenSet.add(neighbor);
//                    }
//                }
//            }
//        }
//        LOGGER.severe("couldn't find path between nodes, src "+ src+", dst "+ dst);
//        return null;

    }

    private static List<AlgoNode> nodesToAlgoNodes(List<Node> neighbors) {
        ArrayList<AlgoNode> algoNeighbors = new ArrayList<>();

        neighbors.stream().forEach(neighbor -> algoNeighbors.add(new AlgoNode(neighbor)));

        return algoNeighbors;
    }

    private static Path reconstructPath(Hashtable<Node, Node> cameFrom, Node n){
        List<Node> pathNodes = new ArrayList<>();
        pathNodes.add(n);
        double pathWeight = 0;
        while(cameFrom.containsKey(n)){
            pathWeight += n.getEdgeTo(cameFrom.get(n)).getWeight();
            n = cameFrom.get(n);
            pathNodes.add(n);
        }
        Collections.reverse(pathNodes);
        Path path =  new Path(pathNodes, pathWeight);
        validate(path.getNodes().size() > 1, "Bad path size :"+ pathNodes.size());
        return path;
    }

//    private static Path reconstructPatha(Hashtable<AlgoNode, AlgoNode> cameFrom, AlgoNode n){
//        List<Node> pathNodes = new ArrayList<>();
//        pathNodes.add(n.getNode());
//        double pathWeight = 0;
//        while(cameFrom.containsKey(n)){
//            pathWeight += n.getNode().getEdgeTo(cameFrom.get(n).getNode()).getWeight();
//            n = cameFrom.get(n);
//            pathNodes.add(n.getNode());
//        }
//        Collections.reverse(pathNodes);
//        Path path =  new Path(pathNodes, pathWeight);
//        validate(path.getNodes().size() > 1, "Bad path size :"+ pathNodes.size());
//        return path;
//    }

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

    public static long hourToMS(double hour){
        return (long) (hour * 3600000);
    }

    public static long minToMs(double minute){
        return (long) (minute * 60000);
    }

    public static double MsToHours(long hour){
        return hour / 3600000.0;
    }

    public static double MsToMinutes(long hour){
        return hour / 60000.0;
    }

    public static double MsToSeconds(long hour){
        return hour / 1000.0;
    }



}

