package app.model;

import app.controller.GraphAlgo;
import app.controller.osm_processing.OsmWay;
import app.controller.MapUtils;
import app.model.interfaces.Located;
import app.model.interfaces.MapObject;

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
    static Set<String> tagNames = new HashSet<>();
    private final String id, highwayType;

    private static final Hashtable<String, Integer> SPEED_LIMIT =  new Hashtable<>(){{
                put("motorway", 110);
                put("trunk", 100);
                put("primary", 90);
                put("secondary", 70);
                put("tertiary", 50);
    }};
    // TODO set lower speed

    public Edge(String id, Node node1, Node node2, Double weight, String highwayType){
        tagNames.add(highwayType); //TODO make highway type enum ?
        this.node1 = node1;
        this.node2 = node2;
        this.id = id == null ? MapUtils.generateId() : id;
        this.highwayType = highwayType;
        this.weight = weight == 0.0 ? calculateWeight() : weight;
    }

    public Edge(OsmWay way, Node node1, Node node2) {
        this(null, node1, node2,0.0, way.getTags().get("highway"));
    }

    /**
     * time to cross edge in ms.
     *
     * I've added 1 second to each weight because of road conditions
     */
    private double calculateWeight(){
        double weight = node1.distanceTo(node2) / SPEED_LIMIT.getOrDefault(highwayType, 50);
        return GraphAlgo.hourToSeconds(weight)+1;
    }



    /**  GETTERS */

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

    public double getSpeedLimit() { return SPEED_LIMIT.get(highwayType); }

    @Override
    public GeoLocation getCoordinates(){
        return node1.getCoordinates();
    }

    @Override
    public boolean inBound() {

        return node1.inBound() && node2.inBound();
    }
}
