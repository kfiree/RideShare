package view;

import model.*;

import static controller.utils.LogHandler.LOGGER;
import static view.StyleUtils.*;

import model.interfaces.ElementsOnMap;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.j2dviewer.J2DGraphRenderer;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;

import java.util.*;


/**
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
    /* MAP */
    private final RoadMap roadMap;
    private final UsersMap userMap;
    private final Graph displayGraph;
    private RealTimeEvents events;

    /* DISPLAY */
    private final Viewer viewer;
    private final ViewerPipe pipeIn;

    protected static Date simulatorStartDate;
    private static Sprite clock;
    private static final long SLEEP_BETWEEN_FRAMES = 2000;
    private static boolean showAllPaths;

//    protected final Hashtable<Node, Drive> cars; //TODO check Exception in thread "main" java.util.ConcurrentModificationException
//    protected final Hashtable<Node, Pedestrian> pedestrians;

    /** CONSTRUCTORS */
    private MapView() {
        displayGraph = new MultiGraph("map simulation");
        roadMap = RoadMap.INSTANCE;
        userMap = UsersMap.INSTANCE;

        /* set graph */
        viewer = new Viewer(displayGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        pipeIn = viewer.newViewerPipe();
        viewer.addView("view1", new J2DGraphRenderer());
        viewer.disableAutoLayout();
        viewer.getView("view1").setMouseManager(new MapMouseManager());
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
        pipeIn.addAttributeSink( displayGraph );

        displayGraph.setAttribute("ui.stylesheet", styleSheet);
        displayGraph.setAttribute("ui.quality");
        displayGraph.setAttribute("ui.antialias");

    }

    /** Singleton specific properties */
    public static final MapView instance = new MapView();



    public void show(double simulatorSpeed, boolean showAllPaths){
        //load events
        events = new RealTimeEvents(simulatorSpeed);
        this.showAllPaths = showAllPaths;
        pipeIn.pump();

        // draw map components
        drawMapComponents();
        drawAddons();

        // start simulator
        Thread eventsThread = new Thread(events);
        eventsThread.start();

        do{
            pipeIn.pump();

            sleep();

            getUpdates();

        }while(eventsThread.isAlive() || !userMap.getDrives().isEmpty());
    }


    private void getUpdates(){
        removeFinishedEvents();
        moveCars();
        drawNewEvents();
    }

    private void drawNewEvents(){
        Iterator<ElementsOnMap> eventIter = events.getNewEvents().iterator();

        while(eventIter.hasNext()) {
            ElementsOnMap nextEvent = eventIter.next();

            Node displayObj = displayGraph.addNode(nextEvent.getId());

            //todo move to StyleUtils.java
            GeoLocation location = nextEvent.getLocation();
            displayObj.setAttribute("xy", location.getLongitude(), location.getLatitude());
            displayObj.setAttribute("ui.label", nextEvent.getId());
            displayObj.setAttribute("ui.style", randomGradientColor());

            if(nextEvent instanceof Drive) {
                displayObj.setAttribute("ui.class", "car");
//                styleDrives((Drive) nextEvent);
            } else {
//                displayObj.setAttribute("ui.class", "pedestrian");
                displayObj.setAttribute("ui.style", pedestrianStyleSheet);
            }
        }
    }

    private void removeFinishedEvents(){
        Iterator<ElementsOnMap> eventIter = userMap.getFinished().iterator();

        while(eventIter.hasNext()){
            ElementsOnMap nextEvent = eventIter.next();


            Node removedNode = displayGraph.removeNode(nextEvent.getId());

            eventIter.remove();
        }
    }

    int index = 0;
    private void moveCars(){
        Iterator<Drive> carIter = userMap.getDrives().iterator();

        while(carIter.hasNext()){
            Drive nextCar = carIter.next();

            GeoLocation location = nextCar.getLocation();

            if(location != null){
                Node car = displayGraph.getNode(nextCar.getId());
                if(car != null)
                car.setAttribute("xy", location.getLongitude(), location.getLatitude());
            }else{
                LOGGER.info(userMap.getDrives().size() + " On-Going drives.");
                displayGraph.removeNode(nextCar.getId());
            }

            index = (index+1) %10;
            if(index == 3) {
                if (showAllPaths) {
//                    System.out.println("======================style paths======================");
                    styleDrives(nextCar);
                } else {
                    styleFocusedDrive(nextCar);
                }
            }
        }
    }

    private void sleep() {
        simulatorStartDate = new Date(simulatorStartDate.getTime()+SLEEP_BETWEEN_FRAMES);
        clock.setAttribute("ui.label", dateFormatter.format(simulatorStartDate));
        try { Thread.sleep(SLEEP_BETWEEN_FRAMES) ; }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    private Boolean drawMapComponents(){
        roadMap.getEdges().forEach(e -> {
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

    private void drawAddons(){
        Node clockNode = displayGraph.addNode("clockNode");
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

}