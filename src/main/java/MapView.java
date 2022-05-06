import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;
import osmProcessing.OEdge;
import osmProcessing.OGraph;
import osmProcessing.ONode;
import scala.Int;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MapView {
    OGraph graph;
    Graph displayGraph;

    public MapView(OGraph graph) {
        this.graph = graph;
        this.displayGraph = new MultiGraph("map simulation");
    }

    public void demoGraph(){
        Graph graph = new MultiGraph("Tutorial 1");
        graph.setStrict(false);
        graph.setAutoCreate( true );
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");

        graph.display();
    }

    public void show(){
        List<ONode> nodesList = graph.getNodesList();

        for(ONode n : nodesList){
            addNodeToMap(n);
        }

        List<OEdge> edgesList = graph.getEdgesList();

        for(OEdge e: edgesList){
            addEdgeToMap(e);
        }

        Viewer viewer = displayGraph.display();
        viewer.disableAutoLayout();
    }

    private Edge addEdgeToMap(OEdge e){
        Node start = addNodeToMap(e.getStartNode());
        Node end = addNodeToMap(e.getEndNode());

        return displayGraph.addEdge(e.getEdgeId().toString(), start, end);

    }

    private Node addNodeToMap(ONode node){
        Node displayNode = displayGraph.getNode(node.getID().toString());

        if(displayNode == null){
            displayNode = displayGraph.addNode(node.getID().toString());
            displayNode.setAttribute("xy", node.getLongitude(), node.getLatitude());
        }

        return displayNode;
    }
}