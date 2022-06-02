package model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OPath implements Comparable<OPath>{
    private List<OEdge>  edges = new ArrayList<>();
    private OGraph graph = OGraph.getInstance();
    private ONode Start, End;

    public OPath(ArrayList<OEdge> edges, ONode start, ONode end) {
        this.edges = edges;
        Start = start;
        End = end;
        graph = OGraph.getInstance();
    }

    public OPath(@NotNull List<Object> objects) {
        if(objects.isEmpty())
            return;
        if (objects.get(0) instanceof OEdge) {
            this.edges = objects.stream().map(object -> (OEdge)object).collect(Collectors.toList());
            Start = edges.get(0).getStartNode();
            End = edges.get(edges.size() - 1).getEndNode();
        }else if(objects.get(0) instanceof Long){
            List<Long> pathNodesID = objects.stream().map(object -> (Long) object).collect(Collectors.toList());

            List<ONode> pathNodes = new ArrayList<>();
            ONode src, dest;
            for (int i = 0; i<pathNodesID.size()-1;i++){
                src = graph.getNode(pathNodesID.get(i));
                dest = graph.getNode(pathNodesID.get(i+1));

                if(dest != null && src != null){
                    OEdge edge = src.getEdgeBetween(dest);
                    if(edge!=null){
                        this.edges.add(edge);
                    }
                }
            }

        }
    }


    public List<OEdge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<OEdge> edges) {
        this.edges = edges;
    }

    public ONode getStart() {
        return Start;
    }

    public void setStart(ONode start) {
        Start = start;
    }

    public ONode getEnd() {
        return End;
    }

    public void setEnd(ONode end) {
        End = end;
    }

    public double length(){
        double pathLen = 0;
        for (OEdge e: edges) {
            pathLen += e.getDistance();
        }
        return pathLen;
    }

    @Override
    public int compareTo(OPath other) {
        return (int)(this.length() - other.length());
    }
}
