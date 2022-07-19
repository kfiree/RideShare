package app.view;

import app.model.Drive;
import app.model.Edge;
import app.model.GeoLocation;
import app.model.UserMap;
import org.graphstream.graph.Node;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.util.DefaultMouseManager;

import java.awt.event.MouseEvent;
import java.util.Map;

import static app.view.StyleUtils.focusOn;


/**
 * todo add description
 */
public class MapMouseManager extends DefaultMouseManager {
    protected View view;
    protected GraphicGraph displayGraph;
    private boolean firstClicked;

    //todo check & clean unused
    private Node focusedNode;
    private Map.Entry<Node, Drive> focusedCar;
    private Edge[] edges;




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