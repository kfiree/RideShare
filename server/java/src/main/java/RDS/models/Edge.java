package RDS.models;
import osmProcessing.ONode;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Edge {
//    private int edge_Id;
    private ONode startNode, endNode;
    private String key;
    private double startNodeId , endNodeId , weight,  distance;
    private String name, highwayType;

    public Edge(double startNodeId, double endNodeId, double weight, double distance, String name, String highwayType) {
        this.key = UUID.randomUUID().toString();
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
        this.weight = weight;
        this.distance = distance;
        this.name = name;
        this.highwayType = highwayType;
    }



    public String getEdge_Id() {
        return key;
    }

    public double getStartNodeId() {
        return startNodeId;
    }

    public void setStartNodeId(double startNodeId) {
        this.startNodeId = startNodeId;
    }

    public double getEndNodeId() {
        return endNodeId;
    }

    public void setEndNodeId(double endNodeId) {
        this.endNodeId = endNodeId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHighwayType() {
        return highwayType;
    }

    public void setHighwayType(String highwayType) {
        this.highwayType = highwayType;
    }

    @Override
    public String toString() {
        return "edges{" +
//                "edge_Id=" + edge_Id +
                ", startNodeId=" + startNodeId +
                ", endNodeId=" + endNodeId +
                ", weight=" + weight +
                ", distance=" + distance +
                ", name='" + name + '\'' +
                ", highwayType='" + highwayType + '\'' +
                '}';
    }
}
