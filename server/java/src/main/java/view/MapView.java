package view;

import model.*;

import static controller.utils.LogHandler.LOGGER;
import static view.StyleUtils.*;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.j2dviewer.J2DGraphRenderer;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;

import java.awt.geom.Point2D;
import java.util.*;


/**
 *TODO: 6/29/2022 print path data on click
 *      * text-visibility-mode: The text visibility mode describe when the optional label of elements should be printed:
 *      * https://graphstream-project.org/doc/Advanced-Concepts/GraphStream-CSS-Reference/1.3/
 *      * show only first digit in long car/user id
 *
 *
 *
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
        viewer.getView("view1").setMouseManager(new MapMouseManager());
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
        pipeIn.addAttributeSink( displayGraph );
        pipeIn.pump();

        displayGraph.setAttribute("ui.stylesheet", styleSheet);// getAttributes("graph"));
        displayGraph.setAttribute("ui.quality");
        displayGraph.setAttribute("ui.antialias");

        // draw map components
        drawMapComponents();

        // start simulator
        Thread eventsThread = new Thread(events);
        eventsThread.start();

        while(!cars.isEmpty() || eventsThread.isAlive()){
            pipeIn.pump();

            sleep();

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
            displayDrive.setAttribute("ui.style", randomGradientColor());
            displayDrive.setAttribute("ui.label", drive.getOwnerId());

            cars.put(displayDrive, drive);
        });
    }

    private void moveCars(){
        Iterator<Map.Entry<org.graphstream.graph.Node, Drive>> iter = cars.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<org.graphstream.graph.Node, Drive> nodeDriveEntry = iter.next();
            GeoLocation location = nodeDriveEntry.getValue().getLocation();

            if(location != null){
                nodeDriveEntry.getKey().setAttribute("xy", location.getLongitude(), location.getLatitude());
            }else{
                iter.remove();
                LOGGER.info(cars.size() + " cars still running");
                displayGraph.removeNode(nodeDriveEntry.getKey());
            }
        }
    }

    private void sleep() {
        try { Thread.sleep(SLEEP_BETWEEN_FRAMES) ; }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    private Boolean drawMapComponents(){
        map.getEdges().forEach(e -> {
            if(displayGraph.getEdge(e.getId()) == null) {
                org.graphstream.graph.Node start = drawNode(e.getNode1());
                org.graphstream.graph.Node end = drawNode(e.getNode2());
                org.graphstream.graph.Edge displayEdge = displayGraph.addEdge(e.getId(), start, end);//, e.isDirected());
                displayEdge.setAttribute("ui.class", "edge."+e.getHighwayType());
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
}