package Gui;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
//import org.graphstream.ui.view.util.DefaultMouseManager;

import osmProcessing.GraphUtils;
import osmProcessing.OEdge;
import osmProcessing.OGraph;
import osmProcessing.ONode;

import java.awt.event.MouseListener;
import java.util.Collection;

/**
 * "fill-mode" ...
 *     "fill-color" ...
 *     "fill-image" ...
 *     "stroke-mode" ...
 *     "stroke-color" ...
 *     "stroke-width" ...
 *     "shadow-mode" ...
 *     "shadow-color" ...
 *     "shadow-width" ...
 *     "shadow-offset" ...
 *     "text-mode" ...
 *     "text-color" ...
 *     "text-style" ...
 *     "text-font" ...
 *     "text-size" ...
 *     "text-visibility-mode" ...
 *     "text-visibility" ...
 *     "text-background-mode" ...
 *     "text-background-color" ...
 *     "text-offset" ...
 *     "text-padding" ...
 *     "icon-mode" ...
 *     "icon" ...
 *     "padding" ...
 *     "z-index" ...
 *     "visibility-mode" ...
 *     "visibility" ...
 *     "shape" ...
 *     "size" ...
 *     "size-mode" ...
 *     "shape-points" ...
 *     "text-alignment" ...
 *     "jcomponent" ...
 *     "arrow-image" ...
 *     "arrow-size" ...
 *     "arrow-shape" ...
 *     "sprite-orientation" ...
 *     "canvas-color" ...
 */

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
        // draw graph components
        GraphUtils utils = GraphUtils.getInstance();

        drawEdge(graph);
        drawRider(utils);
        drawPaths(utils);

        displayGraph.setAttribute("ui.stylesheet", styleSheet);
        displayGraph.setAttribute("ui.quality");
        displayGraph.setAttribute("ui.antialias");

        Viewer viewer = displayGraph.display();
        viewer.disableAutoLayout();
        viewer.getDefaultView();
        viewer.getDefaultView().setMouseManager(new CustomMouseManager());
    }

    private Boolean drawEdge(OGraph graph){
        graph.getEdges().values().forEach(e -> {
            Node start = drawNode(e.getStartNode());
            Node end = drawNode(e.getEndNode());
            Edge edge = displayGraph.addEdge(e.getEdge_Id(), start, end);

            edge.setAttribute("ui.style", "blue");
        });
        return true;
    }


    private Node drawNode(ONode node){

        String keyStr = String.valueOf(node.getOsmID());
        Node displayNode = displayGraph.getNode(keyStr);

        if(displayNode == null){

            displayNode = displayGraph.addNode(keyStr);
            displayNode.setAttribute("xy", node.getLongitude(), node.getLatitude());
            displayNode.setAttribute("ui.label", node.getOsmID().toString());

            if(!node.getTags().containsKey("highway")){
                displayNode.setAttribute("ui.style", "size: 1px;fill-color: black;");
            }
        }

        return displayNode;
    }

    private boolean drawRider(GraphUtils utils){
        utils.getRiders().values().forEach(rider->{
            String keyStr = String.valueOf(rider.getOsmID());
            Node displayNode = displayGraph.getNode(keyStr);

            if(displayNode == null){

                displayNode = displayGraph.addNode(keyStr);
                displayNode.setAttribute("xy", rider.getLongitude(), rider.getLatitude());
                displayNode.setAttribute("ui.label", rider.getOsmID().toString());

                displayNode.setAttribute("ui.style","fill-color: red;");
            }

        });
        return true;
    }

    private boolean drawPaths(GraphUtils utils){
        utils.getPaths().forEach(path -> {
            if(path!=null)
                path.getEdges().forEach(edge -> {
                    displayGraph.getEdge(edge.getEdge_Id()).setAttribute("ui.style", "size: 5px; fill-color: blue;");
                });
        });
        return true;
    }
}