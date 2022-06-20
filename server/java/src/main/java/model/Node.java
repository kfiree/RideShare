package model;

import controller.utils.MapUtils;
import controller.utils.GraphAlgo;
import org.jetbrains.annotations.NotNull;
import controller.osm_processing.OsmObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 *      |==================================|
 *      |=============| NODE  |============|
 *      |==================================|
 *
 *
 * @author  Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 1.0
 * @since   2021-06-20
 */
public class Node implements Comparable<Node> {

    private final String id;
    private final Long osmID;
    private Double latitude, longitude;
    private final List<Edge> edges = new LinkedList<>();
    public enum userType {Driver, Rider, None}
    private userType user;
    private Map<String, String> tags = new HashMap<>();
    private double f;

    public Node(String id, Long osmID, GeoLocation coordinates, userType user) {
        this.id = id.equals("") ? MapUtils.generateId(this) : id;
        this.osmID = osmID;
        latitude = coordinates.getLatitude();
        longitude = coordinates.getLongitude(); //todo save as geolocation
        this.user = user;
    }

    public Node(@NotNull OsmObject object) {
        this("", object.getID(), object.getCoordinates(), userType.None);
        tags = object.getTags();
    }


    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    // GETTERS:
    public String getId() {return id;}

    public Long getOsmID() {
        return osmID;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    //instead of getWeight
    public Integer getDegree() { return edges.size(); }

    public ArrayList<Node> getAdjacentNodesFromGraph() {
        ArrayList<Node> adjacentNodes = new ArrayList<>();
        for(Edge edge : edges) {
            adjacentNodes.add(edge.getEndNode());
        }
        return adjacentNodes;
    }

    public ArrayList<Node> getAdjacentNodes() {
        ArrayList<Node> adjacentNodes = new ArrayList<>();
        for(Edge edge : edges) {
            adjacentNodes.add(edge.getOtherEnd(getId()));
        }
        return adjacentNodes;
    }

//    public ArrayList<OEdge> getIncidentEdges() {
//        return new ArrayList<>(edges);
//    }

    public Map<String, String> getTags() {
        return tags;
    }

    public List<Edge> getEdges() {
        return edges;
    }


    public void addEdge(@NotNull Edge edge) {

        edges.add(edge);
    }

    public void addTags(Map<String, String> tags) {
        if(tags.containsKey("maxspeed")) {
            this.tags.put("maxspeed", tags.get("maxspeed"));
        }
        this.tags.put("oneway", tags.getOrDefault("oneway", "no"));
    }

    public void addTags(String k, String v) {
        tags.put(k, v);
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isAdjacent(Node targetNode) {
        for (Edge e : edges) {
            if (e.getStartNode().getOsmID().equals(targetNode.getOsmID()) ||
                    e.getEndNode().getOsmID().equals(targetNode.getOsmID())) {
                return true;
            }
        }
        return false;
    }

    public Edge getEdgeTo(Node targetNode) {
        for (Edge e : edges) {
            if (e.getStartNode().getOsmID().equals(targetNode.getOsmID()) ||
                    e.getEndNode().getOsmID().equals(targetNode.getOsmID())) {
                return e;
            }
        }
        return null;
    }

//    public boolean removeEdge(OEdge edge){
//        return edges.remove(edge);
//    }

    public userType getUser() {
        return user;
    }

    public void setUser(userType user) {
        addTags("user", "driver");
        this.user = user;
    }

    public void removeEdgeTo(Node other){
        Edge edge = getEdgeTo(other);
        if(edge!=null){
            edges.remove(edge);
        }
    }
//    public boolean stringContainsItemFromList(String inputStr) {
//        return Arrays.stream(relevantTags).anyMatch(inputStr::contains);
//    }

    @Override
    public int compareTo(Node other) {
        if(other.getF() < f){
            return 1;
        }
        else if(other.getF() > f){
            return -1;
        }
        else{
            return Integer.compare(other.getId().compareTo(id), 0);
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Node node){
            return node.getLatitude().equals(latitude) && node.getLongitude().equals(longitude);
        }
        return false;
    }

    @Override
    public String toString() {
        String idStr = "id = " + osmID;
        String coordinatesStr = ", coordinates = (" +latitude + "," + longitude + ")";
        String adjacentStr = edges.stream()
                .map(edge ->edge.getOtherEnd(getId()).getOsmID().toString())
                .collect(Collectors.joining(", "));

        adjacentStr = ", adjacent = (" + adjacentStr +")";
        String tagsStr = tags.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(", "));

        tagsStr = ", tags = (" + tagsStr +")";

        return "Node{" + idStr + coordinatesStr + adjacentStr + tagsStr +"}\n";
    }
}
