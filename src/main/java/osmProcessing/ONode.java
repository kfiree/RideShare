package osmProcessing;

import org.apache.commons.math3.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ONode {
    /**
     * PROPERTIES:
     */
    private LinkedList<OEdge> edges = new LinkedList<OEdge>();
    static int keyGenerator = 0;
    private int key;
    private Double latitude;
    private Double longitude;
    private Long osmID;
    private Map<String, String> tags;
    private ArrayList<Long> waysID = new ArrayList<>();
    public static enum userType {Driver, Rider, None};
    private userType user = userType.None;

    //degree = weight
    private Integer degree;

    public ONode(@NotNull MapObject object) {
        this.key = keyGenerator++;
        this.osmID = object.getID();
        this.latitude = object.getLatitude();
        this.longitude = object.getLongitude();
        this.tags = object.getTags();
        if(!tags.isEmpty()){
            int a = 1;
        }
    }

    public ONode(long id, Double @NotNull [] coordinates, userType user) {
        this.latitude = coordinates[0];
        this.longitude = coordinates[1];
        this.osmID = id;
        this.user = user;
        this.tags = new HashMap<>();
    }

    // GETTERS:

    public int getKey() {return key;}

    public Long getID() {
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
    public Integer getDegree() {
        return this.degree == null ? 0 : this.degree;
    }

    public ArrayList<ONode> getAdjacentNodes() {
        ArrayList<ONode> adjacentNodes = new ArrayList<>();
        for(OEdge edge : edges) {
            adjacentNodes.add(edge.getEndNode());
        }
        return adjacentNodes;
    }

    public ArrayList<OEdge> getIncidentEdges() {
        return new ArrayList<OEdge>(edges);
    }

    // SETTERS:
    public void addEdge(OEdge edge) {
        this.edges.add(edge);
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public void addTags(Map<String, String> tags) {
        this.tags.putAll(tags);
    }

    public void addTags(String k, String v) {
        this.tags.put(k, v);
    }

    public LinkedList<OEdge> getEdges() {
        return edges;
    }

    public Long getOsmID() {
        return osmID;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public boolean isAdjacent(ONode targetNode) {
        for (OEdge e : this.edges) {
            if (e.getStartNode().getID() == targetNode.getID() ||
                    e.getEndNode().getID() == targetNode.getID()) {
                return true;
            }
        }
        return false;
    }

    public OEdge getAdjacent(ONode targetNode) {
        for (OEdge e : this.edges) {
            if (e.getStartNode().getID() == targetNode.getID() ||
                    e.getEndNode().getID() == targetNode.getID()) {
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

    @Override
    public String toString() {

        String idStr = "id = " + osmID;
        String coordinatesStr = ", coordinates = (" +latitude + "," + longitude + ")";
        String adjacentStr = edges.stream().map(list -> list.getEndNode().getID().toString()).collect(Collectors.joining(", "));
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

    public int compareTo(ONode node) {
        return this.degree.compareTo(node.getDegree());
    }
}
