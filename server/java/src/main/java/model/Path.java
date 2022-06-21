package model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
    private List<Edge>  edges = new ArrayList<>();
    private RoadMap map = RoadMap.getInstance();
    private Node _src, _dest;
    private int curr;


//    public Path(ArrayList<Edge> edges, Node start, Node end) {
//        this.edges = edges;
//        Start = start;
//        End = end;
//        map = RegionMap.getInstance();
//    }


    public Path(@NotNull List<Node>  nodes) {
        if(nodes.isEmpty()){
            try {
                throw new Exception("Path constructor. Nodes can not be empty.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        _src = nodes.get(0);
        _dest = nodes.get(nodes.size()-1);

        Node edgeSrc, EdgeDest;

        for (int i = 0; i<nodes.size()-1; i++){
            edgeSrc = nodes.get(i);
            EdgeDest = nodes.get(i+1);
            edges.add(edgeSrc.getEdgeTo(EdgeDest));
        }
    }

//    public Path(@NotNull List<Object> objects) { TODO fix that sh*t
//        if(objects.isEmpty())
//            return;
//        if (objects.get(0) instanceof Edge) {
//            edges = objects.stream().map(object -> (Edge)object).collect(Collectors.toList());
//            _src = edges.get(0).getStartNode();
//            _dest = edges.get(edges.size() - 1).getEndNode();
//        }else if(objects.get(0) instanceof Long){
//            List<Long> pathNodesID = objects.stream().map(object -> (Long) object).collect(Collectors.toList());
//
//            List<Node> pathNodes = new ArrayList<>();
//            Node src, dest;
//            for (int i = 0; i<pathNodesID.size()-1;i++){
//                src = map.getNode(pathNodesID.get(i));
//                dest = map.getNode(pathNodesID.get(i+1));
//
//                if(dest != null && src != null){
//                    Edge edge = src.getEdgeTo(dest);
//                    if(edge!=null){
//                        this.edges.add(edge);
//                    }
//                }
//            }
//
//        }
//    }


    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
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
        return (int)(this.length() - other.length());
    }
}
