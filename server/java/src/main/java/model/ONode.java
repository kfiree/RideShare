package model;

import controller.GraphUtils;
import controller.algorithms.GraphAlgo;
import org.jetbrains.annotations.NotNull;
import controller.osmProcessing.MapObject;

import java.util.*;
import java.util.stream.Collectors;

public class ONode implements Comparable<ONode> {

    private String id;
    private Long osmID;
    private Double latitude, longitude;

    private List<OEdge> edges = new LinkedList<>();
    private Map<String, String> tags = new HashMap<>();
    private ArrayList<Long> waysID = new ArrayList<>();
    static public Set<String> tagNames = new HashSet<>();
    static Map<String, String> tagsMap = new HashMap<>();

    private double H;
    private double G;
    private double F;

    public static enum userType {Driver, Rider, None};
    private userType user = userType.None; //TODO check id used

    public ONode(String id, Long osmID, Double latitude, Double longitude, userType user) {
        this.id = id.equals("") ? GraphUtils.generateId(this) : id;
        this.osmID = osmID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
//        this.edges = edges == null ? new LinkedList<>(): edges;
//        this.tags = tags == null? new HashMap<>(): tags;
//        this.waysID = waysID == null ?new ArrayList<>(): waysID;
    }

    public ONode(@NotNull MapObject object) {
        this("", object.getID(), object.getLatitude(), object.getLongitude(), userType.None);
        this.tags = object.getTags();
    }



// GETTERS:
    public String getId() {return id;}

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

    public ArrayList<ONode> getAdjacentNodesFromGraph() {
        ArrayList<ONode> adjacentNodes = new ArrayList<>();
        for(OEdge edge : edges) {
            adjacentNodes.add(edge.getEndNode());
        }
        return adjacentNodes;
    }

    public ArrayList<ONode> getAdjacentNodes() {
        ArrayList<ONode> adjacentNodes = new ArrayList<>();
        for(OEdge edge : edges) {
//            if(edge.getEndNode().osmID
            adjacentNodes.add(edge.getOtherEnd(this.getId()));
//            adjacentNodes.add(this.osmID != edge.getEndNode().osmID ? edge.getEndNode():edge.getStartNode());
        }
        return adjacentNodes;
    }

//    public ArrayList<OEdge> getIncidentEdges() {
//        return new ArrayList<>(edges);
//    }

    public Map<String, String> getTags() {
        return tags;
    }

    public List<OEdge> getEdges() {
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

    public void setH(ONode other) {
        this. H = GraphAlgo.distance(this, other);
    }

    public void setG(double g) {
        G = g;
    }

    public void setF(double f) {
        F = f;
    }

    public void addEdge(@NotNull OEdge edge) {

        this.edges.add(edge);
    }

    public void addTags(Map<String, String> tags) {
        if(tags.containsKey("maxspeed")) {
            this.tags.put("maxspeed", tags.get("maxspeed"));
//            System.out.println(tags.get("maxspeed") + getOsm_Id());
        }
        if(tags.containsKey("oneway")) {
            this.tags.put("oneway", tags.get("oneway"));
//            System.out.println("oneway = " + tags.get("oneway")+ ", id =  " + getOsm_Id());
        }else{
            this.tags.put("oneway", "no");
        }
//
        tags.forEach((tag,tagValue)->{
            if(tag.equals("one")){

            }
            tagNames.add(tag);
            tagsMap.put(tag,tagValue);
        });
//        this.tags.putAll(tags);
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

static int a = 0;
    public boolean isAdjacent(ONode targetNode) {
        for (OEdge e : this.edges) {
//            System.out.println(e.key);
            if (e == null || e.getEndNode() == null || e.getStartNode() == null){
                int a = 0;
            }else
            if (e.getStartNode().getOsm_Id() == targetNode.getOsm_Id() ||
                    e.getEndNode().getOsm_Id() == targetNode.getOsm_Id()) {
                return true;
            }
        }
        return false;
    }

    public OEdge getEdgeTo(ONode targetNode) {
        for (OEdge e : this.edges) {
            if (e.getStartNode().getOsm_Id() == targetNode.getOsm_Id() ||
                    e.getEndNode().getOsm_Id() == targetNode.getOsm_Id()) {
                return e;
            }
        }
        return null;
    }
    public boolean removeEdge(OEdge edge){
        return edges.remove(edge);
    }
    public userType getUser() {
        return user;
    }

    public void setUser(userType user) {
        this.addTags("user", "driver");
        this.user = user;
    }

    public void removeEdgeTo(ONode other){
        OEdge edge = getEdgeTo(other);
        if(edge!=null){
            edges.remove(edge);
        }
    }

    private final String[] irrelevantTags = {"name","bicycle", "maxspeed:type", "lanes", "maxheight"};

    private boolean relevantTags(String tag){

        for (String s: irrelevantTags) {
            if(tag.contains(s))
                return false;
        }
        return true;
    }
//    public boolean stringContainsItemFromList(String inputStr) {
//        return Arrays.stream(relevantTags).anyMatch(inputStr::contains);
//    }

    @Override
    public int compareTo(ONode node) {
        if(node.getF() < this.F){
            return 1;
        }
        else if(node.getF() > this.F){
            return -1;
        }
        else{
            if(node.getId().compareTo(this.id) < 0){
                return -1;
            }
            else if(node.getId().compareTo(this.id) > 0){
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

    @Override
    public String toString() {
        String idStr = "id = " + osmID;
        String coordinatesStr = ", coordinates = (" +latitude + "," + longitude + ")";
        String adjacentStr = edges.stream()
                .map(edge ->edge.getOtherEnd(this.getId()).getOsm_Id().toString())
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
}
