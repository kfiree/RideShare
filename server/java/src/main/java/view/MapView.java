package view;

import controller.utils.GraphAlgo;
import model.*;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.j2dviewer.J2DGraphRenderer;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;

import java.util.*;

import static controller.utils.LogHandler.LOGGER;


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
    private RoadMap map;
    private Graph displayGraph;
    private HashMap<Drive, org.graphstream.graph.Node> cars;
    private HashMap<Pedestrian, org.graphstream.graph.Node> pedestrians;
//    private List<Drive> onGoingDrives;
    private HashSet<org.graphstream.graph.Node> pedestrian;
    private RealTimeEvents events;

    private static final long SLEEP_BETWEEN_FRAMES = 2000;
    private static final double SYSTEM_SPEED = 2;

    private static MapView instance = new MapView();

    private MapView() {
        displayGraph = new MultiGraph("map simulation");
        map = RoadMap.getInstance();
        cars = new HashMap<>();
        pedestrians = new HashMap<>();
        events = new RealTimeEvents(initDrives(5), SYSTEM_SPEED);
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
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
        pipeIn.addAttributeSink( displayGraph );
        pipeIn.pump();

        drawMapComponents();

        displayGraph.setAttribute("ui.stylesheet", styleSheet);
        displayGraph.setAttribute("ui.quality");
        displayGraph.setAttribute("ui.antialias");


        Thread eventsThread = new Thread(events);
        eventsThread.start();

        int i = 0;
        while(true){
            pipeIn.pump();

            sleep(SLEEP_BETWEEN_FRAMES);

            getUpdates();

            moveCars();

//            drawPedestrian();
        }
    }

    public List<Drive> initDrives(int drivesNum) {
        // drives variables
        List<Drive> drives = new ArrayList<>();
        List<Node> nodes = new ArrayList<>(RoadMap.getInstance().getNodes());
        Node src, dst;
        long sessionStartInMs = (new Date()).getTime();

        // init random indexes for nodes
        Random rand = new Random(1);
        int[] randomIndexes = rand.ints(drivesNum*2, 0, nodes.size()).toArray();

        //create drives
        for (int i = 0; i < drivesNum; i++) {
            src = nodes.get(randomIndexes[i*2]);
            dst = nodes.get(randomIndexes[i*2 +1 ]);

            drives.add(createDrive(src, dst, i, sessionStartInMs));
        }

        LOGGER.finer("init "+drives.size() + " drives.");

        return drives;
    }

    private Drive createDrive(Node src, Node dst, int i, long sessionStartInMs){
        Path shortestPath;
        Drive drive = null;

        if(src != null && dst != null ){
            shortestPath = GraphAlgo.getShortestPath(src, dst);

            if(shortestPath != null) {
                Date startTime = new Date(sessionStartInMs + (15000 * i));
                drive = new Drive(shortestPath, "unknown" , String.valueOf(i), startTime );

                if(drive == null){
                    LOGGER.severe("drive from " + src + " to "+ dst + " was not created, Drive(Id: "+ i +", Date: " + startTime +" = , Path).");
                    //TODO add formatter for date
                }
//                validate(drive != null,"drive from " + src + " to "+ dst + " was not created, Drive(Id: "+ i +", Date: "+startTime.+" = , Path).");
            }
        }


        return drive;
    }

    private void getUpdates(){
        List<Drive> startedEvents = events.getStartedEvents();
        startedEvents.forEach(drive -> {
            org.graphstream.graph.Node node = displayGraph.addNode(drive.getOwnerId());

            GeoLocation location = drive.getCurrentEdge().getNode1().getCoordinates();
            node.setAttribute("xy", location.getLongitude(), location.getLatitude());
            node.setAttribute("ui.class", "car");//todo add getNextNode to drive

            cars.put(drive, node);
        });;//.addAll(startedEvents);
    }

    private void moveCars(){
        cars.forEach( (drive, displayNode) ->{

            model.Edge currEdge = drive.getCurrentEdge();

            if(currEdge != null){
                GeoLocation location = currEdge.getNode2().getCoordinates();
                displayNode.setAttribute("xy", location.getLongitude(), location.getLatitude());

//                displayNode = displayGraph.getNode(currEdge.getNode1().getOsmID().toString());
//                displayNode.setAttribute("ui.class", "normal");
//                displayNode = displayGraph.getNode(currEdge.getNode2().getOsmID().toString());
//                displayNode.addAttribute("ui.class", "car");
            }else{
                cars.remove(drive);
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
//            if(node.getOsmID() == 2432701015l || node.getOsmID() == 1671579963l){
//                displayNode.setAttribute("ui.style", "z-index: 2; size: 10px; fill-color: green;");
//            }else
//            if(node.getType() == Node.userType.Passenger){
//                displayNode.setAttribute("ui.class", "passenger");
//            } else if(node.getType() == Node.userType.Driver) {
//                displayNode.setAttribute("ui.class", "car");
//            }
        }

        return displayNode;
    }


    private String styleSheet=
            "node {"+
                " text-mode: hidden;"+
                " z-index: 1;"+
//                " fill-color: grey;"+
                " size: 3px;"+
            "}"+
            "node.car {"+
                " z-index: 2;"+
                " fill-color: blue;"+
                " size: 10px;"+
            "}"+
            "node.normal {"+
                " z-index: 1;"+
                " fill-color: black;"+
                " size: 3px;"+
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