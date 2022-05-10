package osmProcessing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
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

    //degree = weight
    private Integer degree;

    public ONode(MapObject object) {
        this.key = keyGenerator++;
        this.osmID = object.getID();
        this.latitude = object.getLatitude();
        this.longitude = object.getLongitude();
        this.tags = object.getTags();
        if(!tags.isEmpty()){
            int a = 1;
        }
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

    public boolean isConnected(ONode targetNode) {
        for (OEdge e : this.edges) {
            if (e.getStartNode().getID() == targetNode.getID() ||
                    e.getEndNode().getID() == targetNode.getID()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {

        String idStr = "id = " + osmID;
        String coordinatesStr = ", coordinates = (" +latitude + "," + longitude + ")";
        String adjacentStr = edges.stream().map(list -> list.getEndNode().getID().toString()).collect(Collectors.joining(", "));
        adjacentStr = ", adjacent = (" + adjacentStr +")";
        String tagsStr = tags.entrySet().stream().map(list -> list.getKey()+ ":" + list.getValue()).collect(Collectors.joining(", "));
        tagsStr = ", tags = (" + tagsStr +")";

        return "Node{" + idStr + coordinatesStr + adjacentStr + adjacentStr + tagsStr +"}";
    }

    public int compareTo(ONode node) {
        return this.degree.compareTo(node.getDegree());
    }
}
