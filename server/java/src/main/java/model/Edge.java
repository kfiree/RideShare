package model;

import controller.utils.GraphAlgo;
import controller.osm_processing.OsmWay;
import controller.utils.MapUtils;
import model.interfaces.Located;
import model.interfaces.MapObject;

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
public class Edge implements MapObject, Located {

    private final Node node1, node2;
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

    public Edge(String id, Node node1, Node node2, Double weight, String highwayType){
//        if(startNode.getTags().get("oneway").equals("yes"))
//        directed = startNode.getTags().get("oneway") != null && startNode.getTags().get("oneway").equals("yes");
        tagNames.add(highwayType);//TODO make highway type enum
        this.node1 = node1;
        this.node2 = node2;
        this.id = id == null ? MapUtils.generateId(this) : id;
        this.highwayType = highwayType;
        this.weight = weight == 0.0 ? GraphAlgo.distance(node1, node2)/ SPEED_LIMIT.getOrDefault(highwayType, 50) : weight;
    }

    public Edge(OsmWay way, Node node1, Node node2) {
        this(null, node1, node2,0.0, way.getTags().get("highway"));
    }

    public Edge(String id, Long startNodeId, Long endNodeID, Double weight, String highwayType) {
        this(id, RoadMap.getInstance().getNode(startNodeId), RoadMap.getInstance().getNode(endNodeID), weight, highwayType);
    }

    /**
     * GETTERS
     */
    @Override
    public String getId() { return id; }

    public String getHighwayType() {
        return highwayType;
    }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }

    public Double getDistance() {
        return distance;
    }

    public Double getWeight() {
        return weight;
    }

    public Node getOtherEnd(String nodeId){
        if(nodeId.equals(node2.getId())) {
            return node1;
        } else if(nodeId.equals(node1.getId())) {
            return node2;
        }
        return null;
    }

    @Override
    public GeoLocation getCoordinates(){
        return node1.getCoordinates();
    }

    @Override
    public boolean inBound() {

        return node1.inBound() && node2.inBound();
    }

    /**
     * calculate distance between two geo points
     * using Haversine Method and store it in distance
     * attribute of the object
     */
    public Double getLength() {
        if(distance == 0) {
            distance = GraphAlgo.distance(node1, node2);
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
        return getNode1().getOsmID().equals(other.getNode2().getOsmID()) && getNode2().getOsmID().equals(other.getNode1().getOsmID());
    }


}
