package Gui;

import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.util.DefaultMouseManager;
import osmProcessing.OGraph;
import osmProcessing.ONode;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

public class CustomMouseManager2 extends DefaultMouseManager {
    protected View view;
    protected GraphicGraph graph;
    private GraphicElement focusedNode;

    @Override
    public void init(GraphicGraph graph, View view) {
        super.init(graph, view);
        this.graph = graph;
        this.view = view;
        view.addMouseListener(this);
        view.addMouseMotionListener(this);
    }


//    @Override
//    public void mouseClicked(MouseEvent e) {
//        super.mouseClicked(e);
//        GraphicElement currentNode = view.findNodeOrSpriteAt(e.getX(), e.getY());
//        System.out.println(e.getX()+", "+e.getY()+ ", node:" + currentNode.label);
//
//    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        GraphicElement currentNode = view.findNodeOrSpriteAt(e.getX(), e.getY());

        if(currentNode != null){
            OGraph graph = OGraph.getInstance();

            Random r = new Random();
            currentNode.setAttribute("ui.style", "fill-color: rgb("+r.nextInt(256)+","+r.nextInt(256)+","+r.nextInt(256)+");size: 10px, 10px; text-mode: normal;");

            ONode oNode = graph.getNode(Long.parseLong(currentNode.getLabel()));
            System.out.println(oNode.toString());

        }

        if( focusedNode!= null && focusedNode != currentNode)
            focusedNode.setAttribute("ui.style", "fill-color: black;size: 10px, 10px; text-mode: hidden;");

        focusedNode = currentNode;
    }

//    @Override
//    public void mouseReleased(MouseEvent e) {
//        super.mouseReleased(e);
//        GraphicElement currentNode = view.findNodeOrSpriteAt(e.getX(), e.getY());
//
//        if(currentNode != null){
//            OGraph graph = OGraph.getInstance();
//
//            Random r = new Random();
//            currentNode.setAttribute("ui.style", "fill-color: rgb("+r.nextInt(256)+","+r.nextInt(256)+","+r.nextInt(256)+");size: 10px, 10px; text-mode: normal;");
//            ONode oNode = graph.getNode(Long.parseLong(currentNode.getLabel()));
//            System.out.println(oNode.toString());
//
//        }
//
//        if(focusedNode!= null)
//            focusedNode.setAttribute("ui.style", "fill-color: red;size: 10px, 10px; text-mode: hidden;");
//
//        focusedNode = currentNode;
//    }
}
