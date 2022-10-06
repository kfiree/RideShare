package simulator.view;


import simulator.controller.Simulator;

import static simulator.view.utils.Style.*;

import road_map.model.graph.RoadMap;
import simulator.model.users.Driver;
import simulator.model.users.Passenger;
import simulator.model.users.User;
import simulator.model.users.UserMap;
import simulator.view.frames.SimulatorFrame;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 *
 *      |==================================|
 *      |===========| MAP VIEW |===========|
 *      |==================================|
 * <p>
 *   simulator for road map.
 * <p>
 *
 *
 *  version 1: graph in one thread.
 *
 * @author  Kfir Ettinger
 * @version 2.0
 * @since   2021-06-20
 */
public final class MapView{
    /* MAP */
    private final UserMap userMap;
    private final Map<User, Node> elementsOnMapNodes;
    private final Graph displayGraph;

    /* DISPLAY */
    private SimulatorFrame simulatorFrame;
//    protected Viewer viewer;
//    private final ViewerPipe pipeIn;
//    private static Sprite clock;

    /* SIMULATOR */
    private Simulator simulator;
    private static final long SLEEP_BETWEEN_FRAMES;
    public static final boolean DEBUG = true;
    private Date simulatorCurrTime;


    static{
        SLEEP_BETWEEN_FRAMES = 2000;
    }

    /** CONSTRUCTORS */
    private MapView() {
        userMap = UserMap.INSTANCE;
        displayGraph = new MultiGraph("map simulation");

        displayGraph.addAttribute("ui.stylesheet", STYLE_SHEET);
        displayGraph.addAttribute("ui.quality");
        displayGraph.addAttribute("ui.antialias");

        elementsOnMapNodes = new ConcurrentHashMap<>();
    }

    /** Singleton specific properties */
    public static final MapView INSTANCE = new MapView();

    public void init(Simulator simulator){
        this.simulator = simulator;

        drawRoadMap();
        drawUsers();

        this.simulatorFrame = new SimulatorFrame();
        simulatorFrame.setVisible(true);
        simulatorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void update(){
        this.simulatorFrame.updateFrame();

        removeFinishedEvents();
        drawUsers();
    }

    private void drawUsers(){

        for (Driver drive : new ArrayList<>(userMap.getLiveDrives())) {
            Node node = elementsOnMapNodes.get(drive);

            if (node == null) {
                node = displayGraph.addNode(String.valueOf(drive.getId()));
                elementsOnMapNodes.put(drive, node);
                drawElement(drive, node, "car");
                node.addAttribute("ui.style", randomGradientColor());
            } else {
                moveCar(drive, node);
            }

        }

        for (Passenger request : new ArrayList<>(userMap.getLiveRequest())) {
            Node node = elementsOnMapNodes.get(request);

            if (node == null) {
                node = displayGraph.addNode(String.valueOf(request.getId()));
                elementsOnMapNodes.put(request, node);
                drawElement(request, node, "rider");
            }
        }
    }

    private void removeFinishedEvents(){
        Iterator<User> eventIter = userMap.getFinishedEvents().iterator();

        while(eventIter.hasNext()){
            User nextEvent = eventIter.next();
            try {
                if(elementsOnMapNodes.containsKey(nextEvent)){
                    Node node = displayGraph.getNode(String.valueOf(nextEvent.getId()));
                    if (node != null) {
                        node.setAttribute( "ui.hide");
                    }
//                    displayGraph.removeNode(String.valueOf(nextEvent.getId()));
                    elementsOnMapNodes.remove(nextEvent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            eventIter.remove();
        }
    }

    private void drawRoadMap(){
        RoadMap.INSTANCE.edgesOperation(e -> {
            if(displayGraph.getEdge(String.valueOf(e.getId())) == null) {
                Node start = drawNode(e.getNode1());
                Node end = drawNode(e.getNode2());
                Edge displayEdge = displayGraph.addEdge(String.valueOf(e.getId()), start, end);//, e.isDirected());
                displayEdge.addAttribute("ui.class", "edge."+e.getHighwayType());
            }
        });
    }

    private Node drawNode(road_map.model.graph.Node node){

        String keyStr = String.valueOf(node.getId());
        Node displayNode = displayGraph.getNode(keyStr);

        if(displayNode == null){

            displayNode = displayGraph.addNode(keyStr);
            styleNode(displayNode);
            displayNode.addAttribute("xy", node.getLongitude(), node.getLatitude());
            displayNode.addAttribute("ui.label", node.getId().toString());
                    }

        return displayNode;
    }

    /* GETTERS */

    public Map<User, Node> getElementsOnMapNodes() {
        return elementsOnMapNodes;
    }

    public Graph getDisplayGraph() {
        return displayGraph;
    }
}



























































//
//
//package app.view;
//
//import app.controller.simulator.controller.Simulator;
//import app.model.*;
//
//import static utils.Utils.FORMAT;
//import static app.view.utils.StyleUtils.*;
//
//import app.model.interfaces.ElementOnMap;
//import org.graphstream.graph.Edge;
//import org.graphstream.graph.Graph;
//import org.graphstream.graph.Node;
//import org.graphstream.graph.implementations.MultiGraph;
//import org.graphstream.ui.j2dviewer.J2DGraphRenderer;
//import org.graphstream.ui.spriteManager.Sprite;
//import org.graphstream.ui.view.Viewer;
//import org.graphstream.ui.view.ViewerPipe;
//
//import java.util.*;
//
//
///**
// *
// *      |==================================|
// *      |===========| MAP VIEW |===========|
// *      |==================================|
// *
// *   simulator for road map.
// *
// *
// *
// *  version 1: graph in one thread.
// *
// * @author  Kfir Ettinger
// * @version 2.0
// * @since   2021-06-20
// */
//public class MapView{
//    /* MAP */
//    protected final UserMap userMap;
//    static protected final Hashtable<ElementOnMap, Node> elementsOnMapNodes;
//    static protected final Graph displayGraph;
//
//    /* DISPLAY */
//    protected final Viewer viewer;
//    private final ViewerPipe pipeIn;
//    private static Sprite clock;
//
//    /* SIMULATOR */
//    private Simulator simulator;
//    private static final long SLEEP_BETWEEN_FRAMES;
//    public static final boolean DEBUG = true;
//    private Date simulatorCurrTime;
//
//
//    static{
//        displayGraph = new MultiGraph("map simulation");
//        elementsOnMapNodes = new Hashtable<>();
//        SLEEP_BETWEEN_FRAMES = 2000;
//    }
//
//    /** CONSTRUCTORS */
//    private MapView() {
//        userMap = UserMap.INSTANCE;
//
//
//        /* set graph */
//        viewer = new Viewer(displayGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
//        pipeIn = viewer.newViewerPipe();
//        viewer.addView("view1", new J2DGraphRenderer());
//        viewer.disableAutoLayout();
//
//        viewer.getView("view1").setShortcutManager(new MapShortcutManager());
//        viewer.getView("view1").setMouseManager(new MapMouseManager());
//        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
//        pipeIn.addAttributeSink( displayGraph );
//
//        displayGraph.addAttribute("ui.stylesheet", styleSheet);
//        displayGraph.addAttribute("ui.quality");
//        displayGraph.addAttribute("ui.antialias");
//
//
//    }
//
//    /** Singleton specific properties */
//    public static final MapView instance = new MapView();
//
////    @Override
////    public void operate() {
////        this.simulatorCurrTime = simulator.time();
////
////        pipeIn.pump();
////
////        // draw map components
////        drawRoadMap();
////        clock = drawClock();
////
////        do{
////            pipeIn.pump();
////
////            sleep();
////
////            drawUsers();
////
////        }while(simulator.isAlive());
////
////        System.out.println("Simulator show done.");
////    }
//
//    public void init(Simulator simulator){
//        this.simulator = simulator;
//
//        pipeIn.pump();
//
//        drawRoadMap();
//
//        clock = drawClock();
//
//        updateUsers();
//    }
//
//    public void updateUsers(){
//        pipeIn.pump();
//
//        clock.addAttribute("ui.label", FORMAT(simulator.time()));
//
//        drawUsers();
//    }
//
//    private void drawUsers(){
//        removeFinishedEvents();
//
//        for (Drive drive : userMap.getOnGoingDrives()) {
//            Node node = elementsOnMapNodes.get(drive);
//
//            if (node == null) {
//                node = displayGraph.addNode(String.valueOf(drive.getId()));
//                elementsOnMapNodes.put(drive, node);
//                drawElement(drive, node, "car");
//                node.addAttribute("ui.style", randomGradientColor());
//            } else {
//                moveCar(drive, node);
//            }
//
//        }
//
//        for (Rider request : userMap.getPendingRequests()) {
//            Node node = elementsOnMapNodes.get(request);
//
//            if (node == null) {
//                node = displayGraph.addNode(String.valueOf(request.getId()));
//                elementsOnMapNodes.put(request, node);
//                drawElement(request, node, "rider");
//            }
//        }
//    }
//
//    private void removeFinishedEvents(){
//        Iterator<ElementOnMap> eventIter = userMap.getFinishedEvents().iterator();
//
//        while(eventIter.hasNext()){
//            ElementOnMap nextEvent = eventIter.next();
//            System.out.println("MapView remove finished event " + nextEvent);
//
//            try {
//                if(elementsOnMapNodes.containsKey(nextEvent)){
//                    displayGraph.removeNode(String.valueOf(nextEvent.getId()));
//                    elementsOnMapNodes.remove(nextEvent);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            eventIter.remove();
//        }
//    }
//
////    private void sleep() {
////        simulatorCurrTime = new Date(simulatorCurrTime.getTime()+SLEEP_BETWEEN_FRAMES);
////        clock.addAttribute("ui.label", FORMAT(simulatorCurrTime));
////        try { Thread.sleep(SLEEP_BETWEEN_FRAMES) ; }
////        catch (InterruptedException e) { e.printStackTrace(); }
////    }
//
//    private void drawRoadMap(){
//        RoadMap.INSTANCE.getEdges().forEach(e -> {
//            if(displayGraph.getEdge(String.valueOf(e.getId())) == null) {
//                Node choose = drawNode(e.getNode1());
//                Node end = drawNode(e.getNode2());
//                Edge displayEdge = displayGraph.addEdge(String.valueOf(e.getId()), choose, end);//, e.isDirected());
//                displayEdge.addAttribute("ui.class", "edge."+e.getHighwayType());
//            }
//
//        });
//    }
//
//    private Node drawNode(road_map.graph.Node node){
//
//        String keyStr = String.valueOf(node.getId());
//        Node displayNode = displayGraph.getNode(keyStr);
//
//        if(displayNode == null){
//
//            displayNode = displayGraph.addNode(keyStr);
//            displayNode.addAttribute("xy", node.getLongitude(), node.getLatitude());
//            displayNode.addAttribute("ui.label", node.getId().toString());
//        }
//
//        return displayNode;
//    }
//
//
//
//}