package model;

import controller.GraphUtils;
import org.jetbrains.annotations.NotNull;
import controller.osmProcessing.MapObject;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

public class ONode implements Comparable<ONode> {

    private String id;
    private Long osmID;
    private Double latitude, longitude;

    private LinkedList<OEdge> edges;
    private Map<String, String> tags;
    private ArrayList<Long> waysID;

    private double H;
    private double G;
    private double F;

    public static enum userType {Driver, Rider, None};
    private userType user = userType.None; //TODO check id used
    private userType user = userType.None;

    private ONode(String id, Long osmID, Double latitude, Double longitude, LinkedList<OEdge> edges, Map<String, String> tags, ArrayList<Long> waysID, userType user) {
        this.id = id.equals("") ? GraphUtils.generateId(this) : id;
        this.osmID = osmID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
        this.edges = edges == null ? new LinkedList<>(): edges;
        this.tags = tags == null? new HashMap<>(): tags;
        this.waysID = waysID == null ?new ArrayList<>(): waysID;
    }

    public ONode(@NotNull MapObject object) {
        this("", object.getID(), object.getLatitude(), object.getLongitude(), null, object.getTags(), null, userType.None);
    }

    public ONode(Long osmID, Double @NotNull [] coordinates, userType user) {
        this("", osmID, coordinates[0], coordinates[1], null, null, null, user);
    }
    public ONode(Long osmID, String nodeID, Double latitude, Double longitude, LinkedList<OEdge> edges,Map<String, String> tags) {
        this(nodeID, osmID, latitude, longitude, edges, tags, null, userType.None);
    }


// GETTERS:
    public String getNode_id() {return id;}

    public Long getOsm_Id() {
        return this.osmID;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public ArrayList<Long> getWaysID() {
        return waysID;
    }

    public void addWayID(long wayID) {
        this.waysID.add(wayID);
    }

    //instead of getWeight
    public Integer getDegree() { return this.edges == null ? 0 :this.edges.size(); }

    public ArrayList<ONode> getAdjacentNodes() {
        ArrayList<ONode> adjacentNodes = new ArrayList<>();
        for(OEdge edge : edges) {
            adjacentNodes.add(edge.getEndNode());
        }
        return adjacentNodes;
    }

//    public ArrayList<OEdge> getIncidentEdges() {
//        return new ArrayList<>(edges);
//    }

    public Map<String, String> getTags() {
        return tags;
    }

    public LinkedList<OEdge> getEdges() {
        return edges;
    }

    public double getH() {
        return H;
    }

    public double getG() {
        return G;
    }

    public double getF() {
        return F;
    }

    // SETTERS:

    public void setH(ONode node) {
        this. H = GraphUtils.getInstance().distance(this.latitude, this.longitude, node.getLatitude(), node.getLongitude());
    }

    public void setG(double g) {
        G = g;
    }

    public void setF(double f) {
        F = f;
    }

    public void addEdge(OEdge edge) {
        this.edges.add(edge);
    }

    public void addTags(Map<String, String> tags) {
        this.tags.putAll(tags);
    }

    public void addTags(String k, String v) {
        this.tags.put(k, v);
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    public boolean isAdjacent(ONode targetNode) {
        for (OEdge e : this.edges) {
            if (e.getStartNode().getOsm_Id() == targetNode.getOsm_Id() ||
                    e.getEndNode().getOsm_Id() == targetNode.getOsm_Id()) {
                return true;
            }
        }
        return false;
    }

    public OEdge getEdgeBetween(ONode targetNode) {
        for (OEdge e : this.edges) {
            if (e.getStartNode().getOsm_Id() == targetNode.getOsm_Id() ||
                    e.getEndNode().getOsm_Id() == targetNode.getOsm_Id()) {
                return e;
            }
        }
        return null;
    }

    public userType getUser() {
        return user;
    }

    public void setUser(userType user) {
        this.addTags("user", "driver");
        this.user = user;
    }

    public void removeEdgeTo(ONode other){
        OEdge edge = getEdgeBetween(other);
        if(edge!=null){
            this.edges.remove(edge);
        }
    }

    private final List<String> irrelevantTags = Arrays.asList("name", "surface", "ref");

    private boolean relevantTags(String tag){
        for (String s: irrelevantTags) {
            if(tag.contains(s))
                return false;
        }
        return true;
    }

//    public int compareTo(ONode node) {
//        return this.getDegree().compareTo(node.getDegree());
//    }

    @Override
    public String toString() {
        String idStr = "id = " + osmID;
        String coordinatesStr = ", coordinates = (" +latitude + "," + longitude + ")";
        String adjacentStr = edges.stream()
                .map(edge ->edge.getOtherEnd(this.osmID).getOsm_Id().toString())
                .collect(Collectors.joining(", "));

        adjacentStr = ", adjacent = (" + adjacentStr +")";
        String tagsStr = tags.entrySet().stream()
                .filter(entry -> relevantTags(entry.getKey()))
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(", "));

        tagsStr = ", tags = (" + tagsStr +")";
        String ways = waysID.stream().map(element -> element.toString()).collect(Collectors.joining(", "));
        ways = "ways = (" + ways +")";

        return "Node{" + idStr + coordinatesStr + adjacentStr + tagsStr +"}\n"+ways;
    }
    private final List<String> irrelevantTags = Arrays.asList("name", "surface", "ref");

    private boolean relevantTags(String tag){
        for (String s: irrelevantTags) {
            if(tag.contains(s))
                return false;
        }
        return true;
    }

    @Override
    public int compareTo(ONode node) {
        if(node.getF() < this.F){
            return 1;
        }
        else if(node.getF() > this.F){
            return -1;
        }
        else{
            if(node.getNode_id().compareTo(this.id) < 0){
                return -1;
            }
            else if(node.getNode_id().compareTo(this.id) > 0){
                return 1;
            }
            else{
                return 0;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ONode){
            ONode node = (ONode) o;
            if(node.getLatitude() == this.latitude && node.getLongitude() == this.longitude){
                return true;
            }
            return false;
        }
        return false;
    }
}
