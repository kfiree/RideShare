package model;

import controller.osm_processing.OsmObject;
import controller.osm_processing.OsmWay;

import java.util.*;

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

    /** CONSTRUCTORS */
    private RoadMap() {
        edges = new HashSet<>();
        nodes = new HashMap<>();
    }

    /**  Singleton specific properties */
    public static final RoadMap INSTANCE = new RoadMap();



    /** GETTERS */

    public Node getNode(long key){ return nodes.get(key); }

    public Collection<Node> getNodes() { return nodes.values();}

    public Set<Edge> getEdges() { return edges; }




    /**     SETTERS     */

    public void setEdges(Collection<Edge> edges) { this.edges.addAll(edges); }

    /** add edge from Parser */
    public Edge addEdge(Node src, Node dst, OsmWay way){
        Edge edge = setEdgeIfExists(src, dst);

        if(edge == null){
            edge = new Edge(way, src, dst);
            src.addEdge(edge);
            edges.add(edge);
        }

        return edge;
    }

    /** add edge from DB */
    public Edge addEdge(String id, Long startNodeId, Long endNodeID, Double weight, String highwayType) {
        Node src = getNode(startNodeId), dst = getNode(endNodeID);

        Edge edge = setEdgeIfExists(src, dst);

        if(edge == null){
            edge = new Edge(id, src, dst, weight, highwayType);
            src.addEdge(edge);
            edges.add(edge);
        }

        return edge;
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
    public Node addNode(String id, Long osmID, Double latitude, Double longitude){
        Node node = getNode(osmID);

        if( node == null){
            node = new Node(id, osmID, new GeoLocation(latitude, longitude));
            nodes.put(node.getOsmID(), node);
        }

        return node;
    }



    /** REMOVE FROM GRAPH */

    public Node removeNode(long id){ //TODO test
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
            nodes.remove(node.getOsmID());
            for (Edge edge : node.getEdges()) {
                edge.getOtherEnd(node.getId()).removeEdgeTo(node);
                edgesToRemove.add(edge);
            }
        }

        edges.removeAll(edgesToRemove);//TODO check if removed for dest
    }

    public boolean removeEdge(Edge edge){ //TODO test
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