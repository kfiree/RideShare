

package view;

import controller.utils.GraphAlgo;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.j2dviewer.J2DGraphRenderer;
import org.graphstream.ui.view.Viewer;
//import org.graphstream.ui.view.util.DefaultMouseManager;

import controller.utils.GraphUtils;
import model.OGraph;
import model.ONode;
import org.graphstream.ui.view.ViewerPipe;

import java.util.List;

public class MapView {
    private OGraph graph;
    private Graph displayGraph;
    private String styleSheet = "node {	text-mode: hidden; }";
    private ONode dest;
    private boolean newPath = false;

    private static MapView instance = new view.MapView();

    private MapView() {
        displayGraph = new MultiGraph("map simulation");
        graph = OGraph.getInstance();
        dest = graph.getNode(2432701015l);
    }

    public static MapView getInstance() {
        return instance;
    }

    public void show(){
        // draw graph components
        Viewer viewer = new Viewer(displayGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        ViewerPipe pipeIn = viewer.newViewerPipe();
        viewer.addView("view1", new J2DGraphRenderer());
        viewer.disableAutoLayout();
        viewer.getView("view1").setMouseManager(new CustomMouseManager());

        pipeIn.addAttributeSink( displayGraph );
        pipeIn.pump();

        drawEdge();
//        drawRider(utils);
//        drawPaths();

        displayGraph.setAttribute("ui.stylesheet", styleSheet);
        displayGraph.setAttribute("ui.quality");
        displayGraph.setAttribute("ui.antialias");

        ONode src = graph.getNode(1671579963l);

        int i = 1;
        List<ONode> path = GraphAlgo.AStar(src, dest);
        path.remove(0);//path.remove(path.size());
        assert path != null;
        Node nextInPath = displayGraph.getNode(String.valueOf(path.get(0).getOsm_Id()));

        while(true){
            pipeIn.pump();
            sleep(1);

            if(src != dest){
                ONode node = path.get(i);

                 if(!(node.getOsm_Id().equals(dest.getOsm_Id()) || node.getOsm_Id().equals(src.getOsm_Id()))) {

                     nextInPath.setAttribute("ui.style", "z-index: 1; size: 3px; fill-color: black;");

                     nextInPath = displayGraph.getNode(String.valueOf(node.getOsm_Id()));

                     nextInPath.setAttribute("ui.style", "z-index: 2; size: 10px; fill-color: blue;");
                 }
//                System.out.println(i);
                i = (i + 1) % path.size();

            }
        }
    }

    public void setDest(String key) {
        dest = graph.getNode(Long.parseLong(key));
    }

    private void sleep(long seconds ) {
//        seconds *= 1000;
        seconds = 500;
        try { Thread.sleep( seconds ) ; }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    private Boolean drawEdge(){
        graph.getEdges().forEach(e -> {
            if(displayGraph.getEdge(e.getId()) == null) {
                Node start = drawNode(e.getStartNode());
                Node end = drawNode(e.getEndNode());
                Edge edge = displayGraph.addEdge(e.getId(), start, end);//, e.isDirected());

                //            if(e.isDirected())
                //                edge.setAttribute("ui.style", "arrow-size: 1px;");
            }

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

            if(node.getOsm_Id() == 2432701015l || node.getOsm_Id() == 1671579963l){
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
//                    displayGraph.getEdge(edge.getId()).setAttribute("ui.style", "size: 5px; fill-color: blue;");
//                });
//        });

        GraphUtils.getLabeledPaths().keySet().forEach(path -> {
            if (path != null) {
                if (GraphUtils.getLabeledPaths().get(path).equals("Passenger")) {
                    path.getEdges().forEach(edge -> {
                        displayGraph.getEdge(edge.getId()).setAttribute("ui.style", "size: 5px; fill-color: blue;");
                    });
                } else if (GraphUtils.getLabeledPaths().get(path).equals("Driver")) {
                    path.getEdges().forEach(edge -> {
                        displayGraph.getEdge(edge.getId()).setAttribute("ui.style", "size: 5px; fill-color: red;");
                    });
                }
            }
        });
        return true;
    }
}

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