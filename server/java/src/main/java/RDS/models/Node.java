package RDS.models;

import org.json.simple.JSONObject;

import java.util.UUID;

public class Node {

    private Long osm_Id;
    private String node_id;
    private double latitude, longitude, degree;
    private JSONObject edges, tags;

    public Node(Long id, Double latitude, Double longitude, Integer degree, JSONObject edges, JSONObject tags) {
        this.osm_Id = id;
        this.node_id = UUID.randomUUID().toString();

        this.latitude = latitude;
        this.longitude = longitude;
        this.degree = degree;
        this.edges = edges;
        this.tags = tags;
    }


//    public Node(double latitude, double longitude, double degree, JSONObject edges, JSONObject tags) {
//        this.node_Id = ThreadLocalRandom.current().nextLong(1000000, Integer.MAX_VALUE);;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.degree = degree;
//        this.edges = edges;
//        this.tags = tags;
//    }
//
//    public Node(JSONObject edges) {
//        this.node_Id = ThreadLocalRandom.current().nextLong(1000000, Integer.MAX_VALUE);;
//        this.latitude = 23.2222;
//        this.longitude = 23.2222;
//        this.degree = 23.2222;
//        this.edges = edges;
//
//        JSONObject json = new JSONObject();
//        json.put("name", "foo");
//        this.tags = json;
//    }
//
//    public Node(Long node_Id, double latitude, double longitude, double degree, JSONObject edges, JSONObject tags) {
//        this.node_Id = node_Id;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.degree = degree;
//        this.edges = edges;
//        this.tags = tags;
//    }
//    public Node() {
//        this.node_Id = ThreadLocalRandom.current().nextLong(1000000, Integer.MAX_VALUE);;
//        this.latitude = 3513212.20211231233123123187812313130;
//        this.longitude = 3123325.201231231231231131312321218780;
//        this.degree = 35.20218780;
//
//        JSONObject json = new JSONObject();
//        json.put("name", "foo");
//        this.edges = json;
//        this.tags = json;
//    }


    public String getNode_id() {
        return node_id;
    }



    public Long getOsm_Id() {
        return osm_Id;
    }

    public void setOsm_Id(Long osm_Id) {
        this.osm_Id = osm_Id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDegree() {
        return degree;
    }

    public void setDegree(double degree) {
        this.degree = degree;
    }

    public JSONObject getEdges() {
        return edges;
    }

    public void setEdges(JSONObject edges) {
        this.edges = edges;
    }

    public JSONObject getTags() {
        return tags;
    }

    public void setTags(JSONObject tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "nodes{" +
                "node_Id=" + osm_Id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", degree=" + degree +
                ", edges=" + edges +
                ", tags=" + tags +
                '}';
    }
}
