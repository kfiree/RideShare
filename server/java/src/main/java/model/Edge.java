package model;

import controller.utils.GraphAlgo;
import controller.osm_processing.OsmWay;
import controller.utils.MapUtils;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 *      |==================================|
 *      |=============| EDGE  |============|
 *      |==================================|
 *
 *
 * @author  Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 1.0
 * @since   2021-06-20
 */
public class Edge {

    private final Node startNode, endNode;
    private final double weight;
    private double distance;
    static Set<String> tagNames = new HashSet<>();
    private final String id, highwayType;

    private static final Hashtable<String, Integer> SPEED_LIMIT =  new Hashtable<>()
    {{
                put("motorway", 110);
                put("trunk", 100);
                put("primary", 90);
                put("secondary", 70);
                put("tertiary", 50);
    }};

    private Edge(String id, Node startNode, Node endNode, Double weight, String highwayType){
//        if(startNode.getTags().get("oneway").equals("yes"))
//        directed = startNode.getTags().get("oneway") != null && startNode.getTags().get("oneway").equals("yes");
        tagNames.add(highwayType);
        this.startNode = startNode;
        this.endNode = endNode;
        this.id = id == null ? MapUtils.generateId(this) : id;
        this.highwayType = highwayType;
        this.weight = weight;

        //TODO init weight by length and max speed
    }

    public Edge(OsmWay way, Node startNode, Node endNode) {
        this(null, startNode, endNode,0.0, way.getTags().get("highway"));//TODO fix weight calculation
    }

    public Edge(String id, Long startNodeId, Long endNodeID, Double weight, String highwayType) {
        this(id, RegionMap.getInstance().getNode(startNodeId), RegionMap.getInstance().getNode(endNodeID), weight, highwayType);
    }

    /**
     * GETTERS
     */
    public String getId() { return id; }

    public String getHighwayType() {
        return highwayType;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public Double getDistance() {
        return distance;
    }

    public Double getWeight() {
        return weight;
    }

    public Node getOtherEnd(String nodeId){
        if(nodeId.equals(endNode.getId())) {
            return startNode;
        } else if(nodeId.equals(startNode.getId())) {
            return endNode;
        }
        return null;
    }


    /**
     * calculate distance between two geo points
     * using Haversine Method and store it in distance
     * attribute of the object
     */
    public Double getLength() {
        if(distance == 0) {
            distance = GraphAlgo.distance(startNode, endNode);
        }
        return distance;
    }

    public double getSpeedLimit() {
        return SPEED_LIMIT.get(highwayType);
    }

    public double getTime() {
        return distance / getSpeedLimit();
    }

    public boolean isOpposite(Edge other){
        return getStartNode().getOsmID().equals(other.getEndNode().getOsmID()) && getEndNode().getOsmID().equals(other.getStartNode().getOsmID());
    }


}
