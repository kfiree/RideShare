package app.view;

import app.controller.MatchMaker;
import app.model.*;

import static utils.Utils.FORMAT;
import static app.view.StyleUtils.*;

import app.model.interfaces.ElementsOnMap;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.j2dviewer.J2DGraphRenderer;
import org.graphstream.ui.spriteManager.Sprite;
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
    protected final UserMap userMap;
    static protected final Hashtable<ElementsOnMap, Node> elementsOnMap;
    static protected final Graph displayGraph;
    protected RealTimeEvents events;

    /* DISPLAY */
    protected final Viewer viewer;
    private final ViewerPipe pipeIn;

    protected static Date simulatorCurrTime;
    private static Sprite clock;
    private static final long SLEEP_BETWEEN_FRAMES = 2000;


    static{
        displayGraph = new MultiGraph("map simulation");
        elementsOnMap = new Hashtable<>();
    }

    /** CONSTRUCTORS */
    private MapView() {
        userMap = UserMap.INSTANCE;


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


    public void show(double simulatorSpeed, boolean showMode){
        //load events
        events = new RealTimeEvents(simulatorSpeed);
        simulatorCurrTime = userMap.getFirstEventTime();
        showAllPaths = showMode;
        pipeIn.pump();

        // draw map components
        drawMapComponents();
        clock = drawClock();

        // start simulator
        Thread eventsThread = new Thread(events);
        eventsThread.start();

        // start cupid
        Thread cupidThread = new Thread(MatchMaker.INSTANCE);
        cupidThread.start();

        do{
            pipeIn.pump();

            sleep();

            drawElementsOnMap();

        }while(eventsThread.isAlive() || !userMap.getDrives().isEmpty());
    }

    private void drawElementsOnMap(){
        removeFinishedEvents();

        for (Drive drive : userMap.getOnGoingDrives()) {
            Node node = elementsOnMap.get(drive);

            if (node == null) {
                node = displayGraph.addNode(drive.getId());
                elementsOnMap.put(drive, node);
                drawElement(drive, node, "car");
                node.setAttribute("ui.style", randomGradientColor());
            } else {
                moveCar(drive, node);
            }

        }

        for (Rider request : userMap.getPendingRequests()) {
            Node node = elementsOnMap.get(request);

            if (node == null) {

                node = displayGraph.addNode(request.getId());
                elementsOnMap.put(request, node);
                drawElement(request, node, "rider");
            }
        }
    }

    private void removeFinishedEvents(){
        Iterator<ElementsOnMap> eventIter = userMap.getFinished().iterator();

        while(eventIter.hasNext()){
            ElementsOnMap nextEvent = eventIter.next();

            try {
                if(elementsOnMap.containsKey(nextEvent)){
                    displayGraph.removeNode(nextEvent.getId());
                    elementsOnMap.remove(nextEvent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            eventIter.remove();
        }
    }

    private void sleep() {
        simulatorCurrTime = new Date(simulatorCurrTime.getTime()+SLEEP_BETWEEN_FRAMES);
        clock.setAttribute("ui.label", FORMAT(simulatorCurrTime));
        try { Thread.sleep(SLEEP_BETWEEN_FRAMES) ; }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    private void drawMapComponents(){
        RoadMap.INSTANCE.getEdges().forEach(e -> {
            if(displayGraph.getEdge(e.getId()) == null) {
                Node start = drawNode(e.getNode1());
                Node end = drawNode(e.getNode2());
                Edge displayEdge = displayGraph.addEdge(e.getId(), start, end);//, e.isDirected());
                displayEdge.setAttribute("ui.class", "edge."+e.getHighwayType());
            }

        });
    }

    private Node drawNode(app.model.Node node){

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