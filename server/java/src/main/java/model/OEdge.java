package osmProcessing;

import java.util.Hashtable;
import java.util.UUID;

public class OEdge {

    /**
     * PROPERTIES:
     */
    private ONode startNode, endNode;

    private Double distance, weight, length; //TODO clean redundant field

    private String id = "", name, highwayType;

    private final Hashtable<String, Integer> SPEED_LIMITS =  new Hashtable<>()
    {{
                put("motorway", 110);
                put("trunk", 100);
                put("primary", 90);
                put("secondary", 70);
                put("tertiary", 50);
    }};

    private OEdge(String id, ONode startNode, ONode endNode,Double weight, Double distance, String name, String highwayType){
        this.startNode = startNode;
        this.endNode = endNode;
        this.id = id.equals("") ? UUID.randomUUID().toString() : id;
        this.name = name;
        this.highwayType = highwayType;
        this.distance = distance;
        this.weight = weight;
    }

    public OEdge(OMapWay way, ONode startNode, ONode endNode) {
        this("", startNode, endNode,0.0,0.0, way.getName(), way.getTags().get("highway"));//TODO fix weight calculation
    }

    public OEdge(ONode startNode, ONode endNode, Double weight, Double distance, String id, String name, String highwayType) {
        this(id, startNode, endNode,distance, weight, name, highwayType);
    }
    public OEdge(String id, Long startNodeId, Long endNodeID, Double weight, Double distance, String name, String highwayType) {
        this(id, OGraph.getInstance().getNode(startNodeId), OGraph.getInstance().getNode(endNodeID), weight, distance, name, highwayType);
    }
    public OEdge(Long startNodeId, Long endNodeID, Double weight, Double distance, String name, String highwayType) {
        this(OGraph.getInstance().getNode(startNodeId),OGraph.getInstance().getNode(endNodeID), weight, distance, "", name, highwayType);
    }


    /**
     * GETTERS
     */
    public String getEdge_Id() { return id; }

    public String getName() {
        return this.name;
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



    /**
     * SETTERS
     */
//    public void setWeight(double weight) {
//        this.weight = weight;
//    }

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
    public Double calculateDistance() {

        double lat1 = this.getStartNode().getLatitude();
        double lat2 = this.getEndNode().getLatitude();
        double lon1 = this.getStartNode().getLongitude();
        double lon2 = this.getEndNode().getLongitude();

        return GraphUtils.getInstance().distance(lat1, lon1, lat2, lon2);
    }

    public double getSpeedLimit() {
        return SPEED_LIMITS.get(this.highwayType);
    }

    public double getTime() {
        return this.distance / this.getSpeedLimit();
    }

    public boolean isOpposite(OEdge other){
        return this.getStartNode().getOsm_Id() == other.getEndNode().getOsm_Id() && this.getEndNode().getOsm_Id() == other.getStartNode().getOsm_Id();
    }


}
