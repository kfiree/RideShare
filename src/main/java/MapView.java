import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import osmProcessing.OEdge;
import osmProcessing.OGraph;
import osmProcessing.ONode;

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

        Graph graph = new MultiGraph("map simulation");

        for(OEdge e: edgesList){
            ONode endNode = e.getEndNode();
            ONode startNode = e.getStartNode();
//            graph.addEdge();
            graph.addNode(endNode.getID().toString());
            graph.addNode(startNode.getID().toString());


//            graph.add
        }

    }
}