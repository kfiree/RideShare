package simulator.view.utils;

import simulator.model.graph.RoadMap;
import simulator.model.users.Driver;
import simulator.model.users.UserMap;
import simulator.model.graph.utils.Coordinates;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.util.DefaultMouseManager;
import simulator.model.graph.Node;

import javax.swing.*;
import java.awt.event.MouseEvent;

import static simulator.view.utils.Style.stylePath;


/**
 * todo add description
 */
public class MapMouseManager extends DefaultMouseManager {
    protected View view;
    protected GraphicGraph displayGraph;
    private boolean firstClicked;


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
            System.out.println("================== left click ======================");
            if(SwingUtilities.isLeftMouseButton(e)){
                GraphicElement currentNode = view.findNodeOrSpriteAt(e.getX(), e.getY());
                if(currentNode!= null){
                    Node node = RoadMap.INSTANCE.getNode(Long.parseLong(currentNode.getLabel()));
                    if(node != null) {
                        System.out.println(node);
                    }
                }
            }else{
                System.out.println("#################### right click ####################");
                Driver closestDrive = getClosestDrive(e);
                if (closestDrive != null) {
                    stylePath(closestDrive);
//                    focusOn(closestDrive);
                }

            }
        }
    }

    private Driver getClosestDrive(MouseEvent e){
        Point3 clickCoordinates = view.getCamera().transformPxToGu(e.getX(), e.getY());
        double minDistance = Integer.MAX_VALUE;
        Driver car = null;
        for (Driver drive: UserMap.INSTANCE.getLiveDrives()) {
            Coordinates carLocation = drive.getCoordinates();

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