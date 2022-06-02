package View;

import controller.RDS.utils.utils;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;
//import org.graphstream.ui.view.util.DefaultMouseManager;

import controller.GraphUtils;
import model.OGraph;
import model.ONode;

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

        drawEdge(graph);
//        drawRider(utils);
        drawPaths();

        displayGraph.setAttribute("ui.stylesheet", styleSheet);
        displayGraph.setAttribute("ui.quality");
        displayGraph.setAttribute("ui.antialias");

        Viewer viewer = displayGraph.display();
        viewer.disableAutoLayout();
        viewer.getDefaultView();
        viewer.getDefaultView().setMouseManager(new CustomMouseManager());
    }

    private Boolean drawEdge(OGraph graph){
        graph.getEdges().forEach(e -> {

            Node start = drawNode(e.getStartNode());
            Node end = drawNode(e.getEndNode());
            displayGraph.addEdge(e.getEdge_Id(), start, end);
        });
        return true;
    }


    private Node drawNode(ONode node){

        String keyStr = String.valueOf(node.getOsm_Id());
        Node displayNode = displayGraph.getNode(keyStr);

        if(displayNode == null){

            displayNode = displayGraph.addNode(keyStr);
            displayNode.setAttribute("xy", node.getLongitude(), node.getLatitude());
            displayNode.setAttribute("ui.label", node.getOsm_Id().toString());

            displayNode.setAttribute("ui.style", "z-index: 1; size: 3px;");

            if(node.getOsm_Id() == 2432701015l){
                displayNode.setAttribute("ui.style", "z-index: 2; size: 10px; fill-color: green;");
            } else if(node.getUser() == ONode.userType.Rider){
                displayNode.setAttribute("ui.style", "fill-color: blue;");
            } else if(node.getUser() == ONode.userType.Driver) {
                displayNode.setAttribute("ui.style", "fill-color: red;");
            }


        }

        return displayNode;
    }

    private boolean drawRider(GraphUtils utils){
        utils.getRiders().values().forEach(rider->{
            String keyStr = String.valueOf(rider.getOsm_Id());
            Node displayNode = displayGraph.getNode(keyStr);

            if(displayNode == null){

                displayNode = displayGraph.addNode(keyStr);
                displayNode.setAttribute("xy", rider.getLongitude(), rider.getLatitude());
                displayNode.setAttribute("ui.label", rider.getOsm_Id().toString());

                displayNode.setAttribute("ui.style","fill-color: red;");
            }

        });
        return true;
    }

    private boolean drawPaths() {
//        utils.getPaths().forEach(path -> {
//            if(path!=null)
//                path.getEdges().forEach(edge -> {
//                    displayGraph.getEdge(edge.getEdge_Id()).setAttribute("ui.style", "size: 5px; fill-color: blue;");
//                });
//        });

        GraphUtils.getLabeledPaths().keySet().forEach(path -> {
            if (path != null) {
                if (GraphUtils.getLabeledPaths().get(path).equals("Passenger")) {
                    path.getEdges().forEach(edge -> {
                        displayGraph.getEdge(edge.getEdge_Id()).setAttribute("ui.style", "size: 5px; fill-color: blue;");
                    });
                } else if (GraphUtils.getLabeledPaths().get(path).equals("Driver")) {
                    path.getEdges().forEach(edge -> {
                        displayGraph.getEdge(edge.getEdge_Id()).setAttribute("ui.style", "size: 5px; fill-color: red;");
                    });
                }
            }
        });
        return true;
    }
}