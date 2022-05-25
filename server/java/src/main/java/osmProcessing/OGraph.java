package osmProcessing;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class OGraph {
    // edges id generated by calculacateEdgeId() method below:
    private Map<Long, OEdge> edges;
    // access through node id:
    private Map<Long, ONode> nodes;

    private Map<Long, ONode> junctionNodes = new HashMap<>();

    HashMap<Long, Integer> nodesQuantity = new HashMap<>();

    /**
     * Singleton specific properties:
     */

    private static OGraph INSTANCE = new OGraph();

    private OGraph() {
        this.edges = new HashMap<>();
        this.nodes = new HashMap<>();
    }

    public static OGraph getInstance() {
        return INSTANCE;
    }


    /**
     * Getters:
     */

    public Map<Long, OEdge> getEdges() {
        return edges;
    }

    public Map<Long, ONode> getNodes() {
        return nodes;
    }

    public ONode getNode(long key){
        return this.nodes.get(key);
    }

    public Map<Long, ONode> getJunctionNodes() {
        return junctionNodes;
    }

    /**
     * Setters:
     */

    public OEdge addEdge(long id, OEdge e){
        this.edges.put(id, e);
        return e;
    }

    public ONode addNode(ONode node, long id){
        nodes.put(id, node);
        return node;
    }

    public OEdge removeEdge(long id){
        return this.edges.remove(id);
    }

    /**
     * Function to find the closest node to a given point
     * @param node- the node to which the closest node is to be found
     * @return the closest node to the given node
     */
    public ONode findClosestNode(ONode node){
        //Get the coordinates of the node
        Double latitude = node.getLatitude();
        Double longitude = node.getLongitude();

        //Assign default variables
        AtomicReference<Double> minDistance = new AtomicReference<>(Double.MAX_VALUE);
        AtomicReference<ONode> closestNode = new AtomicReference<>(node);

        //Loop through all the nodes that are not from Rider type, and find the closest one
        this.nodes.values().stream().filter(n -> n.getUser() != ONode.userType.Rider)
                .forEach(n -> {
                    double dist = GraphUtils.getInstance().distance(latitude, longitude, n.getLatitude(), n.getLongitude());
                    if(dist < minDistance.get()){
                        minDistance.set(dist);
                        closestNode.set(n);
                    }
                });

        return closestNode.get();
    }
}