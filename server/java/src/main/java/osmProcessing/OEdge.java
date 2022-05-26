package osmProcessing;

import java.util.UUID;

public class OEdge {

    /**
     * PROPERTIES:
     */
    private ONode startNode;
    private ONode endNode;

    private Double distance;
    private Double weight;

    private String key;

    // The name of the street:
    private String name;

    // Highway type of the Edge:
    private String highwayType;

    /** read from the map constructor: */
    public OEdge(OMapWay way, ONode start, ONode target) {
        this.key = UUID.randomUUID().toString();
        this.startNode = start;
        this.endNode = target;
        this.name = way.getName();
        this.highwayType = way.getTags().get("highway");
    }


    /**
     * GETTERS
     */

    public Double getDistance() {
        return this.distance;
    }

    public String getEdge_Id() { return key; }

    public Double getWeight() {
        return this.weight == null ? 0 : this.weight;
    }

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

    public String getStartNodeId() { return this.startNode.getKey(); }

    public String getEndNodeId() { return this.endNode.getKey(); }

//    /**
//     * @param
//     * @return
//     */
//    public Long getEdgeId() { return (long)(this.getStartNode().getOsmID()+this.getEndNode().getOsmID());
//    }

    /**
     * SETTERS
     */
    public void setWeight(double weight) {
        this.weight = weight;
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
    public void calculateDistance() {

        double lat1 = this.getStartNode().getLatitude();
        double lat2 = this.getEndNode().getLatitude();
        double lon1 = this.getStartNode().getLongitude();
        double lon2 = this.getEndNode().getLongitude();

        this.distance = GraphUtils.getInstance().distance(lat1, lon1, lat2, lon2);

    }

    public boolean isOpposite(OEdge other){
        return this.getStartNode().getOsmID() == other.getEndNode().getOsmID() && this.getEndNode().getOsmID() == other.getStartNode().getOsmID();
    }

}
