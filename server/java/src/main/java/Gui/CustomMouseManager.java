package Gui;

import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.util.DefaultMouseManager;
import org.graphstream.ui.view.View;

import osmProcessing.OGraph;
import osmProcessing.ONode;


import java.awt.event.MouseEvent;
import java.util.Random;

public class CustomMouseManager extends DefaultMouseManager {
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

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        GraphicElement currentNode = view.findNodeOrSpriteAt(e.getX(), e.getY());

        if(currentNode != null && currentNode != focusedNode){
            OGraph graph = OGraph.getInstance();
            ONode oNode = graph.getNode(Long.parseLong(currentNode.getLabel()));

            if(oNode.getUser() == ONode.userType.Rider){
                currentNode.setAttribute("ui.style", "size: 10px, 10px; text-mode: normal;");
            }else {
                Random r = new Random();
                currentNode.setAttribute("ui.style", "fill-color: rgb(" + r.nextInt(256) + "," + r.nextInt(256) + "," + r.nextInt(256) + ");size: 10px, 10px; text-mode: normal;");
                System.out.println(oNode);
            }

        }else{
//            System.out.println(e.getX()+", "+e.getY());
        }

        if( focusedNode!= null && focusedNode != currentNode)
            focusedNode.setAttribute("ui.style", "fill-color: black;size: 10px, 10px; text-mode: hidden;");

        focusedNode = currentNode;
    }
}
