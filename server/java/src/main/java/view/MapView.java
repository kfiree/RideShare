package view;

import controller.utils.MapUtils;
import model.*;

import static controller.utils.LogHandler.LOGGER;
import static view.StyleUtils.*;

import model.Pedestrian;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.j2dviewer.J2DGraphRenderer;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
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
    protected final Hashtable<Node, Drive> cars; //TODO check Exception in thread "main" java.util.ConcurrentModificationException
    protected final Hashtable<Pedestrian, Node> pedestrians;
    private HashSet<Node> pedestrian;
    private RealTimeEvents events;
    private Viewer viewer;
    protected static Date date;
    protected static Sprite clock;
    protected static Node clockNode;
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
        viewer = new Viewer(displayGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
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
        drawAddons();

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

    private void drawAddons(){
        clockNode = displayGraph.addNode("clockNode");
//        Double maxLatitude = MapUtils.getMaxLatitude();
//        Double minLongitude = MapUtils.getMinLongitude();

//        Point3 clockCoordinates = viewer.getView("view1").getCamera().transformPxToGu(10, 10);
//        System.out.println(clockCoordinates);
//        System.out.println("lon "+minLongitude + " - " + 0.005 + " = " + (minLongitude-0.05));
//        System.out.println("lat "+maxLatitude + " - " + 0.005 + " = " + (maxLatitude-0.05));

        clockNode.setAttribute("xy", 34.7615, 32.1179);



        SpriteManager sman = new SpriteManager(displayGraph);
        clock = sman.addSprite("clock");
        clock.addAttribute("ui.label", "YYYY-MM-DD HH:mm:ss");

        clock.setAttribute("ui.style",
                "   fill-mode: plain;"+
                        "   fill-color: #CCC;"+
                        "   stroke-mode: plain;"+
                        "   stroke-color: black;"+
                        "   stroke-width: 1px;"+
                        "   text-size: 16;"+
                        "   text-style: bold;"+
                        "   text-color: #FFF;"+
                        "   text-alignment: center;"+
                        "   text-padding: 3px, 2px;"+
                        "   text-background-mode: rounded-box;"+
                        "   text-background-color: #A7CC;"+
                        "   text-color: white;"+
                        "   text-offset: 5px, 0px;"
        );
        clock.attachToNode("clockNode");
//        32.095888895536966, max 34.7756799147988 min
        //  private static Double _topLatitude = 32.13073917015928, _bottomLatitude = 32.0449580796914,
        //                        _topLongitude = 34.852006, _bottomLongitude = 34.72856;

//        clock.setPosition( minLongitude- 0.005, maxLatitude - 0.002, 0);
//        System.out.println( "MouseEvent xy ("+e.getX() +","+e.getY()+").");
//        System.out.println(view.getCamera().transformPxToGu(e.getX() ,e.getY()));
    }

    /**
     * TODO add Pedestrian events
     */
    private void getUpdates(){
//        Point3 clockCoordinates = viewer.getView("view1").getCamera().transformPxToGu(100, 100);
//        clockNode.setAttribute("xy", clockCoordinates.x, clockCoordinates.y);

        List<Drive> startedEvents = events.getStartedEvents();
        startedEvents.forEach(drive -> {
            Node displayDrive = displayGraph.addNode(drive.getOwnerId());

            GeoLocation location = drive.getLocation();
            displayDrive.setAttribute("xy", location.getLongitude(), location.getLatitude());
            displayDrive.setAttribute("ui.class", "car");
            displayDrive.setAttribute("ui.style", randomGradientColor());
            displayDrive.setAttribute("ui.label", drive.getOwnerId());

            cars.put(displayDrive, drive);
        });
//        clock.setAttribute("xy", minLongitude- 0.005, maxLatitude - 0.002 );
    }

    private void moveCars(){
        Iterator<Map.Entry<Node, Drive>> iter = cars.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<Node, Drive> nodeDriveEntry = iter.next();
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
        date = new Date(date.getTime()+SLEEP_BETWEEN_FRAMES);
        clock.setAttribute("ui.label", dateFormatter.format(date));
        try { Thread.sleep(SLEEP_BETWEEN_FRAMES) ; }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    private Boolean drawMapComponents(){
        map.getEdges().forEach(e -> {
            if(displayGraph.getEdge(e.getId()) == null) {
                Node start = drawNode(e.getNode1());
                Node end = drawNode(e.getNode2());
                Edge displayEdge = displayGraph.addEdge(e.getId(), start, end);//, e.isDirected());
                displayEdge.setAttribute("ui.class", "edge."+e.getHighwayType());
            }

        });
        return true;
    }

    private Node drawNode(model.Node node){

        String keyStr = String.valueOf(node.getOsmID());
        Node displayNode = displayGraph.getNode(keyStr);

        if(displayNode == null){

            displayNode = displayGraph.addNode(keyStr);
            displayNode.setAttribute("xy", node.getLongitude(), node.getLatitude());
            displayNode.setAttribute("ui.label", node.getOsmID().toString());
                    }

        return displayNode;
    }
}