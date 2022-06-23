package view;

import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.util.DefaultMouseManager;
import org.graphstream.ui.view.View;

import model.RoadMap;
import model.Node;


import java.awt.event.MouseEvent;
import java.util.Random;

public class CustomMouseManager extends DefaultMouseManager {
    protected View view;
    protected GraphicGraph displayGraph;
    private GraphicElement focusedNode;



    @Override
    public void init(GraphicGraph displayGraph, View view) {
        super.init(displayGraph, view);
        this.displayGraph = displayGraph;
        this.view = view;
        view.addMouseListener(this);
        view.addMouseMotionListener(this);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        GraphicElement currentNode = view.findNodeOrSpriteAt(e.getX(), e.getY());

        if(currentNode != null && currentNode != focusedNode){
            RoadMap map = RoadMap.getInstance();
            Node node = map.getNode(Long.parseLong(currentNode.getLabel()));
            if(node == null) {
                System.out.println("removed node " + currentNode.getLabel());
            }else{
                if (node.getType() == Node.userType.Passenger) {
                    currentNode.setAttribute("ui.style", "size: 10px, 10px; text-mode: normal;");
                } else {
                    Random r = new Random();
                    currentNode.setAttribute("ui.style", "fill-color: rgb(" + r.nextInt(256) + "," + r.nextInt(256) + "," + r.nextInt(256) + ");size: 10px, 10px; text-mode: normal;");
                    System.out.println(node);
                }
            }
//            MapView.getInstance().setDest(currentNode.getLabel());

        }else{
//            System.out.println(e.getX()+", "+e.getY());
        }

        if( focusedNode!= null && focusedNode != currentNode)
            focusedNode.setAttribute("ui.style", "fill-color: black;size: 10px, 10px; text-mode: hidden;");

        focusedNode = currentNode;
    }
}
