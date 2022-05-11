package Gui;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.util.DefaultMouseManager;
import osmProcessing.OEdge;
import osmProcessing.OGraph;
import osmProcessing.ONode;

import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class MapView {
    private OGraph graph;
    private Graph displayGraph;
    private String styleSheet = "node {	text-mode: hidden; }";

    private static MapView INSTANCE = new MapView();

    private MapView() {
        this.displayGraph = new MultiGraph("map simulation");
        this.graph = OGraph.getInstance();
    }

    public static MapView getInstance() {
        return INSTANCE;
    }

    public void run(){
        List<ONode> nodesList = graph.getNodesList();

        for(ONode n : nodesList){
            drawNode(n);
        }

        List<OEdge> edgesList = graph.getEdgesList();

        for(OEdge e: edgesList){
            drawEdge(e);
        }

//         node.setAttribute("ui.style", "size: 100px;");

        Viewer viewer = displayGraph.display();
        displayGraph.setAttribute("ui.stylesheet", styleSheet);
        displayGraph.setAttribute("ui.quality");
        displayGraph.setAttribute("ui.antialias");
        viewer.disableAutoLayout();
        viewer.getDefaultView().setMouseManager(new CustomMouseManager2());
    }

    private Edge drawEdge(OEdge e){
        Node start = drawNode(e.getStartNode());
        Node end = drawNode(e.getEndNode());

        return displayGraph.addEdge(e.getEdgeId().toString(), start, end);
    }

    private Node drawNode(ONode node){
        String keyStr = String.valueOf(node.getKey());
        Node displayNode = displayGraph.getNode(keyStr);

        if(displayNode == null){

            displayNode = displayGraph.addNode(keyStr);
            displayNode.setAttribute("xy", node.getLongitude(), node.getLatitude());
            displayNode.addAttribute("ui.label", node.getID().toString());

            if(!node.getTags().containsKey("highway")){
                displayNode.setAttribute("ui.style", "size: 1px;");
            }
        }

        return displayNode;
    }
}