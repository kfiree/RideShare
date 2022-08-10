package app.model.graph;

import app.controller.osm_processing.OsmObject;
import app.controller.osm_processing.OsmWay;
import app.model.utils.Coordinates;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *      |==================================|
 *      |==========| Road MAP  |=========|
 *      |==================================|
 *
 *
 *      graph representing map of a region
 *
 * @author  Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 1.0
 * @since   2021-06-20
 */
public class RoadMap {

    private final Set<Edge> edges;
    private final Map<Long, Node> nodes;
    protected static AtomicInteger keyGenerator = new AtomicInteger(-1);

    /** CONSTRUCTORS */
    private RoadMap(){
        edges = new HashSet<>();
        nodes = new HashMap<>();
    }

    /**  Singleton specific properties */
    public static final RoadMap INSTANCE = new RoadMap();



    /* GETTERS */

    public Node getNode(long key){ return nodes.get(key); }

    public Collection<Node> getNodes() { return nodes.values();}

    public Set<Edge> getEdges() { return edges; }

    public int edgesSize(){ return nodes.size(); }

    public int nodesSize(){ return edges.size(); }



    /*     SETTERS     */

    public void setEdges(Collection<Edge> edges) { this.edges.addAll(edges); }

    /** add edge from Parser */
    public void addEdge(Node src, Node dst, OsmWay way){
        Edge edge = setEdgeIfExists(src, dst);

        if(edge == null){
            edge = new Edge(way, src, dst);
            src.addEdge(edge);
            edges.add(edge);
        }

    }

    /** add edge from DB */
    public void addEdge(Long id, Long startNodeId, Long endNodeID, long weight, String highwayType) {
        Node src = getNode(startNodeId), dst = getNode(endNodeID);

        Edge edge = setEdgeIfExists(src, dst);

        if(edge == null){
            edge = new Edge(id, src, dst, weight, highwayType);
            src.addEdge(edge);
            edges.add(edge);
        }

    }

    private Edge setEdgeIfExists(Node src, Node dst){
        boolean srcToDst = src.isAdjacent(dst);
        boolean dstToSrc = dst.isAdjacent(src);

        Edge edge = null;

        if (srcToDst || dstToSrc) {
            if(!srcToDst){ // opposite edge exist
                edge = dst.getEdgeTo(src);
                src.addEdge(edge);
            }else{
                edge = src.getEdgeTo(dst);
            }
        }

        return edge;
    }

    /** add edge from Parser */
    public Node addNode(OsmObject osmObject){
        Node node = getNode(osmObject.getID());

        if( node == null){
            node = new Node(osmObject);
            nodes.put(osmObject.getID(), node);
        }

        return node;
    }

    /** add edge from DB */
    public void addNode(Long osmID, Double latitude, Double longitude){
        Node node = getNode(osmID);

        if( node == null){
            node = new Node(osmID, new Coordinates(latitude, longitude));
            nodes.put(node.getId(), node);
        }

    }



    /* REMOVE FROM GRAPH */

    public Node removeNode(long id){ //TODO check this
        Node node = this.nodes.remove(id);

        node.getEdges().forEach(edge ->{
            Node otherEnd = edge.getOtherEnd(node.getId());
            otherEnd.removeEdgeTo(node);
            edges.remove(edge);
        });

        return nodes.remove(id);
    }

    public void removeNodes(List<Node> nodesToRemove){
        List<Edge> edgesToRemove = new ArrayList<>();

        for(Node node:nodesToRemove) {
            nodes.remove(node.getId());
            for (Edge edge : node.getEdges()) {
                edge.getOtherEnd(node.getId()).removeEdgeTo(node);
                edgesToRemove.add(edge);
            }
        }

        edgesToRemove.forEach(edges::remove);

        /*
            TODO:
                1) check if removed for dest
                2) for faster performance I've replaced
                    "edges.removeAll(edgesToRemove);"
                    with "edgesToRemove.forEach(edges::remove);"
                    check if working.
        */

    }

    public boolean removeEdge(Edge edge){
        edge.getNode2().removeEdge(edge);
        edge.getNode1().removeEdge(edge);
        return edges.remove(edge);
    }



    @Override
    public String toString() {
        return "RoadMap{" +
                "edges=" + edges.size() +
                ", nodes=" + nodes.size() +
                '}';
    }
}