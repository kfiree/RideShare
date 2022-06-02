package model;

import controller.GraphUtils;
import controller.osmProcessing.OMapWay;

public class OEdge {
    private static int keyGen = 0;
    public int key;
    /**
     * PROPERTIES:
     */
    private ONode startNode, endNode;

    private Double distance, weight; //TODO clean redundant field

    private String _id, name, highwayType;

    private OEdge(String id, ONode startNode, ONode endNode,Double weight, Double distance, String name, String highwayType){
//        List<Long> longs = jsonManager.readList("removed.json");
//        if(longs.contains(id)){
//            GraphUtils.nodeEdges.put()
//        }
        if((endNode.getOsm_Id() == 560149995l && startNode.getOsm_Id() == 560149987l) && (endNode.getOsm_Id() == 560149987l && startNode.getOsm_Id() == 560149995l)){
            int stop = 1;
        }

        key = ++keyGen;

        this.startNode = startNode;
        this.endNode = endNode;
        this._id = id == null ? GraphUtils.generateId(this) : id;
        this.name = name;
        this.highwayType = highwayType;
        this.distance = distance;
        this.weight = weight;
    }

    public OEdge(String id, OMapWay way, ONode startNode, ONode endNode) {
        this(id, startNode, endNode,0.0,0.0, way.getName(), way.getTags().get("highway"));//TODO fix weight calculation
    }

    public OEdge(OMapWay way, ONode startNode, ONode endNode) {
        this(null, startNode, endNode,0.0,0.0, way.getName(), way.getTags().get("highway"));//TODO fix weight calculation
    }

    public OEdge(ONode startNode, ONode endNode, Double weight, Double distance, String id, String name, String highwayType) {
        this(id, startNode, endNode,distance, weight, name, highwayType);
    }
    public OEdge(String id, Long startNodeId, Long endNodeID, Double weight, Double distance, String name, String highwayType) {
        this(id, OGraph.getInstance().getNode(startNodeId), OGraph.getInstance().getNode(endNodeID), weight, distance, name, highwayType);
    }
    public OEdge(Long startNodeId, Long endNodeID, Double weight, Double distance, String name, String highwayType) {
        this(OGraph.getInstance().getNode(startNodeId),OGraph.getInstance().getNode(endNodeID), weight, distance, null, name, highwayType);
    }


    /**
     * GETTERS
     */
    public String getEdge_Id() { return _id; }

    public ONode getOtherEnd(long nodeId){
        return nodeId == endNode.getOsm_Id() ? startNode : endNode;
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

        return GraphUtils.distance(lat1, lon1, lat2, lon2);
    }


    public boolean isOpposite(OEdge other){
        return this.getStartNode().getOsm_Id() == other.getEndNode().getOsm_Id() && this.getEndNode().getOsm_Id() == other.getStartNode().getOsm_Id();
    }

    @Override
    public String toString() {
        String nameStr = name == null? "":", name='" + name + '\'';
        return "OEdge{" +
                "startNode=" + startNode.getOsm_Id() +
                ", endNode=" + endNode.getOsm_Id() +
                ", weight=" + weight +
                ", id='" + _id + '\'' +
                nameStr+
                ", Type='" + highwayType + '\'' +
                '}';
    }
}
