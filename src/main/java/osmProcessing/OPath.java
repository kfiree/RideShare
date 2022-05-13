package osmProcessing;

import java.util.ArrayList;

public class OPath implements Comparable<OPath>{
    private ArrayList<OEdge>  edges = new ArrayList<>();
    private ONode Start, End;

        public OPath(ArrayList<OEdge> edges, ONode start, ONode end) {
        this.edges = edges;
        Start = start;
        End = end;
    }

    public OPath(ArrayList<OEdge> edges) {
        this.edges = edges;
        Start = edges.get(0).getStartNode();
        End = edges.get(edges.size()-1).getEndNode();
    }

    public ArrayList<OEdge> getEdges() {
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
