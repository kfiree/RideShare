package model;

import controller.utils.GraphAlgo;
import controller.utils.MapUtils;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *      |==================================|
 *      |=============| PATH  |============|
 *      |==================================|
 *
 *   path on the map contains list of 'EDGE'
 *
 * Note: this class has a natural ordering that is inconsistent with equals.
 * @author Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 1.0
 * @since 2021-06-20
 */
public class Path implements Comparable<Path> , Iterable<Edge>{
    private final List<Edge>  edges = new ArrayList<>();
    private Node _src, _dest;

    public Path(List objects) {

        MapUtils.validate(!objects.isEmpty(),"Path constructor. Nodes can not be empty.");

        if (objects.get(0) instanceof Edge) {
            objects.forEach(o->{

                MapUtils.validate(o instanceof Edge,"node in edges list.");

                edges.add((Edge)o);

                _src = edges.get(0).getNode1();
                _dest = edges.get(edges.size() - 1).getNode2();
            });
        }else if (objects.get(0) instanceof Node) {

            Node edgeSrc, EdgeDest;

            for (int i = 0; i<objects.size()-1; i++){

                MapUtils.validate(objects.get(i+1) instanceof Node, "edge in nodes list.");

                edgeSrc = (Node) objects.get(i);
                EdgeDest = (Node) objects.get(i+1);
                edges.add(edgeSrc.getEdgeTo(EdgeDest));
            }
            _src = (Node) objects.get(0);
            _dest = (Node) objects.get(objects.size()-1);
        }else{
            MapUtils.throwException("Path constructor accepts only List<Node> or List<Edges>.");
        }
    }

    public List<Edge> getEdges() {
        return edges;
    }//TODO ADD sync method getEdgeIterator()

    public void setEdges(ArrayList<Edge> edges) {
        this.edges.addAll(edges);
    }

    public Node get_src() { return _src; }


    //todo improve naive solution
    public void addMiddlePath(Path passengerPath, Edge currEdge, Node currNode){

//        for (int i = edgeIndex; i < edges.size(); i++) {
//            currEdge = edges.get(i);
//            Node node = currEdge.getOtherEnd(currNode.getId());
//
//            double distance = GraphAlgo.distance(node.getCoordinates(), path.get_src().getCoordinates());
//
//            if( distance< minDistance){
//                edgeIndex = i;
//                minDistance = distance;
//            }
//        }

        // currnode 8674210510
        Path pathToPassenger = GraphAlgo.getShortestPath(currNode, passengerPath.get_src()),
                pathFromPassenger = GraphAlgo.getShortestPath(passengerPath.get_dest(), _dest);

        edges.clear();

        edges.addAll(pathToPassenger.getEdges());

        edges.addAll(passengerPath.getEdges());

        edges.addAll(pathFromPassenger.getEdges());


    }

//    public void set_src(Node _src) { this._src = _src; }
//    TODO run A* After every change of dest or src (might delete this method)

    public Node get_dest() { return _dest; }

//    public void set_dest(Node _dest) { this._dest = _dest; }

    /**
     *  todo get in constructor
     *
     *  in ms
     *
     */
    public double pathTime(){
        double pathLen = 0;
        for (Edge e: edges) {
            pathLen += e.getWeight();
        }
        return pathLen;
    }

    public int getSize(){ return edges.size(); }

    @Override
    public int compareTo(Path other) {
        return (int)(pathTime() - other.pathTime());
    }

    @NotNull
    @Override
    public Iterator iterator() {
        return edges.iterator();
    }

}
