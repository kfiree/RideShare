package app.view;


import app.model.Drive;
import app.model.GeoLocation;
import app.model.UserMap;
import app.model.interfaces.ElementsOnMap;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import static app.view.MapView.displayGraph;
import static app.view.MapView.elementsOnMap;
import static utils.LogHandler.LOGGER;
import static utils.Utils.validate;

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
 *     "jcomponent" ... todo check this!
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


    /*     |==== NODE STYLE ====|      */
    protected static boolean showAllPaths;
    protected static Node focusedCar;
    protected static Drive focusedDrive;
    protected static final HashMap<String, Edge[]> carPaths = new HashMap<>();
    protected static Edge[] focusedPath;
    protected static final String
            nodeStyleSheet, riderStyleSheet, carStyleSheet,
            edgeStyleSheet, secondaryEdgeStyleSheet, primaryEdgeStyleSheet,
            motorwayEdgeStyleSheet, tertiaryEdgeStyleSheet, pathStyleSheet,
            spriteStyleSheet, styleSheet, focusedCarStyleSheet;



    /* STYLE USERS DATA */

    protected static synchronized void styleDrives(Drive... drives) {
        for(Drive drive: drives){
            Edge[] currPath = carPaths.get(drive.getId());
            /* if path changed */
            if (currPath != null && currPath.length != drive.getNodes().size()) {
                /* clean old path style */
                styleEdges(edgeStyleSheet, currPath);
                /* update path and paint */
                StylePath(drive);
            } else if (currPath == null) {

                /* add path and paint */
                StylePath(drive);
            }
        }
    }

    private static synchronized void StylePath(Drive drive){
        if(drive !=null) {
            Node car = focusedCar;
            Edge[] edges = focusedPath;
            String color = "";

            if(drive != focusedDrive){
                car = elementsOnMap.get(drive);

                if(validate(car != null, "drive "+drive.getId()+"'s display node is null")){
                    int pathSize = drive.getNodes().size();
                    edges = new Edge[pathSize];
                    for (int i = 0; i < pathSize; i++) {
                        edges[i] = displayGraph.getEdge(drive.getNodes().get(i).getId());
                    }
                    carPaths.put(drive.getId(), edges);
                    color = extractAttribute("fill-color", car);
                }
            }else if(validate(car != null, "drive "+drive.getId()+"'s display node is null")){
                color = extractAttribute("fill-color", car);
            }



            if (edges != null) {
                styleEdges(pathStyleSheet + color,edges);
            }

            String finalColor = color;
            drive.getPassengers().forEach(passenger ->
                    styleNodes(finalColor, displayGraph.getNode(passenger.getId())));

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
            int pathSize = drive.getNodes().size();
            focusedPath = new Edge[pathSize];
            for (int i = 0; i < pathSize-1; i++) {
                focusedPath[i] = displayGraph.getEdge(
                        drive.getNodes().get(i).getEdgeTo(drive.getNodes().get(i+1)).getId());

                        //displayGraph.getEdge(drive.getNodes().get(i).getId());

            }

            focusedDrive = drive;
            focusedCar = displayGraph.getNode(drive.getId());

            /* style new drive */
            styleFocusedDrive(drive);
        }
    }

    protected static void drawElement(ElementsOnMap element, Node node, String styleClass) {
        GeoLocation location = element.getLocation();
        node.setAttribute("xy", location.getLongitude(), location.getLatitude());
        node.setAttribute("ui.label", element.getId().substring(1));
        node.setAttribute("ui.class", styleClass);
    }


    protected static void moveCar(Drive drive, Node car){
        GeoLocation location = drive.getLocation();

        if(location != null){
            car.setAttribute("xy", location.getLongitude(), location.getLatitude());
        }else{
            LOGGER.info(UserMap.INSTANCE.getDrives().size() + " On-Going drives.");
            displayGraph.removeNode(drive.getId());
        }

        if (showAllPaths) {
            styleDrives(drive);
        } else {
            styleFocusedDrive(drive);
        }
    }

    /* STYLE MAP PARTS */

    protected static void styleEdges(String styleSheet, Edge... edges) {
        if(edges.length ==0){
            edges = focusedPath;
        }

        for (Edge edge : edges) {
            if (edge != null) {
                displayGraph.getEdge(edge.getId()).setAttribute("ui.style", styleSheet);
            }
        }
    }

    protected static void styleNodes(String styleSheet, Node... nodes){
        if(nodes.length == 0){
            focusedCar.setAttribute("ui.style", styleSheet);
        }else for (Node node : nodes) {
            if (node != null) {
                node.setAttribute("ui.style", styleSheet);
            }
        }
    }



    /* STYLE UTILS */

    protected static String extractAttribute(String style, @NotNull Node displayNode){
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
    protected static String randomGradientColor(){
        int r = rand.nextInt(256), g = rand.nextInt(256), b = rand.nextInt(256);
        return "  fill-mode: dyn-plain; " +
                " fill-color: rgb(" + r +"," + g + "," + b + "), white;" +
                " fill-mode: gradient-horizontal;";
    }

    public static Sprite drawClock(){
        Node clockNode = displayGraph.addNode("clockNode");

        clockNode.setAttribute("xy", 34.7615, 32.1179); //todo locate relative to map

        SpriteManager spriteManager = new SpriteManager(displayGraph);
        Sprite clock = spriteManager.addSprite("clock");
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
        return clock;
    }

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
                "node.rider {" + riderStyleSheet + "}";

    }

}