//package Gui;
//
//import org.graphstream.ui.graphicGraph.GraphicElement;
//import org.graphstream.ui.graphicGraph.GraphicGraph;
//import org.graphstream.ui.swing_viewer.util.DefaultMouseManager;
//import org.graphstream.ui.view.View;
//import org.graphstream.ui.view.util.InteractiveElement;
//import org.graphstream.ui.view.util.MouseManager;
//
//import osmProcessing.OGraph;
//import osmProcessing.ONode;
//
//import java.awt.event.MouseEvent;
//import java.util.EnumSet;
//import java.util.Random;
//
//public class CustomMouseManager extends DefaultMouseManager {
////    TODO inherit from default mouse manager
//
//    protected View view;
//    protected GraphicGraph graph;
//    private GraphicElement focusedNode;
//    final private EnumSet<InteractiveElement> types;
//
//    public CustomMouseManager() {
//        this(EnumSet.of(InteractiveElement.NODE,InteractiveElement.SPRITE));
//    }
//
//    public CustomMouseManager(EnumSet<InteractiveElement> types) {
//        this.types = types;
//    }
//
//    @Override
//    public void init(GraphicGraph graph, View view) {
//        this.graph = graph;
//        this.view = view;
////        view.addMouseListener(this);
////        view.addMouseMotionListener(this);
//        view.addListener("Mouse", this);
//        view.addListener("MouseMotion", this);
//    }
//
//    @Override
//    public void release() {
//        view.removeListener("Mouse", this);
//        view.removeListener("MouseMotion", this);
//
//    }
//
//    @Override
//    public void mouseClicked(MouseEvent e) {
//
//        GraphicElement currentNode = view.findGraphicElementAt(types, e.getX(), e.getY());
//
//        if(currentNode != null){
//            OGraph graph = OGraph.getInstance();
//
////            System.out.println("Node " + node.getId() + ": (" + e.getX() + "," + e.getY() + ")");
//            Random r = new Random();
//            currentNode.setAttribute("ui.style", "fill-color: rgb("+r.nextInt(256)+","+r.nextInt(256)+","+r.nextInt(256)+");size: 10px, 10px; text-mode: normal;");
////             "node {	text-mode: normal; }";
//            ONode oNode = graph.getNode(Long.parseLong(currentNode.getLabel()));
//            System.out.println(oNode.toString());
//
//        }else{
////            System.out.println("(" + e.getX() + "," + e.getY() + ")");
//        }
//        if(focusedNode!= null)
//            focusedNode.setAttribute("ui.style", "fill-color: black;size: 10px, 10px; text-mode: hidden;");
//
//        focusedNode = currentNode;
//
//    }
//
//    @Override
//    public void mousePressed(MouseEvent e) {}
//
//    @Override
//    public void mouseReleased(MouseEvent e) {}
//
//    @Override
//    public void mouseEntered(MouseEvent e) {}
//
//    @Override
//    public void mouseExited(MouseEvent e) {}
//
//    @Override
//    public void mouseDragged(MouseEvent e) {}
//
//    @Override
//    public void mouseMoved(MouseEvent e) {}
//}