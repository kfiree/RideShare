package model;

import controller.utils.GraphAlgo;
import controller.osmProcessing.OMapWay;
import controller.utils.GraphUtils;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class OEdge {

    /**
     * PROPERTIES:
     */
    private ONode startNode, endNode;
    private Double distance, weight, length; //TODO clean redundant field
    static Set<String> tagNames = new HashSet<>();
    private String id = "", name, highwayType;
    private boolean directed;

    private final Hashtable<String, Integer> speedLimit =  new Hashtable<>()
    {{
                put("motorway", 110);
                put("trunk", 100);
                put("primary", 90);
                put("secondary", 70);
                put("tertiary", 50);
    }};

    private OEdge(String id, ONode startNode, ONode endNode, Double weight, Double distance, String name, String highwayType){
//        if(startNode.getTags().get("oneway").equals("yes"))
        tagNames.add(highwayType);
        this.directed = startNode.getTags().get("oneway") != null && startNode.getTags().get("oneway").equals("yes");
        this.startNode = startNode;
        this.endNode = endNode;
        this.id = id == null ? GraphUtils.generateId(this) : id;
//        if((startNode.getOsm_Id() == 560149987l && endNode.getOsm_Id() ==  560149995l) || (startNode.getOsm_Id() == 560149995l && endNode.getOsm_Id() == 560149987l)){
//            System.out.println("node from "+ startNode.getOsm_Id() + " to "+endNode.getOsm_Id()+", :"+ this.id);
//        }
        this.name = name;
        this.highwayType = highwayType;
        this.distance = distance;
        this.weight = weight;
    }

    public OEdge(OMapWay way, ONode startNode, ONode endNode) {
        this(null, startNode, endNode,0.0,0.0, way.getName(), way.getTags().get("highway"));//TODO fix weight calculation
    }

    public OEdge(String id, Long startNodeId, Long endNodeID, Double weight, Double distance, String name, String highwayType) {
        this(id, OGraph.getInstance().getNode(startNodeId), OGraph.getInstance().getNode(endNodeID), weight, distance, name, highwayType);
    }

    /**
     * GETTERS
     */
    public String getId() { return id; }

    public String getName() {
        return this.name;
    }

    public boolean isDirected() {
        return this.directed;
    }

    public String getHighwayType() {
        return this.highwayType;
    }

    public ONode getStartNode() {
        return this.startNode;
    }

    public ONode getEndNode() {
        return this.endNode;
    }

    public Double getDistance() {
        return this.distance;
    }

    public Double getWeight() {
        return this.weight == null ? 0 : this.weight;
    }

    public ONode getOtherEnd(String nodeId){
        if(nodeId.equals(endNode.getId()))
            return startNode;
        else if(nodeId.equals(startNode.getId()))
            return endNode;
        return null;
    }


    /**
     * SETTERS
     */
//    public void setWeight(double weight) {
//        this.weight = weight;
//    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHighwayType(String highwayType) {
        this.highwayType = highwayType;
    }

    /**
     * calculate distance between two geo points
     * using Haversine Method and store it in distance
     * attribute of the object
     */
    public Double getLength() {
        return GraphAlgo.distance(startNode, endNode);
    }

    public double getSpeedLimit() {
        return speedLimit.get(this.highwayType);
    }

    public double getTime() {
        return this.distance / this.getSpeedLimit();
    }

    public boolean isOpposite(OEdge other){
        return this.getStartNode().getOsm_Id() == other.getEndNode().getOsm_Id() && this.getEndNode().getOsm_Id() == other.getStartNode().getOsm_Id();
    }


}
