package model;

import controller.utils.GraphAlgo;
import controller.utils.MapUtils;
import model.interfaces.Located;
import model.interfaces.MapObject;
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
public class Node implements Comparable<Node>, MapObject , Located {

    private final String id;
    private Long osmID;
    private final GeoLocation coordinates;
    private final Set<Edge> edges;
    private double f;


    public Node(String id, GeoLocation coordinates) {
        this.id = id;
        this.coordinates = coordinates;
        edges = new HashSet<>();
    }

    public Node(String id, Long osmID, GeoLocation coordinates) {
        this(id, coordinates);
        this.osmID = osmID;
    }

    public Node(@NotNull OsmObject object) {
        this(MapUtils.generateId(), object.getID(), object.getCoordinates());
    }


    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    // GETTERS:
    @Override
    public String getId() {return id;}

    public Long getOsmID() {
        return osmID;
    }

    public Double getLatitude() {
        return coordinates.getLatitude();
    }

    public Double getLongitude() {
        return coordinates.getLongitude();
    }

    @Override
    public GeoLocation getCoordinates() { return coordinates; }

    @Override
    public boolean inBound() {
        return MapUtils.inBound(coordinates);
    }


    public ArrayList<Node> getAdjacentNodes() {
        ArrayList<Node> adjacentNodes = new ArrayList<>();
        for(Edge edge : edges) {
            adjacentNodes.add(edge.getOtherEnd(getId()));
        }
        return adjacentNodes;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public void addEdge(@NotNull Edge edge) { edges.add(edge); }

    public void move(double longitude, double latitude){
        coordinates.setCoordinates(longitude, latitude);
    }

    public boolean isAdjacent(Node targetNode) {
        for (Edge e : edges) {
            if (e.getNode1().getOsmID().equals(targetNode.getOsmID()) ||
                    e.getNode2().getOsmID().equals(targetNode.getOsmID())) {
                return true;
            }
        }
        return false;
    }

    public Edge getEdgeTo(Node targetNode) {
        for (Edge e : edges) {
            if (e.getNode1().getOsmID().equals(targetNode.getOsmID()) ||
                    e.getNode2().getOsmID().equals(targetNode.getOsmID())) {
                return e;
            }
        }
        return null;
    }

    public double distanceTo(Node other){
        return GraphAlgo.distance(getCoordinates(), other.getCoordinates());
    }

    public void removeEdgeTo(Node other){
        Edge edge = getEdgeTo(other);
        if(edge!=null){
            edges.remove(edge);
        }
    }

    public void removeEdge(Edge edge){
        edges.remove(edge);
    }

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
            return node.getLatitude().equals(coordinates.getLatitude()) && node.getLongitude().equals(coordinates.getLongitude());
        }
        return false;
    }

    @Override
    public String toString() {
        String idStr = "id = " + osmID;
        String coordinatesStr = ", "+ coordinates;
        String adjacentStr = edges.stream()
                .map(edge ->edge.getOtherEnd(getId()).getOsmID().toString())
                .collect(Collectors.joining(", "));

        adjacentStr = ", adjacent = (" + adjacentStr +")";
//        String tagsStr = tags.entrySet().stream()
//                .map(entry -> entry.getKey() + ":" + entry.getValue())
//                .collect(Collectors.joining(", "));

//        tagsStr = ", tags = (" + tagsStr +")";

        return "Node{" + idStr + coordinatesStr + adjacentStr +"}\n";
    }
}
