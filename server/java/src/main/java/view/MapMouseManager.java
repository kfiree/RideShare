package view;

import model.Drive;
import model.Edge;

import static view.StyleUtils.*;
import static controller.utils.GraphAlgo.distance;

import org.graphstream.graph.Node;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.util.DefaultMouseManager;
import org.graphstream.ui.view.View;
import java.awt.event.MouseEvent;
import java.util.*;


/**
 * TODO set hovering https://stackoverflow.com/questions/70745672/get-graphicedge-at-mouse-hovering-in-graphstream/70775617#70775617
 */
public class MapMouseManager extends DefaultMouseManager {
    protected View view;
    protected GraphicGraph displayGraph;
    private MapView mapView = MapView.getInstance();
    private Node focusedNode;
    private Map.Entry<Node, Drive> focusedCar;
    private Edge[] edges;
    private boolean firstClicked;




    @Override
    public void init(GraphicGraph displayGraph, View view) {
        super.init(displayGraph, view);
        this.displayGraph = displayGraph;
        this.view = view;
        view.addMouseListener(this);
        view.addMouseMotionListener(this);
        StyleUtils.displayGraph = displayGraph;
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
        firstClicked  = !firstClicked; //prevent double click

        System.out.println( "MouseEvent xy ("+e.getX() +","+e.getY()+").");
        System.out.println(view.getCamera().transformPxToGu(e.getX() ,e.getY()));

        if(firstClicked) {

            Map.Entry<Node, Drive> closestCar = getClosestCar(e);
            if (closestCar != null) {
                styleDrive(closestCar);
            }
        }
    }

    private Map.Entry<Node, Drive> getClosestCar(MouseEvent e){
        Point3 clickCoordinates = view.getCamera().transformPxToGu(e.getX(), e.getY());
        double minDistance = Integer.MAX_VALUE;
        Map.Entry<Node, Drive> car = null;
        for (Map.Entry<Node, Drive> carEntry: mapView.cars.entrySet()) {
            Double carX = carEntry.getValue().getLocation().getLongitude();
            Double carY = carEntry.getValue().getLocation().getLatitude();

            Double currDis = Math.sqrt(( carY - clickCoordinates.y) * (carY - clickCoordinates.y)
                                        + (carX - clickCoordinates.x) * (carX - clickCoordinates.x));

            if(currDis< minDistance){
                car = carEntry;
                minDistance = currDis;
            }
        }
        return car;
    }

}