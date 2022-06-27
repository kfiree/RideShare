package view;

import model.Drive;
import model.Edge;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.util.DefaultMouseManager;
import org.graphstream.ui.view.View;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Optional;

public class CustomMouseManager extends DefaultMouseManager {
    protected View view;
    protected GraphicGraph displayGraph;
    private GraphicElement focusedNode;
    private GraphicElement focusedCar;
    private Map.Entry<Node, Drive> focusedPath;
    private MapView mapView = MapView.getInstance();



    @Override
    public void init(GraphicGraph displayGraph, View view) {
        super.init(displayGraph, view);
        this.displayGraph = displayGraph;
        this.view = view;
        view.addMouseListener(this);
        view.addMouseMotionListener(this);
    }

    private Optional<Map.Entry<Node, Drive>> getCar(GraphicElement graphicElement){
        return mapView.cars.entrySet().stream()
                .filter(entry -> entry.getKey().getId().equals(graphicElement.getLabel()))
                .findFirst();
    }

    /**
     * TODO
     *      * check if 2 nodes clicked at once
     *      * paint node data
     *      * clear path patin
     *
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        resetStyle();

//        Optional.ofNullable(view.findNodeOrSpriteAt(e.getX(), e.getY()))
//                .ifPresent(graphicElement ->{ // click on graphicElement
//                    getCar(graphicElement)
//                            .filter(car-> car != focusedCar)// != focusedCar
//                            .isPresent((GraphicElement)car->{
//                                Optional.ofNullable(car.style.getFillColor(0))
//                                        .ifPresent(color -> paintPath(displayNode, focusedPath.getValue()));
//                                    });
////                    if(graphicElement != focusedCar){ // not equal focusedCar
////                        getCar(graphicElement).filter(car-> car != focusedCar)
////                                .isPresent(car->{
////
////                        });
////                        Optional<Map.Entry<Node, Drive>> car = getCar(graphicElement);
////                        if(focusedPath != car.get()){
////
////                        }
////
////                    }
//                });
//TODO continue improve

        GraphicElement displayNode = view.findNodeOrSpriteAt(e.getX(), e.getY());
//        Optional<GraphicElement> graphicElement =
        Optional.ofNullable(view.findNodeOrSpriteAt(e.getX(), e.getY()))
                .ifPresent( graphicElement->{// if clicked on graphicElement
                    if(graphicElement != focusedCar){
                        mapView.cars.entrySet().stream()
                                .filter(entry -> entry.getKey().getId().equals(graphicElement.getLabel()))
                                .findFirst().ifPresent( driveEntry -> {
                                    System.out.println(driveEntry.getValue());
                                    resetStyle();
                                    focusedPath = driveEntry;

                                    Optional.ofNullable(graphicElement.style.getFillColor(0))
                                            .ifPresent(color -> paintPath(graphicElement, focusedPath.getValue()));
                                });
                    }
//                    else if(graphicElement != focusedNode){
//                        resetNodeStyle();
//                focusedNode =graphicElement;
//                focusedNode.setAttribute("ui.style", "fill-color: rgb(" + r.nextInt(256) + "," + r.nextInt(256) + "," + r.nextInt(256) + ");size: 10px; text-mode: normal;");
            });

        }
//        if(displayNode!= null){
//
//            if(displayNode != focusedNode){
//
//            }

//            if(displayNode != focusedCar){
//                Optional<Map.Entry<Node, Drive>> driveEntry = mapView.cars.entrySet().stream()
//                        .filter(entry -> entry.getKey().getId().equals(displayNode.getLabel()))
//                        .findFirst();
//            }
//            Optional<Map.Entry<Node, Drive>> driveEntry = mapView.cars.entrySet().stream()
//                    .filter(entry -> entry.getKey().getId().equals(graphicElement.getLabel()))
//                    .findFirst();

//            if(driveEntry.isPresent() && focusedPath != driveEntry.get()){
//                resetStyle();
//
//                focusedPath = driveEntry.get();
//
//                Optional.ofNullable(graphicElement.style.getFillColor(0))
//                        .ifPresent(color -> paintPath(graphicElement, focusedPath.getValue()));
//            }else if(focusedPath != null){
//                resetStyle();
//                focusedPath = null;
//            }
//                    .ifPresent(driveEntry -> {
//                                focusedPath = driveEntry.getValue().getEdges();
//                                Optional.ofNullable(displayNode.style.getFillColor(0))
//                                        .ifPresent(color -> {
//                                                    colorEdges(color, 3, focusedPath.toArray(new Edge[0]));
//                                                    colorNodes(color, 20, displayNode);
////                                                    displayNode.setAttribute("ui.style", "size: 20px");
//                                                }
//                                        );

//        ===================================================================================================
//                       paintPath(displayNode, driveEntry.getValue());
// class org.graphstream.ui.graphicGraph.stylesheet.Colors
// cannot be cast to class java.awt.Color (org.graphstream.ui.graphicGraph.stylesheet.Colors is in unnamed module of loader 'app'; java.awt.Color is in module java.desktop of loader 'bootstrap')

//            tableDescriptions.getAllSchemaTableNames()
//                .stream()
//                .filter(schemaTableName -> !schemaName.isPresent() || schemaTableName.getSchemaName().equals(schemaName.get()))
//                .collect(toImmutableList());

//                =====================================================================================================================
//                String attribute = displayNode.getAttribute("ui.style");
//                StyleGroup style = displayNode.style;
//
//                String color = attribute.split("fill-color:")[1].split(";")[0];
//                System.out.println("================================"+color+"================================");
//                nodeDriveEntry.get().getValue().getEdges().forEach(edge->{
//                    displayGraph.getEdge(edge.getId()).setAttribute("ui.style", "fill-color: "+ color + ";");
//                    System.out.println("ui.style \" , \" fill-color: "+ color + ";");
//                });
//                =====================================================================================================================


//                List<Edge> edges = drive.getEdges();
//                Object attribute = displayNode.getAttribute("ui.style.fill-color");
//                displayNode.setAttribute("ui.style", "fill-color: rgb(" + r.nextInt(256) + "," + r.nextInt(256) + "," + r.nextInt(256) + ");size: 10px, 10px; text-mode: normal;");
//                System.out.println(node);

//                    }
//            );
////            MapView.getInstance().setDest(currentNode.getLabel());
//
//        }else{
////            System.out.println(e.getX()+", "+e.getY());
//        }
//
//        if( focusedNode!= null && focusedNode != displayNode)
//            focusedNode.setAttribute("ui.style", "fill-color: black;size: 10px, 10px; text-mode: hidden;");
//
//        focusedNode = displayNode;
//    }
    private void resetStyle() {
        resetNodeStyle();
        resetPathStyle();
    }

    private void resetNodeStyle() {
        if(focusedNode != null) {
            focusedNode.setAttribute("ui.style", " size: 1px; text-mode: hidden;");
        }
    }
    private void resetPathStyle() {

        if(focusedCar != null) {
            focusedCar.setAttribute("ui.style", " size: 10px; text-mode: hidden;");
        }
        if(focusedPath != null) {
            for (Edge edge : focusedPath.getValue().getEdges()) {
                displayGraph.getEdge(edge.getId()).removeAttribute("ui.style");
            }
        }
    }

    private void paintPath(GraphicElement displayNode, Drive drive){
        Optional.ofNullable(displayNode.style.getFillColor(0))
                .ifPresent(color -> {
                    colorEdges(color, 3, drive.getEdges().toArray(new Edge[0]));
                    colorNodes(color, 20, displayNode);
                            displayNode.setAttribute("ui.style", "size: 20px;");
                        }
                );
    }

    private void colorEdges(Color color, int size, Edge... edges){
        for(Edge edge : edges) {
            displayGraph.getEdge(edge.getId())
                    .setAttribute(
                            "ui.style",
                            "fill-color: rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ");" +
                                    "size: " + size + "px;"
                    );
        }
    }

    private void colorNodes(Color color, int size, GraphicElement... nodes){
        for(GraphicElement node : nodes) {
            node.setAttribute(
                    "ui.style",
                    "fill-color: rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ");" +
                            "size: " + size + "px;"
            );
        }
    }


}
