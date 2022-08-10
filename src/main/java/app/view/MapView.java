package app.view;


import app.controller.Simulator;

import static utils.Utils.FORMAT;
import static app.view.StyleUtils.*;

import app.model.graph.RoadMap;
import app.model.users.Driver;
import app.model.users.Rider;
import app.model.users.User;
import app.model.users.UserMap;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import javax.swing.*;
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
public class MapView{
    /* MAP */
    protected final UserMap userMap;
    static protected final Hashtable<User, Node> elementsOnMapNodes;
    static protected final Graph displayGraph;

    /* DISPLAY */
    protected SimulatorFrame simulatorFrame;
//    protected Viewer viewer;
//    private final ViewerPipe pipeIn;
//    private static Sprite clock;

    /* SIMULATOR */
    private Simulator simulator;
    private static final long SLEEP_BETWEEN_FRAMES;
    public static final boolean DEBUG = true;
    private Date simulatorCurrTime;


    static{
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        displayGraph = new MultiGraph("map simulation");

        displayGraph.addAttribute("ui.stylesheet", styleSheet);
        displayGraph.addAttribute("ui.quality");
        displayGraph.addAttribute("ui.antialias");

        elementsOnMapNodes = new Hashtable<>();
        SLEEP_BETWEEN_FRAMES = 2000;
    }

    /** CONSTRUCTORS */
    private MapView() {
        userMap = UserMap.INSTANCE;


//
//        JFrame frame = new JFrame();
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//
//        JPanel panel = new JPanel(new GridLayout())
//        {
//            @Override
//            public Dimension getPreferredSize() {
//                return Toolkit.getDefaultToolkit().getScreenSize();
////                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
////                setBounds(100, 100, (int) dim.getWidth(), (int) dim.getHeight());
////                return new Dimension(640, 480);
//            }
//        };
//
//        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//        frame.setBounds(100, 100, (int) dim.getWidth(), (int) dim.getHeight());
//        frame.setLocationRelativeTo(null);
//
//        panel.setBorder(BorderFactory.createLineBorder(Color.black, 5));
//
//        viewer = new Viewer(displayGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
////        ViewPanel graphRenderer = viewer.addView("graphRenderer", new J2DGraphRenderer());
////        View view = viewer.addDefaultView(false);
//
//        ViewPanel viewPanel = viewer.addDefaultView(false);
//        panel.add(viewPanel);
////        panel.add(new JSeparator());
//
//
//        frame.add(panel);
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//        pipeIn = simulatorFrame.getGraphViewer().newViewerPipe();
//        viewer.getView("view1").setShortcutManager(new MapShortcutManager());
//        viewer.getView("view1").setMouseManager(new MapMouseManager());
//        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
//        pipeIn.addAttributeSink( displayGraph );


    }

    /** Singleton specific properties */
    public static final MapView instance = new MapView();

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

        for (Driver drive : new ArrayList<>(userMap.getOnGoingDrives())) {
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

        for (Rider request : new ArrayList<>(userMap.getPendingRequests())) {
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
                    displayGraph.removeNode(String.valueOf(nextEvent.getId()));
                    elementsOnMapNodes.remove(nextEvent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            eventIter.remove();
        }
    }

//    private void sleep() {
//        simulatorCurrTime = new Date(simulatorCurrTime.getTime()+SLEEP_BETWEEN_FRAMES);
//        clock.addAttribute("ui.label", FORMAT(simulatorCurrTime));
//        try { Thread.sleep(SLEEP_BETWEEN_FRAMES) ; }
//        catch (InterruptedException e) { e.printStackTrace(); }
//    }

    private void drawRoadMap(){
        RoadMap.INSTANCE.getEdges().forEach(e -> {
            if(displayGraph.getEdge(String.valueOf(e.getId())) == null) {
                Node start = drawNode(e.getNode1());
                Node end = drawNode(e.getNode2());
                Edge displayEdge = displayGraph.addEdge(String.valueOf(e.getId()), start, end);//, e.isDirected());
                displayEdge.addAttribute("ui.class", "edge."+e.getHighwayType());
            }

        });
    }

    private Node drawNode(app.model.graph.Node node){

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
}



//
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

























































//
//
//package app.view;
//
//import app.controller.Simulator;
//import app.model.*;
//
//import static utils.Utils.FORMAT;
//import static app.view.StyleUtils.*;
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
// * @author  Kfir Ettinger & Amit Hajaj & Motti Dahari
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
////    public void run() {
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
//                Node start = drawNode(e.getNode1());
//                Node end = drawNode(e.getNode2());
//                Edge displayEdge = displayGraph.addEdge(String.valueOf(e.getId()), start, end);//, e.isDirected());
//                displayEdge.addAttribute("ui.class", "edge."+e.getHighwayType());
//            }
//
//        });
//    }
//
//    private Node drawNode(app.model.graph.Node node){
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