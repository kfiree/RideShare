//package Gui;
//
//import org.graphstream.ui.graphicGraph.GraphicElement;
//import org.graphstream.ui.graphicGraph.GraphicGraph;
//import org.graphstream.ui.swing_viewer.util.DefaultMouseManager;
//import org.graphstream.ui.view.View;
//
//import org.graphstream.ui.view.util.InteractiveElement;
//import osmProcessing.OGraph;
//import osmProcessing.ONode;
//
//
//import java.awt.event.MouseEvent;
//import java.util.EnumSet;
//import java.util.Random;
//
//public class CustomMouseManager2 extends DefaultMouseManager {
//    protected View view;
//    protected GraphicGraph graph;
//    private GraphicElement focusedNode;
//    final private EnumSet<InteractiveElement> types;
//
//    public CustomMouseManager2(EnumSet<InteractiveElement> types) { this.types = types;}
//    public CustomMouseManager2(){ this(EnumSet.of(InteractiveElement.NODE,InteractiveElement.SPRITE)); }
//
//
////    @Override
////    public void init(GraphicGraph graph, View view) {
////        super.init(graph, view);
////        this.graph = graph;
////        this.view = view;
////        view.addListener("Mouse", this);
////        view.addListener("MouseMotion", this);
////    }
//
//
////    @Override
////    public void mouseClicked(MouseEvent e) {
////        super.mouseClicked(e);
////        GraphicElement currentNode = view.findNodeOrSpriteAt(e.getX(), e.getY());
////        System.out.println(e.getX()+", "+e.getY()+ ", node:" + currentNode.label);
////
////    }
//
//    @Override
//    public void mouseReleased(MouseEvent e) {
//        super.mouseReleased(e);
//        GraphicElement currentNode = view.findGraphicElementAt(types, e.getX(), e.getY());
////        GraphicElement currentNode = view.findNodeOrSpriteAt(e.getX(), e.getY());
//
//        if(currentNode != null){
//            OGraph graph = OGraph.getInstance();
//
//            Random r = new Random();
//            currentNode.setAttribute("ui.style", "fill-color: rgb("+r.nextInt(256)+","+r.nextInt(256)+","+r.nextInt(256)+");size: 10px, 10px; text-mode: normal;");
//
//            ONode oNode = graph.getNode(Long.parseLong(currentNode.getLabel()));
//            System.out.println(oNode.toString());
//
//        }
//
//        if( focusedNode!= null && focusedNode != currentNode)
//            focusedNode.setAttribute("ui.style", "fill-color: black;size: 10px, 10px; text-mode: hidden;");
//
//        focusedNode = currentNode;
//    }
//
////    @Override
////    public void mouseReleased(MouseEvent e) {
////        super.mouseReleased(e);
////        GraphicElement currentNode = view.findNodeOrSpriteAt(e.getX(), e.getY());
////
////        if(currentNode != null){
////            OGraph graph = OGraph.getInstance();
////
////            Random r = new Random();
////            currentNode.setAttribute("ui.style", "fill-color: rgb("+r.nextInt(256)+","+r.nextInt(256)+","+r.nextInt(256)+");size: 10px, 10px; text-mode: normal;");
////            ONode oNode = graph.getNode(Long.parseLong(currentNode.getLabel()));
////            System.out.println(oNode.toString());
////
////        }
////
////        if(focusedNode!= null)
////            focusedNode.setAttribute("ui.style", "fill-color: red;size: 10px, 10px; text-mode: hidden;");
////
////        focusedNode = currentNode;
////    }
//}
