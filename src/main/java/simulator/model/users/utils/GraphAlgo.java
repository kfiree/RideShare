package simulator.model.users.utils;

import simulator.model.graph.utils.GraphUtils;
import simulator.model.graph.utils.Coordinates;
import simulator.model.graph.Path;
import simulator.model.graph.Node;
import utils.Utils;
import utils.logs.LogHandler;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
 * @author  Kfir Ettinger
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
                double dist = GraphUtils.distance(other.getCoordinates(), location);
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
        LogHandler.LOGGER.fine("BFS from node "+ node.getId());
        return visited.stream().toList();
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
        LogHandler.LOGGER.severe("couldn't find path between nodes, src "+ src+", dst "+ dst);
        return null;

    }

    /**
     * calculate the shortest path using A*
     *
     * @param src origin
     * @param dst destination
     * @return shortest path from src to dest
     */
    public static Path getShortestPath(Node src, Node dst){

        AlgoNode algoSrc = new AlgoNode(src);
        AlgoNode algoDst = new AlgoNode(dst);

        return getShortestPathAlgoNodesVersion(algoSrc, algoDst);

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
        Utils.validate(path.getNodes().size() >= 1, "Bad path size :"+ pathNodes.size());
        return path;
    }

}

