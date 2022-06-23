package controller.utils;

import model.RoadMap;
import model.Node;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
public class GraphAlgo {


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

    public static Node findClosestNode(Node node){
    //Get the coordinates of the node
    Double latitude = node.getLatitude();
    Double longitude = node.getLongitude();

    //Assign default variables
    AtomicReference<Double> minDistance = new AtomicReference<>(Double.MAX_VALUE);
    AtomicReference<Node> closestNode = new AtomicReference<>(node);

    //Loop through all the nodes that are not from Rider type, and find the closest one
    RoadMap.getInstance().getNodes().values().stream().filter(n -> n.getType() != Node.userType.Passenger)
            .forEach(other -> {
                double dist = GraphAlgo.distance(node, other);
                if(dist < minDistance.get()){
                    minDistance.set(dist);
                    closestNode.set(other);
                }
            });

    return closestNode.get();
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

    /**
     * Function to find the closest node to a given point
     * @param node- the node to which the closest node is to be found
     * @return the closest node to the given node
     */
    /**
     This function will be adressed by all distance calculations
     * between two nodes in space
     * Current calculation method: Haversine Method
     *
     * @param node1
     * @param node2
     * @param unit
     * @return calculated distance between two nodes by coordinates
     */
    public static double distance(Node node1, Node node2, String... unit) {
        double lat1 = node1.getLatitude(), lon1 = node1.getLongitude(), lat2 = node2.getLatitude(), lon2 = node2.getLongitude();

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
     * get all nodes connected to 'node'
     * with bfs
     * @param node
     * @return
     */
    public static List<Node> getConnectedComponent(Node node){
        ArrayList<Long> problems = new ArrayList<>(Arrays.asList(340375216l, 289499143l, 3988271550l)); //TODO debug

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
                if(problems.contains(n.getOsmID())){
                    boolean a = true;
                }
                if(!visited.contains(n)){
                    queue.add(n);
                    visited.add(n);
                }
            }
        }

        return visited.stream().toList();
    }

    private static HashMap<Node, Double> NodesH = new HashMap<>(), NodesG = new HashMap<>();

    private static void setH(Node node, Node other){ NodesH.put(node, distance(node, other)); }

    private static void setG(Node node, double g){ NodesG.put(node, g); }

    private static Double getH(Node node){ return NodesH.get(node); }

    private static Double getG(Node node){ return NodesG.get(node); }

    /**
     * calculate shortest path using A*
     *
     * @param src origin
     * @param dest destination
     * @return shortest path from src to dest
     */
    public static List<Node> getShortestPath(Node src, Node dest){

        Hashtable<String, Node> CloseSet = new Hashtable<>();
        Hashtable<String, Node> OpenSet = new Hashtable<>();
        PriorityQueue<Node> PQ_OpenSet = new PriorityQueue<>();
        Hashtable<Node, Node> cameFrom = new Hashtable<>();

        setH(src,dest);
        setG(src, 0);
        src.setF(getG(src) + getH(src));

        OpenSet.put(src.getId(), src);
        PQ_OpenSet.add(src);

        while(!OpenSet.isEmpty()){
            Node current = PQ_OpenSet.poll();
            if(current.equals(dest)){
                return reconstructPath(cameFrom, dest);
            }

            OpenSet.remove(current.getId());
            CloseSet.put(current.getId(), current);
            for(Node neighbor : current.getAdjacentNodesFromGraph()){
                if(CloseSet.containsKey(neighbor.getId())){
                    continue;
                }
                double TentativeGScore = getG(current) + distance(current, neighbor);
                if(!OpenSet.containsKey(neighbor.getId()) || TentativeGScore < getG(neighbor)){
                    cameFrom.put(neighbor, current);
                    setG(neighbor, TentativeGScore);
                    setH(neighbor, dest);
                    neighbor.setF(getG(neighbor) + getH(neighbor));
                    if(!OpenSet.containsKey(neighbor.getId())){
                        OpenSet.put(neighbor.getId(), neighbor);
                        PQ_OpenSet.add(neighbor);
                    }
                }
            }
        }
        return null;
    }

    private static List<Node> reconstructPath(Hashtable<Node, Node> cameFrom, Node n){
        List<Node> path = new ArrayList<>();
        while(cameFrom.containsKey(n)){
            n = cameFrom.get(n);
            path.add(n);
        }
        Collections.reverse(path);
        return path;
    }

    public static void removeNodesThatNotConnectedTo(Node src){
        RoadMap map = RoadMap.getInstance();
        //delete node that aren't connected to src
        List<Node> connectedComponent = getConnectedComponent(src);

        List<Node> notPartOfComponent = map.getNodes().entrySet().stream()
                .filter(e -> !connectedComponent.contains(e.getValue()))
                .map(e -> e.getValue())
                .collect(Collectors.toList());

        map.removeNodes(notPartOfComponent);
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
}
//        ArrayList<Long> problems = new ArrayList<>();
//        problems.add(340375216l);
//        problems.add(289499143l);
//        problems.add(3988271550l);
//
//        for(ONode node: notPartOfComponent){
//            Long nodeId = node.getOsmID();
//            problems.remove(node.getOsmID());
//
//        }
//        if(!problems.isEmpty()){
//            System.out.println("יוסטון וי האב אה פראבלס");
//            System.out.println(problems + " not found");
//            problems.remove(340375216l);
//            System.out.println("fuuucckkk");
//        }
//        problems = new ArrayList<>();
//        problems.add(340375216l);
//        problems.add(289499143l);
//        problems.add(3988271550l);
//        for(ONode node: connectedComponent){
//            Long nodeId = node.getOsmID();
//            problems.remove(node.getOsmID());
//
//        }
//        if(!problems.isEmpty()){
//            System.out.println("יוסטון וי האב אה פראבלס");
//            System.out.println(problems + " not found");
//            problems.remove(340375216l);
//            System.out.println("fuuucckkk");
//        }
