package model;

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
    private List<Edge>  edges = new ArrayList<>();
    private RegionMap map = RegionMap.getInstance();
    private Node Start, End;
    private int curr;


    public Path(ArrayList<Edge> edges, Node start, Node end) {
        this.edges = edges;
        Start = start;
        End = end;
        map = RegionMap.getInstance();
    }

    public Path(@NotNull List<Object> objects) {
        if(objects.isEmpty())
            return;
        if (objects.get(0) instanceof Edge) {
            this.edges = objects.stream().map(object -> (Edge)object).collect(Collectors.toList());
            Start = edges.get(0).getStartNode();
            End = edges.get(edges.size() - 1).getEndNode();
        }else if(objects.get(0) instanceof Long){
            List<Long> pathNodesID = objects.stream().map(object -> (Long) object).collect(Collectors.toList());

            List<Node> pathNodes = new ArrayList<>();
            Node src, dest;
            for (int i = 0; i<pathNodesID.size()-1;i++){
                src = map.getNode(pathNodesID.get(i));
                dest = map.getNode(pathNodesID.get(i+1));

                if(dest != null && src != null){
                    Edge edge = src.getEdgeTo(dest);
                    if(edge!=null){
                        this.edges.add(edge);
                    }
                }
            }

        }
    }


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

    public Node getStart() {
        return Start;
    }

    public void setStart(Node start) {
        Start = start;
    }

    public Node getEnd() {
        return End;
    }

    public void setEnd(Node end) {
        End = end;
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
