package osmProcessing;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;

public class ONode implements Node {
    /**
     * PROPERTIES:
     */
    private LinkedList<OEdge> edges = new LinkedList<OEdge>();

    private Double latitude;
    private Double longitude;
    private Long id;
    private Map<String, String> tags;

    //degree = weight
    private Integer degree;

    public ONode(MapObject object) {
        this.id = object.getID();
        this.latitude = object.getLatitude();
        this.longitude = object.getLongitude();
        this.tags = object.getTags();
        if(!tags.isEmpty()){
            int a = 1;
        }
    }

    // GETTERS:

    public Long getID() {
        return this.id;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    @Override
    public Graph getGraph() {
        return null;
    }

    //instead of getWeight
    public int getDegree() {
        return this.degree == null ? 0 : this.degree;
    }

    @Override
    public int getOutDegree() {
        return 0;
    }

    @Override
    public int getInDegree() {
        return 0;
    }

    @Override
    public boolean hasEdgeToward(String s) {
        return false;
    }

    @Override
    public boolean hasEdgeFrom(String s) {
        return false;
    }

    @Override
    public boolean hasEdgeBetween(String s) {
        return false;
    }

    @Override
    public <T extends Edge> T getEdgeToward(String s) {
        return null;
    }

    @Override
    public <T extends Edge> T getEdgeFrom(String s) {
        return null;
    }

    @Override
    public <T extends Edge> T getEdgeBetween(String s) {
        return null;
    }

    @Override
    public <T extends Edge> Iterator<T> getEdgeIterator() {
        return null;
    }

    @Override
    public <T extends Edge> Iterator<T> getEnteringEdgeIterator() {
        return null;
    }

    @Override
    public <T extends Edge> Iterator<T> getLeavingEdgeIterator() {
        return null;
    }

    @Override
    public <T extends Node> Iterator<T> getNeighborNodeIterator() {
        return null;
    }

    @Override
    public <T extends Edge> T getEdge(int i) {
        return null;
    }

    @Override
    public <T extends Edge> T getEnteringEdge(int i) {
        return null;
    }

    @Override
    public <T extends Edge> T getLeavingEdge(int i) {
        return null;
    }

    @Override
    public <T extends Node> Iterator<T> getBreadthFirstIterator() {
        return null;
    }

    @Override
    public <T extends Node> Iterator<T> getBreadthFirstIterator(boolean b) {
        return null;
    }

    @Override
    public <T extends Node> Iterator<T> getDepthFirstIterator() {
        return null;
    }

    @Override
    public <T extends Node> Iterator<T> getDepthFirstIterator(boolean b) {
        return null;
    }

    @Override
    public <T extends Edge> Iterable<T> getEachEdge() {
        return null;
    }

    @Override
    public <T extends Edge> Iterable<T> getEachLeavingEdge() {
        return null;
    }

    @Override
    public <T extends Edge> Iterable<T> getEachEnteringEdge() {
        return null;
    }

    @Override
    public <T extends Edge> Collection<T> getEdgeSet() {
        return null;
    }

    @Override
    public <T extends Edge> Collection<T> getLeavingEdgeSet() {
        return null;
    }

    @Override
    public <T extends Edge> Collection<T> getEnteringEdgeSet() {
        return null;
    }

    public ArrayList<ONode> getAdjacentNodes() {
        ArrayList<ONode> adjacentNodes = new ArrayList<ONode>();
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
        String tagsStr = "";
        if(!tags.isEmpty()) {
            tagsStr = ", tags: ";
        }
        return "fd{" +
                "edges=" + edges +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", id=" + id +
                ", degree=" + degree +
                '}';
    }

    @Override
    public boolean hasEdgeToward(Node node) {
        return false;
    }

    @Override
    public boolean hasEdgeToward(int i) throws IndexOutOfBoundsException {
        return false;
    }

    @Override
    public boolean hasEdgeFrom(Node node) {
        return false;
    }

    @Override
    public boolean hasEdgeFrom(int i) throws IndexOutOfBoundsException {
        return false;
    }

    @Override
    public boolean hasEdgeBetween(Node node) {
        return false;
    }

    @Override
    public boolean hasEdgeBetween(int i) throws IndexOutOfBoundsException {
        return false;
    }

    @Override
    public <T extends Edge> T getEdgeToward(Node node) {
        return null;
    }

    @Override
    public <T extends Edge> T getEdgeToward(int i) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public <T extends Edge> T getEdgeFrom(Node node) {
        return null;
    }

    @Override
    public <T extends Edge> T getEdgeFrom(int i) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public <T extends Edge> T getEdgeBetween(Node node) {
        return null;
    }

    @Override
    public <T extends Edge> T getEdgeBetween(int i) throws IndexOutOfBoundsException {
        return null;
    }

    public int compareTo(ONode node) {
        return this.degree.compareTo(node.getDegree());
    }

    @Override
    public Iterator<Edge> iterator() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public int getIndex() {
        return 0;
    }

    @Override
    public <T> T getAttribute(String s) {
        return null;
    }

    @Override
    public <T> T getFirstAttributeOf(String... strings) {
        return null;
    }

    @Override
    public <T> T getAttribute(String s, Class<T> aClass) {
        return null;
    }

    @Override
    public <T> T getFirstAttributeOf(Class<T> aClass, String... strings) {
        return null;
    }

    @Override
    public CharSequence getLabel(String s) {
        return null;
    }

    @Override
    public double getNumber(String s) {
        return 0;
    }

    @Override
    public ArrayList<? extends Number> getVector(String s) {
        return null;
    }

    @Override
    public Object[] getArray(String s) {
        return new Object[0];
    }

    @Override
    public HashMap<?, ?> getHash(String s) {
        return null;
    }

    @Override
    public boolean hasAttribute(String s) {
        return false;
    }

    @Override
    public boolean hasAttribute(String s, Class<?> aClass) {
        return false;
    }

    @Override
    public boolean hasLabel(String s) {
        return false;
    }

    @Override
    public boolean hasNumber(String s) {
        return false;
    }

    @Override
    public boolean hasVector(String s) {
        return false;
    }

    @Override
    public boolean hasArray(String s) {
        return false;
    }

    @Override
    public boolean hasHash(String s) {
        return false;
    }

    @Override
    public Iterator<String> getAttributeKeyIterator() {
        return null;
    }

    @Override
    public Iterable<String> getEachAttributeKey() {
        return null;
    }

    @Override
    public Collection<String> getAttributeKeySet() {
        return null;
    }

    @Override
    public void clearAttributes() {

    }

    @Override
    public void addAttribute(String s, Object... objects) {

    }

    @Override
    public void changeAttribute(String s, Object... objects) {

    }

    @Override
    public void setAttribute(String s, Object... objects) {

    }

    @Override
    public void addAttributes(Map<String, Object> map) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public int getAttributeCount() {
        return 0;
    }
}
