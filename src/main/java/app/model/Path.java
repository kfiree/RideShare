package app.model;

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

    public Path(List<Path> pathParts){
        this.nodes = new ArrayList<>();
        this.nodes.add(pathParts.get(0).nodes.get(0));

        for(Path part: pathParts){
            part.getNodes().remove(0);
            this.nodes.addAll(part.getNodes());
        }
    }

    public Path(List<Node> nodes, double weight){
        this.nodes = nodes;
        this.weight = weight;
    }

    public List<Node> getNodes() {
        //TODO ADD sync method getEdgeIterator()
        return nodes;
    }

    public Node getSrc() { return nodes.get(0); }

    public Node getDest() {
        Node node;
        try {
            node = nodes.get(nodes.size() - 1);
            return node;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getWeight(){
        //todo get in constructor
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

    @Override
    public String toString() {
        return "Path{" +
                "length=" + nodes.size() +
                ", weight=" + weight +
                ", src = " + getSrc().getId() +
                ", dst=" + getDest().getId() +
                '}';
    }
}
