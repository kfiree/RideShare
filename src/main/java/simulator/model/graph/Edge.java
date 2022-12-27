package simulator.model.graph;

import simulator.model.graph.utils.GraphUtils;
import simulator.model.graph.utils.Coordinates;
import simulator.model.graph.utils.Located;

import java.util.Hashtable;

/**
 *      |==================================|
 *      |=============| EDGE  |============|
 *      |==================================|
 *
 *
 * @author  Kfir Ettinger
 * @version 1.0
 * @since   2021-06-20
 */
public class Edge implements Located { // , GraphElement {
    private final Node node1, node2;
    private final long weight;
    private final String highwayType;
    private final long id;

    static private final Hashtable<String, Integer> SPEED_LIMIT;
    static{
        SPEED_LIMIT =  new Hashtable<>(){{
            put("motorway", 70);
            put("trunk", 65);
            put("primary", 55);
            put("secondary", 45);
            put("tertiary", 30);
        }};
    }

//    static{
//        SPEED_LIMIT =  new Hashtable<>(){{
//            put("motorway", 110);
//            put("trunk", 100);
//            put("primary", 90);
//            put("secondary", 70);
//            put("tertiary", 50);
//        }};
//    }
    // TODO set lower speed

    public Edge(long id, Node node1, Node node2, Long weight, String highwayType){
        this.node1 = node1;
        this.node2 = node2;
        this.id = id;
        this.highwayType = highwayType;
        this.weight = weight;
    }

    /**
     * time to cross edge in ms.
     *
     * I've added 1 second to each weight because of road conditions
     *
     * todo add time relative to highway type and distance (and time of day?)
     */
    private long calculateWeight(){
        double timeInHour = (node1.distanceTo(node2) / SPEED_LIMIT.getOrDefault(highwayType, 50));
        return GraphUtils.hourToMS(timeInHour);
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

//    @Override
//    public double distanceTo(User other) {
//        return Located.super.distanceTo(other);//todo choose shortest distance
//    }

    @Override
    public Coordinates getCoordinates() {
        return getNode1().getCoordinates();
    }

    @Override
    public boolean inBound() {
        return node1.inBound() && node2.inBound();
    }

    @Override
    public String toString() {
        return "Edge{" +
                "id=" + id +
                ", node1=" + node1.getId() +
                ", node2=" + node2.getId() +
                ", weight=" + weight +
                ", highwayType='" + highwayType + '\'' +
                '}';
    }
}
