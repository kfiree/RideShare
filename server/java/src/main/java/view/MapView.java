package view;

import model.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.j2dviewer.J2DGraphRenderer;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;

import java.util.*;


/**
 *      |==================================|
 *      |===========| MAP VIEW |===========|
 *      |==================================|
 *
 *   simulator for road map.
 *
 *
 *
 *  version 1: graph in one thread.
 *
 * @author  Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 2.0
 * @since   2021-06-20
 */
public class MapView {
    private final RoadMap map;
    private final Graph displayGraph;
    protected final Hashtable<org.graphstream.graph.Node, Drive> cars; //TODO check Exception in thread "main" java.util.ConcurrentModificationException
    protected final Hashtable<Pedestrian, org.graphstream.graph.Node> pedestrians;
    private HashSet<org.graphstream.graph.Node> pedestrian;
    private RealTimeEvents events;
    private final Random rand = new Random(1);

    private static final long SLEEP_BETWEEN_FRAMES = 2000;

    private static final MapView instance = new MapView();

    private MapView() {
        displayGraph = new MultiGraph("map simulation");
        map = RoadMap.getInstance();
        cars = new Hashtable<>();
        pedestrians = new Hashtable<>();

    }

    public static MapView getInstance() {
        return instance;
    }




    public void show(double simulatorSpeed){
        //load events
        events = new RealTimeEvents(simulatorSpeed);

        // set graph
        Viewer viewer = new Viewer(displayGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        ViewerPipe pipeIn = viewer.newViewerPipe();
        viewer.addView("view1", new J2DGraphRenderer());
        viewer.disableAutoLayout();
        viewer.getView("view1").setMouseManager(new CustomMouseManager());
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
        pipeIn.addAttributeSink( displayGraph );
        pipeIn.pump();

        displayGraph.setAttribute("ui.stylesheet", styleSheet);
        displayGraph.setAttribute("ui.quality");
        displayGraph.setAttribute("ui.antialias");

        // draw map components
        drawMapComponents();

        // start simulator
        Thread eventsThread = new Thread(events);
        eventsThread.start();

        while(true){
            pipeIn.pump();

            sleep(SLEEP_BETWEEN_FRAMES);

            getUpdates();

            moveCars();

//            drawPedestrian();
        }
    }


    /**
     * TODO add Pedestrian events
     */
    private void getUpdates(){
        List<Drive> startedEvents = events.getStartedEvents();
        startedEvents.forEach(drive -> {
            org.graphstream.graph.Node displayDrive = displayGraph.addNode(drive.getOwnerId());

            GeoLocation location = drive.getLocation();
            displayDrive.setAttribute("xy", location.getLongitude(), location.getLatitude());
            displayDrive.setAttribute("ui.class", "car");
            displayDrive.setAttribute("ui.label", drive.getOwnerId());
            int r = rand.nextInt(256), g = rand.nextInt(256), b = rand.nextInt(256);
            displayDrive.setAttribute("ui.style", "fill-color: rgb(" + r + "," + g + "," + b + ");");
            cars.put(displayDrive, drive);
        });
    }

    private void moveCars(){
        cars.forEach( (displayNode, drive) ->{

            GeoLocation location = drive.getLocation();

            if(location != null){
                displayNode.setAttribute("xy", location.getLongitude(), location.getLatitude());
            }else{
                cars.remove(displayNode);
                displayGraph.removeNode(displayNode);
            }
        });
    }

    private void sleep(long ms ) {
        try { Thread.sleep( ms ) ; }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    private Boolean drawMapComponents(){
        map.getEdges().forEach(e -> {
            if(displayGraph.getEdge(e.getId()) == null) {
                org.graphstream.graph.Node start = drawNode(e.getNode1());
                org.graphstream.graph.Node end = drawNode(e.getNode2());
                displayGraph.addEdge(e.getId(), start, end);//, e.isDirected());
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
                    }

        return displayNode;
    }


    private String styleSheet=
            "node {"+
                " text-mode: hidden;"+
                " z-index: 1;"+
                " fill-color: grey;"+
                " size: 3px;"+
            "}"+
            "node.car {"+
                " z-index: 2;"+
                " size: 10px;"+
            "}"+
            "node.passenger {"+
                " fill-color: red;"+
                " size: 10px;"+
            "}";
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