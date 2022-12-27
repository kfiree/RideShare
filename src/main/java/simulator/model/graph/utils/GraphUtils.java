package simulator.model.graph.utils;

import simulator.model.graph.Node;
import simulator.model.graph.RoadMap;
import simulator.model.graph.utils.Coordinates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class GraphUtils {
    public static long hourToMS(double hour){
        return (long) (hour * 3600000);
    }

    /**
     * delete from graph all nodes that aren't part of the largest connected component.
     */
    public static void extractLargestCC(){
        RoadMap.INSTANCE.removeAllNodesBut(getLargestCC());
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
//        LOGGER.fine("BFS from node "+ node.getId());
        return visited.stream().toList();
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
     * converter
     * @param deg decimal degree
     * @return radian
     */
    private static double deg2rad(double deg) {
        return deg * Math.PI / 180.0;
    }

    /**
     * converter
     * @param rad radian
     * @return decimal degree
     */
    private static double rad2deg(double rad) {
        return rad * 180.0 / Math.PI;
    }
}
