import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;
import osmProcessing.OEdge;
import osmProcessing.OGraph;
import osmProcessing.ONode;
import scala.Int;

import java.util.ArrayList;
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
//        viewer.getDefaultView().enable;
    }

    private Edge addEdgeToMap(OEdge e){
        Node start = addNodeToMap(e.getStartNode());
        Node end = addNodeToMap(e.getEndNode());

        return displayGraph.addEdge(e.getEdgeId().toString(), start, end);

    }

    private Node addNodeToMap(ONode node){
        String keyStr = String.valueOf(node.getKey());
        Node displayNode = displayGraph.getNode(keyStr);

        if(displayNode == null){
            displayNode = displayGraph.addNode(keyStr);
            displayNode.setAttribute("xy", node.getLongitude(), node.getLatitude());
            displayNode.addAttribute("ui.label", node.getID().toString());
        }
        if(node.getKey() == 224){
            ArrayList<ONode> adjacentNodes = node.getAdjacentNodes();
            int a = 1;

            //255
        }
        return displayNode;
    }
    public static void main(String args[]) {
        Graph graph = new MultiGraph("Tutorial 1");
        graph.setStrict(false);
        graph.setAutoCreate( true );

        graph.addNode("A").setAttribute("xy", 1, 1);
        graph.addNode("B").setAttribute("xy", 1, 10);
        graph.addNode("C").setAttribute("xy", 10, 1);
        graph.addNode("D").setAttribute("xy", 10, 10);


        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");
        graph.addEdge("DA", "D", "A");
        graph.addEdge("DB", "D", "B");
//        graph.addEdge("DC", "D", "C");

        Viewer viewer = graph.display();
        viewer.disableAutoLayout();
    }

}