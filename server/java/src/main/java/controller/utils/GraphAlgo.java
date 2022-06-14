package controller.utils;

import model.OGraph;
import model.ONode;

import java.util.*;
import java.util.stream.Collectors;

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
     This function will be adressed by all distance calculations
     * between two nodes in space
     * Current calculation method: Haversine Method
     *
     * @param node1
     * @param node2
     * @param unit
     * @return calculated distance between two nodes by coordinates
     */
    public static double distance(ONode node1, ONode node2, String... unit) {
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
    public static List<ONode> getConnectedComponent(ONode node){
        ArrayList<Long> problems = new ArrayList<>(Arrays.asList(340375216l, 289499143l, 3988271550l)); //TODO debug

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
                if(problems.contains(n.getOsm_Id())){
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

    public static List<ONode> AStar(ONode start, ONode end){
        Hashtable<String, ONode> CloseSet = new Hashtable<>();
        Hashtable<String, ONode> OpenSet = new Hashtable<>();
        PriorityQueue<ONode> PQ_OpenSet = new PriorityQueue<>();
        Hashtable<ONode, ONode> cameFrom = new Hashtable<>();

        start.setH(end);
        start.setG(0);
        start.setF(start.getG() + start.getH());

        OpenSet.put(start.getId(), start);
        PQ_OpenSet.add(start);

        while(!OpenSet.isEmpty()){
            ONode current = PQ_OpenSet.poll();
            if(current.equals(end)){
                return reconstructPath(cameFrom, end);
            }

            OpenSet.remove(current.getId());
            CloseSet.put(current.getId(), current);
            for(ONode neighbor : current.getAdjacentNodesFromGraph()){
                if(CloseSet.containsKey(neighbor.getId())){
                    continue;
                }
                double TentativeGScore = current.getG() + distance(current, neighbor);
                if(!OpenSet.containsKey(neighbor.getId()) || TentativeGScore < neighbor.getG()){
                    cameFrom.put(neighbor, current);
                    neighbor.setG(TentativeGScore);
                    neighbor.setH(end);
                    neighbor.setF(neighbor.getG() + neighbor.getH());
                    if(!OpenSet.containsKey(neighbor.getId())){
                        OpenSet.put(neighbor.getId(), neighbor);
                        PQ_OpenSet.add(neighbor);
                    }
                }
            }
        }
        return null;
    }

    private static List<ONode> reconstructPath(Hashtable<ONode, ONode> cameFrom, ONode n){
        List<ONode> path = new ArrayList<>();
        while(cameFrom.containsKey(n)){
            n = cameFrom.get(n);
            path.add(n);
        }
        Collections.reverse(path);
        return path;
    }

    public static void removeNodesThatNotConnectedTo(ONode src){
        OGraph graph = OGraph.getInstance();
        //delete node that aren't connected to src
        List<ONode> connectedComponent = getConnectedComponent(src);

        List<ONode> notPartOfComponent = graph.getNodes().entrySet().stream()
                .filter(e -> !connectedComponent.contains(e.getValue()))
                .map(e -> e.getValue())
                .collect(Collectors.toList());

        graph.removeNodes(notPartOfComponent);
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
//            Long nodeId = node.getOsm_Id();
//            problems.remove(node.getOsm_Id());
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
//            Long nodeId = node.getOsm_Id();
//            problems.remove(node.getOsm_Id());
//
//        }
//        if(!problems.isEmpty()){
//            System.out.println("יוסטון וי האב אה פראבלס");
//            System.out.println(problems + " not found");
//            problems.remove(340375216l);
//            System.out.println("fuuucckkk");
//        }
