package simulator.model.graph;

import simulator.model.graph.utils.EdgeOperation;
import simulator.model.graph.utils.NodeOperation;
import simulator.model.graph.utils.Coordinates;
//import utils.DS.EdgeOperation;
//import utils.DS.NodeOperation;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

//import static utils.logs.LogHandler.LOGGER;

/**
 *      |==================================|
 *      |==========| Road MAP  |=========|
 *      |==================================|
 *
 *
 *      graph representing map of a region
 *
 * @author  Kfir Ettinger
 * @version 1.0
 * @since   2021-06-20
 */
public class RoadMap {
    private final ReentrantLock edgeLock, nodeLock;
    private String name = "";
    private final Set<Edge> edges;
    private final Map<Long, Node> nodes;
    protected static AtomicInteger keyGenerator = new AtomicInteger(-1);

    /** CONSTRUCTORS */
    private RoadMap(){
        edges = new HashSet<>();
        nodes = new HashMap<>();
        edgeLock = new ReentrantLock();
        nodeLock = new ReentrantLock();
    }

    /**  Singleton specific properties */
    public static final RoadMap INSTANCE = new RoadMap();



    /* GETTERS */

    public String getName() {
        return name;
    }

    public Node getNode(long key){ return nodes.get(key); }

    public Collection<Node> getNodes(){
        return Collections.unmodifiableCollection(nodes.values());
    }

    public void nodesOperation(NodeOperation op){
        nodes.values().forEach(op::operate);
    }
    public void edgesOperation(EdgeOperation op){
        edgeLock.lock();
        edges.forEach(op::operate);
        edgeLock.unlock();
    }
//    private Collection<Edge> getEdges() {
//        return Collections.unmodifiableCollection(edges);
//    }

    public int edgesSize(){ return nodes.size(); }

    public int nodesSize(){ return edges.size(); }



    /*     SETTERS     */

    public void setName(String name)  {
        this.name = name;
    }

    public void setEdges(Collection<Edge> edges) { this.edges.addAll(edges); }

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

    /** add edge from DB */
    public void addNode(Long osmID, Double latitude, Double longitude){
        Node node = getNode(osmID);

        if( node == null){
            node = new Node(osmID, new Coordinates(latitude, longitude));
            nodes.put(node.getId(), node);
        }

    }



    /* REMOVE FROM GRAPH */

    public Node removeNode(long id){ //TODO uncheck
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
            for (Edge edge : node.getEdges()) {
                edge.getOtherEnd(node.getId()).removeEdgeTo(node);
                edgesToRemove.add(edge);
            }
        }

        nodes.entrySet().removeIf(e->nodesToRemove.contains(e.getValue()));



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

    public void removeAllNodesBut(List<Node> nodesToKeep){
        List<Node> nodesToRemove = nodes.values().stream()
                .filter(node -> !nodesToKeep.contains(node))
                .collect(Collectors.toList());

//        LogHandler.LOGGER.info(nodesToRemove.size() + " nodes that are not part of main component are found and being removed.");

        removeNodes(nodesToRemove);
    }

    public boolean removeEdge(Edge edge){
        edge.getNode2().removeEdge(edge);
        edge.getNode1().removeEdge(edge);
        return edges.remove(edge);
    }



    public static void unlockEdges(){}
    public static void unlockNode(){}
    @Override
    public String toString() {
        return "RoadMap{" +
                "edges=" + edges.size() +
                ", nodes=" + nodes.size() +
                '}';
    }
}