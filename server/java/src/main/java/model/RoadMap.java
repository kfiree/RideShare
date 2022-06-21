package model;

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
public final class RoadMap {

    private Set<Edge> edges;
    private final Map<Long, Node> nodes;
    private final Map<Long, Node> junctionNodes = new HashMap<>();

    /** CONSTRUCTORS */
    private RoadMap() {
        edges = new HashSet<>();
        nodes = new HashMap<>();
    }

    /**  Singleton specific properties */
    private static final RoadMap INSTANCE = new RoadMap();

    public static RoadMap getInstance() {
        return INSTANCE;
    }

    /** GETTERS */
    public Map<Long, Node> getNodes() { return nodes; }

    public Set<Edge> getEdges() { return edges; }

    public Node getNode(long key){
        return this.nodes.get(key);
    }

    public Map<Long, Node> getJunctionNodes() {
        return junctionNodes;
    } //TODO move to somewhere else

    /** SETTERS */

    public void setEdges(Set<Edge> edges) {
        this.edges.addAll(edges);
    }

    public Edge addEdge(Node src, Node dst, OsmWay way){
        boolean srcToDst = src.isAdjacent(dst);
        boolean dstToSrc = dst.isAdjacent(src);

        Edge edge = null;

        if(srcToDst && dstToSrc){
            return src.getEdgeTo(dst);
        }else if(dstToSrc){
            edge = dst.getEdgeTo(src);
        }else if(!srcToDst){
            edge = new Edge(way, src, dst);
        }

        src.addEdge(edge);
        edges.add(edge);

        return edge;
    }

    public Edge addEdge(String id, Long startNodeId, Long endNodeID, Double weight, String highwayType) {
        Edge edge = new Edge(id, startNodeId, endNodeID, weight, highwayType);
        addEdge(edge);

        return edge;
    }

    private void addEdge(Edge e){
        if(!e.getStartNode().isAdjacent(e.getEndNode())) {
            e.getStartNode().addEdge(e);
            e.getEndNode().addEdge(e);
//            e.getLength();
            edges.add(e);
        }else{
//            e.setDirected(true);
            //TODO set directed
        }
    }

    public Node addNode(Node node, long id){//TODO make private
        nodes.put(id, node);
        return node;
    }

    public Node addNode(Node node){
        nodes.put(node.getOsmID(), node);
        return node;
    }

    public Node addNode(String id, Long osmID, Double latitude, Double longitude, Node.userType user){
        return addNode(new Node(id, osmID, new GeoLocation(latitude, longitude), Node.userType.None));
    }

    public Node removeNode(long id){
        Node node = this.nodes.remove(id);

        node.getEdges().forEach(edge ->{
            Node otherEnd = edge.getOtherEnd(node.getId());
            otherEnd.removeEdgeTo(node);
            edges.remove(edge);
        });

        return nodes.remove(id);
    }

    public void removeNodes(List<Node> nodes){

        List<Edge> edgesToRemove = new ArrayList<>();
        int a = 0;
        for(Node node:nodes) {
            this.nodes.remove(node.getOsmID());
            if(node.getOsmID() == 560149987l || node.getOsmID() == 560149995l){
                int stop = 0;
            }
            for (Edge edge : node.getEdges()) {
//                System.out.println(a++);
//                ONode otherEnd = edge.getOtherEnd(node.getId());
//                otherEnd.removeEdge(edge);//removeEd(node);
                edgesToRemove.add(edge);
            }
        }


        edges.removeAll(edgesToRemove);
//        this.nodes.removeAll(nodes);
    }


//    public void removeNodes(List<Long> nodeKeys){
//
//        for(int i = 0; i < nodeKeys.size(); i++){
//            Long id = nodeKeys.get(i);
//
//            ONode removed = this.nodes.remove(id);
//            ArrayList<OEdge> oEdgesCopy = new ArrayList<>(removed.getEdges());
//            for (OEdge e:oEdgesCopy) {
//                removeEdge(e);
//            }
//        }
//    }

    public boolean removeEdge(Edge e){//TODO check call method on remove
        e.getEndNode().removeEdgeTo(e.getStartNode());
        e.getStartNode().removeEdgeTo(e.getEndNode());
        return this.edges.remove(e);
    }

    public void removeEdge(Edge e, Iterator<Edge> itr){
        e.getEndNode().removeEdgeTo(e.getStartNode());
        e.getStartNode().removeEdgeTo(e.getEndNode());
        itr.remove();
    }

    @Override
    public String toString() {
        return "OMap{" +
                "edges=" + this.edges.size() +
                ", nodes=" + this.nodes.size() +
                '}';
    }
}