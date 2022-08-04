package app.view;

import app.model.*;
import org.graphstream.graph.Node;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.util.DefaultMouseManager;

import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.util.Map;

import static app.view.StyleUtils.focusOn;


/**
 * todo add description
 */
public class MapMouseManager extends DefaultMouseManager {
    protected View view;
    protected GraphicGraph displayGraph;
    private boolean firstClicked;
    private Node node1, node2;

    private static boolean editing;
    //todo check & clean unused
    private Node focusedNode;
    private Map.Entry<Node, Drive> focusedCar;
    private Edge[] edges;



    public static void addDrive(){
        editing = true;

//        UserMap.INSTANCE.addDrive();


        editing = false;
    }

    public static void addRider(){
        editing = true;

        editing = false;
    }

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
//        KeyListener k =
        firstClicked  = !firstClicked; //prevent double click

        if(firstClicked) { //todo add min distance for paint

            GraphicElement currentNode = view.findNodeOrSpriteAt(e.getX(), e.getY());
            if(currentNode!= null){
                app.model.Node node = RoadMap.INSTANCE.getNode(Long.parseLong(currentNode.getLabel()));
                if(node != null) {
                    System.out.println(node);
                }
            }

//            System.out.println( "MouseEvent xy ("+e.getX() +","+e.getY()+"), coordinates ("
//                    +view.getCamera().transformPxToGu(e.getX() ,e.getY())+").");
//
//            if(MapView.DEBUG){
//                GraphicElement currentNode = view.findNodeOrSpriteAt(e.getX(), e.getY());
//                if(currentNode!= null){
//                    app.model.Node node = RoadMap.INSTANCE.getNode(Long.parseLong(currentNode.getLabel()));
//                    if(node != null) {
//                        System.out.println(node);
//                    }
//                }
//            }
//            Drive closestDrive = getClosestDrive(e);
//            if (closestDrive != null) {
//
//                focusOn(closestDrive);
//            }

        }
    }

    private Drive getClosestDrive(MouseEvent e){
        Point3 clickCoordinates = view.getCamera().transformPxToGu(e.getX(), e.getY());
        double minDistance = Integer.MAX_VALUE;
        Drive car = null;
        for (Drive drive: UserMap.INSTANCE.getOnGoingDrives()) {
            GeoLocation carLocation = drive.getLocation();

            double currDis = Math.sqrt(( carLocation.getLatitude() - clickCoordinates.y) * (carLocation.getLatitude() - clickCoordinates.y)
                                        + (carLocation.getLongitude() - clickCoordinates.x) * (carLocation.getLongitude() - clickCoordinates.x));

            if(currDis< minDistance){
                car = drive;
                minDistance = currDis;
            }
        }
        return car;
    }

}