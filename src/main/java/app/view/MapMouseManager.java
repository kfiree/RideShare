package app.view;

import app.model.Drive;
import app.model.Edge;

import static app.view.StyleUtils.*;
import static app.controller.GraphAlgo.distance;

import app.model.GeoLocation;
import app.model.UserMap;
import org.graphstream.graph.Node;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.util.DefaultMouseManager;
import org.graphstream.ui.view.View;
import java.awt.event.MouseEvent;
import java.util.*;


/**
 *
 */
public class MapMouseManager extends DefaultMouseManager {
    protected View view;
    protected GraphicGraph displayGraph;
    private MapView mapView = MapView.instance;
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

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        firstClicked  = !firstClicked; //prevent double click

        if(firstClicked) { //todo add min distance for paint
            System.out.println( "MouseEvent xy ("+e.getX() +","+e.getY()+"), coordinates ("
                    +view.getCamera().transformPxToGu(e.getX() ,e.getY())+").");

            Drive closestDrive = getClosestDrive(e);
            if (closestDrive != null) {

                focusOn(closestDrive);
            }

        }
    }

    private Drive getClosestDrive(MouseEvent e){
        Point3 clickCoordinates = view.getCamera().transformPxToGu(e.getX(), e.getY());
        double minDistance = Integer.MAX_VALUE;
        Drive car = null;
        for (Drive drive: UserMap.INSTANCE.getDrives()) {
            GeoLocation carLocation = drive.getLocation();
//            Double carX = driveEntry.getValue().getLocation();
//            Double carY = driveEntry.getValue().getLocation().getLatitude();

            Double currDis = Math.sqrt(( carLocation.getLatitude() - clickCoordinates.y) * (carLocation.getLatitude() - clickCoordinates.y)
                                        + (carLocation.getLongitude() - clickCoordinates.x) * (carLocation.getLongitude() - clickCoordinates.x));

            if(currDis< minDistance){
                car = drive;
                minDistance = currDis;
            }
        }
        return car;
    }

}