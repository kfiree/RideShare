package app.view;


import app.model.Drive;
//import app.model.Edge;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * MAP ELEMENTS ATTRIBUTES
 *
 *     "fill-mode" ...
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
 *
 * todo remove redundant null-checks
 */
public class StyleUtils {

    private StyleUtils(){}


    /**     |==== NODE STYLE ====|      */
    protected static Node focusedCar;
    protected static Drive focusedDrive;
    protected static final HashMap<String, Edge[]> carPaths = new HashMap<>();
    protected static Edge[] focusedEdges;
    protected static GraphicGraph displayGraph;
    protected static final String
            nodeStyleSheet, pedestrianStyleSheet, carStyleSheet,
            edgeStyleSheet, secondaryEdgeStyleSheet, primaryEdgeStyleSheet,
            motorwayEdgeStyleSheet, tertiaryEdgeStyleSheet, pathStyleSheet,
            spriteStyleSheet, styleSheet, focusedCarStyleSheet;



    /** STYLE USERS DATA */

    protected static synchronized void styleDrives(Drive... drives) {
        for(Drive drive: drives){
//            System.out.println(1);
            Edge[] currPath = carPaths.get(drive.getId());

//            System.out.println(2);
            /* if path changed */
            if (currPath != null && currPath.length != drive.getEdges().size()) {
//                System.out.println(3);
                /* clean old path style */
                styleEdges(edgeStyleSheet, currPath);
//                System.out.println(4);
                /* update path and paint */
                StylePath(drive);
//                System.out.println(5);
            } else if (currPath == null) {

                /* add path and paint */
//                System.out.println(6);
                StylePath(drive);
            }
        }
    }

    private static synchronized void StylePath(Drive drive){
        if(drive !=null) {
            Node car = focusedCar;
            Edge[] edges = focusedEdges;

            if(drive != focusedDrive){
                car = displayGraph.getNode(drive.getId());
                if(car == null){
                    System.out.println("something bad happened");
                    return;
                }
                int pathSize = drive.getEdges().size();
                edges = new Edge[pathSize];
                for (int i = 0; i < pathSize; i++) {
                    edges[i] = displayGraph.getEdge(drive.getEdges().get(i).getId());
                }
                carPaths.put(drive.getId(), edges);
            }

            String color = extractAttribute("fill-color", car);

            if (edges != null) {
                styleEdges(pathStyleSheet + color,edges);
            }

            drive.getPassengers().forEach(pedestrian ->
                    styleNodes(color, displayGraph.getNode(pedestrian.getId())));

            //todo
            //  1. make lighter color.
            //  2. style taken.
        }
    }

    /* style */
    protected static void styleFocusedDrive( Drive drive) {
        if(focusedCar!=null && drive == focusedDrive){
            StylePath(focusedDrive); //todo add focusedEdges to carPaths with sending it to styleDrives
            styleNodes(focusedCarStyleSheet, focusedCar);
        }
    }

    /* call from MouseManager */
    public static void focusOn(@NotNull Drive drive){
        if(focusedDrive != drive){
            if(focusedDrive != null){
                /* reset prev drive style */
                styleEdges(edgeStyleSheet);
                styleNodes(carStyleSheet, focusedCar);
            }

            /* set new drive */
            int pathSize = drive.getEdges().size();
            focusedEdges = new Edge[pathSize];
            for (int i = 0; i < pathSize; i++) {
                focusedEdges[i] = displayGraph.getEdge(drive.getEdges().get(i).getId());
            }

            focusedDrive = drive;
            focusedCar = displayGraph.getNode(drive.getId());

            /* style new drive */
            styleFocusedDrive(drive);
        }
    }



    /** STYLE MAP PARTS */

    protected static void styleEdges(String styleSheet, Edge... edges) {
        if(edges.length ==0){
            edges = focusedEdges;
        }

        for (Edge edge : edges) {
            if (edge != null) {
                displayGraph.getEdge(edge.getId()).setAttribute("ui.style", styleSheet);
            }
        }
    }

    protected static void styleNodes(String styleSheet, Node... nodes){
//        List<Node> nodes = nodesArr.length != 0 ?  Arrays.asList(nodesArr) :
//                new ArrayList<>(){{add(focusedDrive.getKey());}};
        if(nodes.length == 0){
            focusedCar.setAttribute("ui.style", styleSheet);
        }else for (Node node : nodes) {
            if (node != null) {
                node.setAttribute("ui.style", styleSheet);
            }
        }
    }



    /** STYLE UTILS */
    protected static String extractAttribute(String style, Node displayNode){
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
        String styleValue ="";
        for (String s : styleValueList) {
            styleValue = styleValue + s;
        }
        return styleValue;
    }

    private static final Random rand = new Random(1);
    protected static String randomGradientColor(){
        int r = rand.nextInt(256), g = rand.nextInt(256), b = rand.nextInt(256);
        return "  fill-mode: dyn-plain; " +
                " fill-color: rgb(" + r +"," + g + "," + b + "), white;" +
                " fill-mode: gradient-horizontal;";
    }


    /** STYLE-SHEETS */
    static {
        /*     |==== NODE ====|   */

        nodeStyleSheet = "text-mode: hidden;" +
                "z-index: 1;" +
                "fill-color: grey;" +
                "size: 3px;";

        pedestrianStyleSheet =
                "z-index: 2;" +
                "size: 20px;" +
                "text-mode: normal;" +
                "text-style: bold;" +
                "stroke-mode: plain;" +
                "stroke-color: black;" +
                "stroke-width: 1px;" +
                        " shape: diamond;";

//                " fill-mode: image-scaled; " +
//                " fill-image: url(DATA_PATH +"assets/pedestrianIcon.png');";

        focusedCarStyleSheet = "size: 25px;";

        carStyleSheet =
                "z-index: 2;" +
                "size: 15px;" +
                        "text-mode: normal;" +
                        "text-style: bold;" +
                        "stroke-mode: plain;" +
                        "stroke-color: black;" +
                        "stroke-width: 1px;";
//                            + " fill-mode: image-scaled; "
//                            + " fill-image: url('data/assets/car"+ (a+1) +".png');"
//                " shape: rounded-box;"+
//                "icon-mode: none;" +
//                "fill-mode: image-scaled; "+
//                " fill-image: url('data/assets/smallCar.png');"+


        /*     |==== EDGE ====|     */

        edgeStyleSheet = "fill-color: black;" +
                "size: 1px;" ;

        pathStyleSheet = "size: 4px;";

        motorwayEdgeStyleSheet ="fill-color: rgb(50, 50, 255);" +
                "size: 2px;";
        tertiaryEdgeStyleSheet =
                "fill-color: rgb(50, 50, 200);" +
                        "size: 1px;";

        secondaryEdgeStyleSheet ="fill-color: rgb(50, 50, 150);" +
                "size: 1.25px;";

        primaryEdgeStyleSheet ="" +
                "fill-color: rgb(50, 50, 100);" +
                "size: 1.5px;";



        /*     |==== SPRITE ====|   */
        spriteStyleSheet = """
                """;


        styleSheet =
                "node {" + nodeStyleSheet + "}" +
                "edge{" + edgeStyleSheet + "}" +
                "node.car {" + carStyleSheet + "}" +
                "node.pedestrian {" + pedestrianStyleSheet + "}";

    }

//    static {
//        /*     |==== NODE ====|   */
//
//        nodeStyleSheet = """
//                    text-mode: hidden;
//                    z-index: 1;
//                    fill-color: grey;
//                    size: 3px;
//                """;
//
//        passengerStyleSheet = """
//                    fill-color: red;
//                    size: 10px;
//                """;
//
//        carStyleSheet ="""
//                    z-index: 2;
//                    size: 20px;
//                    text-mode: normal;
//                    text-style: bold;
//                    stroke-mode: plain;
//                    stroke-color: black;
//                    stroke-width: 1px;
//                """;
////                            + " fill-mode: image-scaled; "
////                            + " fill-image: url('data/assets/car"+ (a+1) +".png');"
////                " shape: rounded-box;"+
////                "icon-mode: none;" +
////                "fill-mode: image-scaled; "+
////                " fill-image: url('data/assets/smallCar.png');"+
//
//
//        /*     |==== EDGE ====|     */
//
//        edgeStyleSheet = """
//                    fill-color: black;
//                    size: 1px;
//                """
//                ;
//
//        pathStyleSheet = """
//                size: 4px;
//                """;
//
//        motorwayEdgeStyleSheet ="""
//                    fill-color: rgb(50, 50, 255);
//                    size: 2px;
//                """;
//        tertiaryEdgeStyleSheet = """
//                    fill-color: rgb(50, 50, 200);
//                    size: 1px;
//                """;
//
//        secondaryEdgeStyleSheet ="""
//                    fill-color: rgb(50, 50, 150);
//                    size: 1.25px;
//                """;
//
//        primaryEdgeStyleSheet ="""
//                    fill-color: rgb(50, 50, 100);
//                    size: 1.5px;
//                """;
//
//
//
//        /*     |==== SPRITE ====|   */
//        spriteStyleSheet = """
//                """;
//
//
//        styleSheet =
//                "node {" + nodeStyleSheet + "}" +
//                "edge{" + edgeStyleSheet + "}" +
//                "node.car {" + carStyleSheet + "}" +
//                "node.passenger {" + passengerStyleSheet + "}";
//    }

}






















//    private void resetNodeStyle() {
//        if(focusedNode != null) {
//            styleNodes(nodeStyleSheet);
//        }
//    }
//    private static Hashtable<String, String> styles = new Hashtable<>(){{
//        put("graph", styleSheet);
//        put("node", nodeStyleSheet);
//        put("edge", edgeStyleSheet);
//        put("sprite", spriteStyleSheet);
//        put("car", carStyleSheet);
//        put("passenger", passengerStyleSheet);
//        put("secondary", secondaryStyleSheet);
//        put("primary", primaryStyleSheet);
//        put("motorway", motorwayStyleSheet);
//    }};

//    protected static String getAttributes(String styleClass){
//        String attributes;
//        switch(styleClass){
//            case "graph" -> attributes = styleSheet;
//            case "node" -> attributes =  nodeStyleSheet;
//            case "edge" -> attributes =  edgeStyleSheet;
//            case "sprite" -> attributes =  spriteStyleSheet;
//            case "car" -> attributes = carStyleSheet + randomGradientColor();
//            case "passenger" -> attributes =  passengerStyleSheet;
//            case "motorway" -> attributes =  motorwayEdgeStyleSheet;
//            case "primary" -> attributes =  primaryEdgeStyleSheet;
//            case "secondary" -> attributes =  secondaryEdgeStyleSheet;
//            case "tertiary" -> attributes =  tertiaryEdgeStyleSheet;
//            default -> attributes = "";
//        }
//
//        return attributes;
//    }
