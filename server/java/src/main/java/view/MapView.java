package view;

import controller.utils.GraphAlgo;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.j2dviewer.J2DGraphRenderer;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;

import model.RoadMap;
import model.Node;

import java.util.List;

public class MapView {
    private RoadMap map;
    private Graph displayGraph;
    private String styleSheet = "node {	text-mode: hidden; }";
    private Node dest;
    private boolean newPath = false;

    private static MapView instance = new view.MapView();

    private MapView() {
        displayGraph = new MultiGraph("map simulation");
        map = RoadMap.getInstance();
        dest = map.getNode(2432701015l);
    }

    public static MapView getInstance() {
        return instance;
    }

    public void show(){
        // draw map components
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

        Node src = map.getNode(1671579963l);

        int i = 1;
        List<Node> path = GraphAlgo.getShortestPath(src, dest);
        path.remove(0);//path.remove(path.size());
        assert path != null;
        org.graphstream.graph.Node nextInPath = displayGraph.getNode(String.valueOf(path.get(0).getOsmID()));

        while(true){
            pipeIn.pump();

            sleep(1);


            //TODO           event: +drive    +passenger     {type, timestamp, id , src, dest}
//            getUpdates();

            if(src != dest){
                Node node = path.get(i);

                 if(!(node.getOsmID().equals(dest.getOsmID()) || node.getOsmID().equals(src.getOsmID()))) {

                     nextInPath.setAttribute("ui.style", "z-index: 1; size: 3px; fill-color: black;");

                     nextInPath = displayGraph.getNode(String.valueOf(node.getOsmID()));

                     nextInPath.setAttribute("ui.style", "z-index: 2; size: 10px; fill-color: blue;");
                 }
//                System.out.println(i);
                i = (i + 1) % path.size();

            }
        }
    }

    public void setDest(String key) {
        dest = map.getNode(Long.parseLong(key));
    }

    private void sleep(long seconds ) {
//        seconds *= 1000;
        seconds = 500;
        try { Thread.sleep( seconds ) ; }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    private Boolean drawEdge(){
        map.getEdges().forEach(e -> {
            if(displayGraph.getEdge(e.getId()) == null) {
                org.graphstream.graph.Node start = drawNode(e.getStartNode());
                org.graphstream.graph.Node end = drawNode(e.getEndNode());
                Edge edge = displayGraph.addEdge(e.getId(), start, end);//, e.isDirected());

                //            if(e.isDirected())
                //                edge.setAttribute("ui.style", "arrow-size: 1px;");
            }

        });
        return true;
    }


    private org.graphstream.graph.Node drawNode(Node node){

        String keyStr = String.valueOf(node.getOsmID());
        org.graphstream.graph.Node displayNode = displayGraph.getNode(keyStr);

        if(displayNode == null){

            displayNode = displayGraph.addNode(keyStr);
            displayNode.setAttribute("xy", node.getLongitude(), node.getLatitude());
            displayNode.setAttribute("ui.label", node.getOsmID().toString());

            displayNode.setAttribute("ui.style", "z-index: 1; size: 3px;");

            if(node.getOsmID() == 2432701015l || node.getOsmID() == 1671579963l){
                displayNode.setAttribute("ui.style", "z-index: 2; size: 10px; fill-color: green;");
            } else if(node.getUser() == Node.userType.Rider){
                displayNode.setAttribute("ui.style", "fill-color: blue;");
            } else if(node.getUser() == Node.userType.Driver) {
                displayNode.setAttribute("ui.style", "fill-color: red;");
            }


        }

        return displayNode;
    }

//    private boolean drawRider(MapUtils utils){
//        utils.getRiders().values().forEach(rider->{
//            String keyStr = rider.getOsmID();
//            org.graphstream.graph.Node displayNode = displayGraph.getNode(keyStr);
//
//            if(displayNode == null){
//
//                displayNode = displayGraph.addNode(keyStr);
//                displayNode.setAttribute("xy", rider.getLongitude(), rider.getLatitude());
//                displayNode.setAttribute("ui.label", rider.getOsmID().toString());
//
//                displayNode.setAttribute("ui.style","fill-color: red;");
//            }
//
//        });
//        return true;
//    }
//
//    private boolean drawPaths() {
////        utils.getPaths().forEach(path -> {
////            if(path!=null)
////                path.getEdges().forEach(edge -> {
////                    displayGraph.getEdge(edge.getId()).setAttribute("ui.style", "size: 5px; fill-color: blue;");
////                });
////        });
//
//        MapUtils.getLabeledPaths().keySet().forEach(path -> {
//            if (path != null) {
//                if (MapUtils.getLabeledPaths().get(path).equals("Passenger")) {
//                    path.getEdges().forEach(edge -> {
//                        displayGraph.getEdge(edge.getId()).setAttribute("ui.style", "size: 5px; fill-color: blue;");
//                    });
//                } else if (MapUtils.getLabeledPaths().get(path).equals("Driver")) {
//                    path.getEdges().forEach(edge -> {
//                        displayGraph.getEdge(edge.getId()).setAttribute("ui.style", "size: 5px; fill-color: red;");
//                    });
//                }
//            }
//        });
//        return true;
//    }
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