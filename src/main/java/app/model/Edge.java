package app.model;

import app.controller.GraphAlgo;
import app.controller.osm_processing.OsmWay;
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
    private final long weight;
    private final String highwayType;
    private final long id;

    static private final Hashtable<String, Integer> SPEED_LIMIT;
    static{
        SPEED_LIMIT =  new Hashtable<>(){{
            put("motorway", 110);
            put("trunk", 100);
            put("primary", 90);
            put("secondary", 70);
            put("tertiary", 50);
        }};
    }
    // TODO set lower speed

    /** add edge from DB */
    public Edge(long id, Node node1, Node node2, Long weight, String highwayType){
        this.node1 = node1;
        this.node2 = node2;
        this.id = id;
        this.highwayType = highwayType;
        this.weight = weight;
    }

    /** add edge from Parser */
    public Edge(OsmWay way, Node node1, Node node2) {
        this.node1 = node1;
        this.node2 = node2;
        this.id = RoadMap.keyGenerator.incrementAndGet();
        this.highwayType = way.getTags().get("highway");
        this.weight = calculateWeight();
    }

    /**
     * time to cross edge in ms.
     *
     * I've added 1 second to each weight because of road conditions
     *
     * todo add time relative to highway type and distance (and time of day?)
     */
    private long calculateWeight(){
        long weight = (long) (node1.distanceTo(node2) / SPEED_LIMIT.getOrDefault(highwayType, 50));
        return GraphAlgo.hourToSeconds(weight)+1;
    }



    /*  GETTERS */

//    @Override
    public long getId() { return id; }

    public String getHighwayType() {
        return highwayType;
    }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }

    public long getWeight() {
        return weight;
    }

    public Node getOtherEnd(long nodeId){
        if(nodeId == node2.getId()){
            return node1;
        } else if(nodeId == node1.getId()){
            return node2;
        }
        return null;
    }

    public double getSpeedLimit() { return SPEED_LIMIT.get(highwayType); }

    @Override
    public GeoLocation getLocation(){
        return node1.getLocation();
    }

    @Override
    public boolean inBound() {
        return node1.inBound() && node2.inBound();
    }
}
