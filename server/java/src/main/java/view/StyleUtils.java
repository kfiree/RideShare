package view;


import model.Drive;
//import model.Edge;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicGraph;

import java.text.SimpleDateFormat;
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
 *     TODO:
 *          * 6/29/2022 color highwayType
 *          * style by class type
 *          * add ui.class
 */
public class StyleUtils {

    private StyleUtils(){}


    /**     |==== NODE STYLE ====|      */
//    protected static Node focusedNode;
    protected static Map.Entry<Node, Drive> focusedDrive;
    protected static final List<Edge> focusedEdges = new ArrayList<>();
    protected static GraphicGraph displayGraph;
    protected static SimpleDateFormat dateFormatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
    protected static final String
            nodeStyleSheet, passengerStyleSheet, carStyleSheet,
            edgeStyleSheet, secondaryEdgeStyleSheet, primaryEdgeStyleSheet,
            motorwayEdgeStyleSheet, tertiaryEdgeStyleSheet, pathStyleSheet,
            spriteStyleSheet, styleSheet, focusedCarStyleSheet;


    protected static void styleDrive(Map.Entry<Node, Drive> drive) {
        resetPathStyle();

        setNewDrive(drive);

        styleEdges(pathStyleSheet + extractAttribute("fill-color", drive.getKey()));
        styleNodes(focusedCarStyleSheet, drive.getKey());
    }

    private static void setNewDrive(Map.Entry<Node, Drive> newDrive){
        focusedDrive = newDrive;
        newDrive.getValue().getEdges().forEach(edge -> focusedEdges.add(displayGraph.getEdge(edge.getId())));
    }

    private static void resetPathStyle() {
        if(focusedDrive != null){
            styleEdges(edgeStyleSheet);
            styleNodes(focusedCarStyleSheet, focusedDrive.getKey());
            focusedEdges.clear();
        }
    }

    protected static void styleEdges(String styleSheet, Edge... edgesArr) {
        List<Edge> edges = edgesArr.length != 0? Arrays.asList(edgesArr): focusedEdges;
                for (Edge edge : edges) {
            if (edge != null) {
                displayGraph.getEdge(edge.getId()).setAttribute("ui.style", styleSheet);
            }
        }
    }

    protected static void styleNodes(String styleSheet, Node... nodesArr){
        List<Node> nodes = nodesArr.length != 0 ?  Arrays.asList(nodesArr) :
                new ArrayList<>(){{add(focusedDrive.getKey());}};
        for (Node node : nodes) {
            if (node != null) {
                node.setAttribute("ui.style", styleSheet);
            }
        }

    }

    protected static String extractAttribute(String style, Node displayNode){
        String styleSheet = (String) displayNode.getAttribute("ui.style");
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

    static {
        /*     |==== NODE ====|   */

        nodeStyleSheet = "text-mode: hidden;" +
                "z-index: 1;" +
                "fill-color: grey;" +
                "size: 3px;";

        passengerStyleSheet = "fill-color: red;" +
                "size: 10px;";

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
//                            + " fill-image: url('server/java/data/assets/car"+ (a+1) +".png');"
//                " shape: rounded-box;"+
//                "icon-mode: none;" +
//                "fill-mode: image-scaled; "+
//                " fill-image: url('server/java/data/assets/smallCar.png');"+


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
                "node.passenger {" + passengerStyleSheet + "}";
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
////                            + " fill-image: url('server/java/data/assets/car"+ (a+1) +".png');"
////                " shape: rounded-box;"+
////                "icon-mode: none;" +
////                "fill-mode: image-scaled; "+
////                " fill-image: url('server/java/data/assets/smallCar.png');"+
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
