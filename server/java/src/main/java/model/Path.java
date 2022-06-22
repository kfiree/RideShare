package model;

import controller.utils.MapUtils;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *      |==================================|
 *      |=============| PATH  |============|
 *      |==================================|
 *
 *   path on the map contains list of 'EDGE'
 *
 * @author  Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 1.0
 * @since   2021-06-20
 */
public class Path implements Comparable<Path>{
    private final List<Edge>  edges = new ArrayList<>();
    private final RoadMap map = RoadMap.getInstance();
    private Node _src, _dest;
    private int curr;

    public Path(List objects) {
        if(objects.isEmpty()){
            MapUtils.throwException("Path constructor. Nodes can not be empty.");
        }else if (objects.get(0) instanceof Edge) {
            objects.forEach(o->{
                if(o instanceof Node) {
                    MapUtils.throwException("node in edges list.");
                }else{
                    edges.add((Edge)o);
                }
                _src = edges.get(0).getStartNode();
                _dest = edges.get(edges.size() - 1).getEndNode();
            });
        }else if (objects.get(0) instanceof Node) {

            Node edgeSrc, EdgeDest;

            for (int i = 0; i<objects.size()-1; i++){
                if(objects.get(i+1) instanceof Node) {
                    MapUtils.throwException("edge in nodes list.");
                }
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
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges.addAll(edges);
    }

    public Node getNext(){
        if(curr>edges.size()){
            return edges.get(curr++).getEndNode();
        }
        return null;
    }

    public Node get_src() {
        return _src;
    }

    public void set_src(Node _src) {
        this._src = _src;
    }

    public Node get_dest() {
        return _dest;
    }

    public void set_dest(Node _dest) {
        this._dest = _dest;
    }

    public double length(){
        double pathLen = 0;
        for (Edge e: edges) {
            pathLen += e.getDistance();
        }
        return pathLen;
    }

    @Override
    public int compareTo(Path other) {
        return (int)(length() - other.length());
    }
}
