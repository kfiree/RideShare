package app.model.graph;

import app.model.utils.RoadMapHandler;
import app.model.utils.Coordinates;
import app.model.utils.Located;
import org.jetbrains.annotations.NotNull;
import app.controller.osm_processing.OsmObject;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 *      |==================================|
 *      |=============| NODE  |============|
 *      |==================================|
 *
 *
 * @author  Kfir Ettinger
 * @version 1.0
 * @since   2021-06-20
 */
public class Node implements Comparable<Node>, Located {
    private final Long id;
    private final Coordinates coordinates;
    private final CopyOnWriteArrayList<Edge> edges;
    private double f;


    public Node(long id, Coordinates coordinates) {
        this.coordinates = coordinates;
        edges = new CopyOnWriteArrayList<>();
        this.id = id;
    }


    public Node(@NotNull OsmObject object) {
        this(object.getID(), object.getCoordinates());
    }


    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    /* GETTERS */
//    @Override
    public Long getId() {
        return id;
    }

    public Double getLatitude() {
        return coordinates.getLatitude();
    }

    public Double getLongitude() {
        return coordinates.getLongitude();
    }

    @Override
    public Coordinates getCoordinates() { return coordinates; }

    @Override
    public boolean inBound() {
        return RoadMapHandler.inBound(coordinates);
    }


    public ArrayList<Node> getAdjacentNodes() {
        ArrayList<Node> adjacentNodes = new ArrayList<>();
        for(Edge edge : edges) {
            adjacentNodes.add(edge.getOtherEnd(getId()));
        }
        return adjacentNodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addEdge(@NotNull Edge edge) { edges.add(edge); }

    public void move(double longitude, double latitude){
        coordinates.setCoordinates(longitude, latitude);
    }

    public boolean isAdjacent(Node targetNode) {
        for (Edge e : edges) {
            if (e.getNode1().getId().equals(targetNode.getId()) ||
                    e.getNode2().getId().equals(targetNode.getId())) {
                return true;
            }
        }
        return false;
    }

    public Edge getEdgeTo(Node targetNode) {
        for (Edge e : edges) {
            if (e.getNode1().getId().equals(targetNode.getId()) ||
                    e.getNode2().getId().equals(targetNode.getId())) {
                return e;
            }
        }
        return null;
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
            return Integer.compare(other.getId().compareTo(this.id), 0);
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
        String idStr = "id = " + id;
        String coordinatesStr = ", "+ coordinates;
        String adjacentStr = edges.stream()
                .map(edge ->edge.getOtherEnd(getId()).getId().toString())
                .collect(Collectors.joining(", "));

        adjacentStr = ", adjacent = (" + adjacentStr +")";
//        String tagsStr = tags.entrySet().stream()
//                .map(entry -> entry.getKey() + ":" + entry.getValue())
//                .collect(Collectors.joining(", "));

//        tagsStr = ", tags = (" + tagsStr +")";

        return "Node{" + idStr + coordinatesStr + adjacentStr +"}";
    }
}
