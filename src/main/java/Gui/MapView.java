package Gui;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;
//import org.graphstream.ui.view.util.DefaultMouseManager;

import osmProcessing.OEdge;
import osmProcessing.OGraph;
import osmProcessing.ONode;

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

        for(ONode n : graph.getNodes().values()){
            drawNode(n);
        }

        for(OEdge e: graph.getEdges().values()){
            drawEdge(e);
        }

        for(ONode rider: graph.getRiders().values()){
            drawRider(rider);
        }


        Viewer viewer = displayGraph.display();


        displayGraph.setAttribute("ui.stylesheet", styleSheet);
        displayGraph.setAttribute("ui.quality");
        displayGraph.setAttribute("ui.antialias");
        viewer.disableAutoLayout();
//        viewer.getDefaultView().setMouseManager(new CustomMouseManager2());
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
            displayNode.setAttribute("ui.label", node.getID().toString());

            if(!node.getTags().containsKey("highway")){
                displayNode.setAttribute("ui.style", "size: 1px;fill-color: black;");
            }
        }

        return displayNode;
    }

    private Node drawRider(ONode node){
        String keyStr = String.valueOf(node.getKey());
        Node displayNode = displayGraph.getNode(keyStr);

        if(displayNode == null){

            displayNode = displayGraph.addNode(keyStr);
            displayNode.setAttribute("xy", node.getLongitude(), node.getLatitude());
            displayNode.setAttribute("ui.label", node.getID().toString());

            displayNode.setAttribute("ui.style","fill-color: red;");

//            String image = "url('data/assets/Thumbs_up_icon.png')";
//            displayNode.setAttribute("ui.style", "fill-mode: image-scaled; fill-image: "+ image);

        }

        return displayNode;
    }
}