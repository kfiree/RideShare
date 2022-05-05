import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import osmProcessing.OEdge;
import osmProcessing.OGraph;
import osmProcessing.ONode;
import scala.Int;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MapView {
//    public static void main(String args[]) {
//        Graph graph = new SingleGraph("Tutorial 1");
//        graph.setStrict(false);
//        graph.setAutoCreate( true );
//        graph.addNode("A");
//        graph.addNode("B");
//        graph.addNode("C");
//        graph.addEdge("AB", "A", "B");
//        graph.addEdge("BC", "B", "C");
//        graph.addEdge("CA", "C", "A");
//
//        graph.display();
//    }

    public void show(OGraph g){
        List<ONode> nodesList = g.getNodesList();
        List<OEdge> edgesList = g.getEdgesList();
        HashSet<Long> used = new HashSet<>();
        Graph graph = new MultiGraph("map simulation");
        boolean a = true;
        for(OEdge e: edgesList){
            if(a) {
                ONode endNode = e.getEndNode();
                ONode startNode = e.getStartNode();
                if(used.contains(endNode.getID() )){
                    used.add(endNode.getID());
                    graph.addNode(endNode.getID().toString());
                }
                if(used.contains(startNode.getID() )){
                    used.add(startNode.getID());
                    graph.addNode(startNode.getID().toString());
                }
                graph.addEdge(e.getName(), (Node)startNode, (Node)endNode);
//                graph.addEdge()
                a=false;
            }

//            graph.add
        }
        graph.display();
    }
}