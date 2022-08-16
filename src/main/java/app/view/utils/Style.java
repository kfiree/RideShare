package app.view.utils;


import app.model.users.Driver;
import app.model.users.User;
import app.model.users.UserMap;
import app.model.utils.Coordinates;
import app.view.MapView;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import static utils.logs.LogHandler.LOGGER;

/**
 * todo remove redundant null-checks
 */
public class Style {

    private Style(){}


    /*     |==== NODE STYLE ====|      */

    protected static Node focusedCar;
    protected static Driver focusedDrive;
//    protected static final HashMap<String, Edge[]> carPaths = new HashMap<>();
    protected static final HashMap<String, String> carColors = new HashMap<>();
    protected static Edge[] focusedPath;
    public static final String nodeStyleSheet, riderStyleSheet, carStyleSheet,
            edgeStyleSheet, secondaryEdgeStyleSheet, primaryEdgeStyleSheet,
            motorwayEdgeStyleSheet, tertiaryEdgeStyleSheet, pathStyleSheet,
            spriteStyleSheet, styleSheet, focusedCarStyleSheet;
    private static ReentrantLock lock = new ReentrantLock();



    /* STYLE USERS DATA */

//    private static synchronized void styleDrives(Drive... drives) {
//        for(Drive drive: drives){
//            Edge[] currPath = carPaths.get(drive.getId());
//            /* if path changed */
//            if (currPath != null && currPath.length != drive.getNodes().size()) {
//                /* clean old path style */
//                styleEdges(edgeStyleSheet, currPath);
//                /* updateUsers path and paint */
//                stylePath(drive);
//            } else if (currPath == null) {
//
//                /* add path and paint */
//                stylePath(drive);
//            }
//        }
//    }

    protected static synchronized void stylePath(Driver drive){
        MapView mapView = MapView.INSTANCE;
        if(drive !=null) {

            Node car = mapView.getElementsOnMapNodes().get(drive);
//            Edge[] edges = focusedPath;
            String color = "";


            if(car != null){

                color = extractAttribute("fill-color", car);

                Edge[] edges = Arrays.stream(drive.getPath().getEdgeIds())
                        .map(mapView.getDisplayGraph()::getEdge).toArray(Edge[]::new);

                styleEdges(pathStyleSheet + color,  edges);

//                Arrays.stream(drive.getPath().getEdges()).forEach(edge -> {
//                    styleEdges(pathStyleSheet + finalColor,  mapView.getDisplayGraph().getEdge(String.valueOf(edge.getId())));
//                });
//                for (int i = 0; i < pathSize; i++) {
//
//                    edges[i] = mapView.getDisplayGraph().getEdge(String.valueOf(drive.getNodes().get(i).getId()));
//                }
//                color = extractAttribute("fill-color", car);
//
//                if (edges != null) {
//                    styleEdges(pathStyleSheet + color,edges);
//                }

                stylePassenger(drive, color);
            }
            //todo
            //  1. make lighter color.
            //  2. style taken.
        }
    }

    private static synchronized void stylePassenger(Driver drive, String color){
        drive.passengerOperation(rider -> styleNodes(color, MapView.INSTANCE.getElementsOnMapNodes().get(rider)));
    }


    /* style */
    protected static void styleFocusedDrive( Driver drive) {
        try {
            lock.lock();
            if (focusedCar != null && drive == focusedDrive) {
                stylePath(focusedDrive); //todo add focusedEdges to carPaths with sending it to styleDrives
//                System.out.println(""+focusedCar.getAttribute("ui.style"));
                styleNodes(focusedCarStyleSheet+focusedCar.getAttribute("ui.style"), focusedCar);
            }//todo duplicate 2
        }finally {
            lock.unlock();
        }
    }

    /* call from MouseManager */
    public static void focusOn(@NotNull Driver drive){
        MapView mapView = MapView.INSTANCE;
        try {
            lock.lock();
            if (focusedDrive != drive) {
                if (focusedDrive != null) {
                    /* reset prev drive style */
                    styleEdges(edgeStyleSheet);
                    styleNodes(carStyleSheet, focusedCar);//todo duplicate 1
                }

                /* set new drive */
                int pathSize = drive.getNodes().size();
                focusedPath = new Edge[pathSize];
                for (int i = 0; i < pathSize - 1; i++) {
                    focusedPath[i] =
                            mapView.getDisplayGraph().getEdge(String.valueOf(drive.getNodes().get(i).getEdgeTo(drive.getNodes().get(i + 1)).getId()));
                }

                focusedDrive = drive;
                focusedCar = mapView.getElementsOnMapNodes().get(drive);

                /* style new drive */
                styleFocusedDrive(drive);
            }
        }finally {
            lock.unlock();
        }
    }

    public static void drawElement(User element, Node node, String styleClass) {
        Coordinates location = element.getCoordinates();
        node.addAttribute("xy", location.getLongitude(), location.getLatitude());
        node.addAttribute("ui.label", element.getId());
        if(styleClass.equals("rider")){
            node.setAttribute("ui.style", riderStyleSheet);
        }else{
            node.setAttribute("ui.style", carStyleSheet);
            node.setAttribute("ui.style", "size: 15px;");
        }
        // node.addAttribute("ui.class", styleClass);
    }


    public static void moveCar(Driver drive, Node car){
        MapView mapView = MapView.INSTANCE;
        Coordinates location = drive.getCoordinates();

        if(location != null){
            car.addAttribute("xy", location.getLongitude(), location.getLatitude());
        }else{
            LOGGER.info(UserMap.INSTANCE.getDrives().size() + " On-Going drives.");
            mapView.getDisplayGraph().removeNode(drive.getId());
        }

        try {
            lock.lock();

            String color = carColors.get(drive.getId());
            if(color == null) {
                color = extractAttribute("fill-color", mapView.getElementsOnMapNodes().get(drive));
            }
            stylePassenger(drive, color);

        } finally {
            lock.unlock();
        }
    }

    /* STYLE MAP PARTS */

    public static void styleNode(Node node){
            node.setAttribute("ui.style", nodeStyleSheet);

    }

    private static void styleEdges(String styleSheet, Edge... edges) {
        if(edges.length ==0){
            edges = focusedPath;
        }

        for (Edge edge : edges) {
            if (edge != null) {
                MapView.INSTANCE.getDisplayGraph().getEdge(edge.getId()).addAttribute("ui.style", styleSheet);
            }
        }
    }

    private static void styleNodes(String styleSheet, Node... nodes){
        if(nodes.length == 0){
            focusedCar.addAttribute("ui.style", styleSheet);
        }else for (Node node : nodes) {
            if (node != null) {
                node.setAttribute("ui.style", styleSheet);
            }
        }
    }



    /* STYLE UTILS */

    private static String extractAttribute(String style, @NotNull Node displayNode){
        String styleSheet = displayNode.getAttribute("ui.style");
        String[] styleAttributes = styleSheet.split(";"); //split to attributes

        for (String att:styleAttributes) {
            if(att.contains(style)){
                return att+";";
            }
        }
        return "";
    }

    protected static String splitStyleVal(String attribute){
        String[] attributeKeyVal = attribute.split(":");
        String[] styleValueList = Arrays.copyOfRange(attributeKeyVal, 1, attributeKeyVal.length);
        StringBuilder styleValue = new StringBuilder();
        for (String s : styleValueList) {
            styleValue.append(s);
        }
        return styleValue.toString();
    }

    private static final Random rand = new Random(1);
    public static String randomGradientColor(){
        int r = rand.nextInt(256), g = rand.nextInt(256), b = rand.nextInt(256);
        return "  fill-mode: dyn-plain; " +
                " fill-color: rgb(" + r +"," + g + "," + b + "), white;" +
                " fill-mode: gradient-horizontal;";
    }

    static public final Color LIGHT_GREEN = new Color(114,187,102);
    static public final Color LIGHT_RED = new Color(212, 81, 81);
    static public final Color LIGHT_BLUE = new Color(112, 139, 220);
    static public final Color LIGHT_PURPLE = new Color(173, 106, 191);

    /* STYLE-SHEETS */
    static {
        /*     |==== NODE ====|   */

        nodeStyleSheet = "text-mode: hidden;" +
                "z-index: 1;" +
                "fill-color: grey;" +
                "size: 3px;";
        riderStyleSheet =
                " z-index: 2;" +
                " size: 20px;" +
                " text-mode: normal;" +
                " text-style: bold;" +
                " stroke-mode: plain;" +
                " stroke-color: black;" +
                " stroke-width: 1px;" +
                "  fill-mode: dyn-plain; " +
                " fill-color: grey, white;" +
                " fill-mode: gradient-horizontal;"+
                " shape: diamond;";
        focusedCarStyleSheet =
                "size: 25px;";
        carStyleSheet =
                "z-index: 2;" +
                "size: 15px;" +
                "text-mode: normal;" +
                "text-style: bold;" +
                "stroke-mode: plain;" +
                "stroke-color: black;" +
                "stroke-width: 1px;";

        /*     |==== EDGE ====|     */

        edgeStyleSheet =
                "fill-color: black;" +
                "size: 1px;" ;
        pathStyleSheet =
                "size: 4px;";
        motorwayEdgeStyleSheet =
                "fill-color: rgb(50, 50, 255);" +
                "size: 2px;";
        tertiaryEdgeStyleSheet =
                "fill-color: rgb(50, 50, 200);" +
                "size: 1px;";
        secondaryEdgeStyleSheet =
                "fill-color: rgb(50, 50, 150);" +
                "size: 1.25px;";
        primaryEdgeStyleSheet =
                "fill-color: rgb(50, 50, 100);" +
                "size: 1.5px;";


        /*     |==== SPRITE ====|   */

        spriteStyleSheet =
                "";

        /*     |==== STYLE ====|   */

        styleSheet =
                "edge{" + edgeStyleSheet + "}" +
                "node.car {" + carStyleSheet + "}" +
                "node.rider {" + riderStyleSheet + "}";
//                "node {" + nodeStyleSheet + "}" +
    }

}