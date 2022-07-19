package app.model;

import app.controller.GraphAlgo;

import java.util.Iterator;
import java.util.List;

/**
 *      |==================================|
 *      |=============| PATH  |============|
 *      |==================================|
 *
 *   path on the map contains list of 'EDGE'
 *
 * TODO save estimatedDistance from path creation
 *      convert to list of nodes only.
 *
 *
 * Note: this class has a natural ordering that is inconsistent with equals.
 * @author Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 1.0
 * @since 2021-06-20
 */
public class Path implements Comparable<Path> , Iterable<Node>{
    private List<Node>  nodes;
    private double weight; /*  times crossing */

    public Path(List<Node> nodes, double weight){
        this.nodes = nodes;
        this.weight = weight;
    }

    public List<Node> getNodes() {
        return nodes;
    } //TODO ADD sync method getEdgeIterator()


    public Node get_src() { return nodes.get(0); }


    //todo improve naive solution
    public void addMiddlePath(Path path, Node currNode){
        Path toPathSrc = GraphAlgo.getShortestPath(currNode, path.get_src()),
                fromPathDest = GraphAlgo.getShortestPath(path.getDest(), getDest());

        path.getNodes().remove(0);
        fromPathDest.getNodes().remove(0);

        nodes.clear();

        nodes.addAll(toPathSrc.getNodes());

        nodes.addAll(path.getNodes());

        nodes.addAll(fromPathDest.getNodes());

        weight = fromPathDest.weight + path.weight + fromPathDest.weight;
    }

//    public void set_src(Node _src) { this._src = _src; }
//    TODO run A* After every change of dest or src (might delete this method)

    public Node getDest() { return nodes.get(nodes.size()-1); }

//    public void set_dest(Node _dest) { this._dest = _dest; }

    /**
     *  todo get in constructor
     *
     *  in ms
     *
     */
    public double getWeight(){
        return this.weight;
    }

    public int getSize(){ return nodes.size(); }

    @Override
    public int compareTo(Path other) {
        return (int)(getWeight() - other.getWeight());
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

}
