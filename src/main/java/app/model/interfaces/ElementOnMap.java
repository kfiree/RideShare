package app.model.interfaces;

import app.model.GeoLocation;
import app.model.Node;

import java.util.Date;
//import static app.controller.GraphAlgo.distance;

//TODO remove "D" & "R" when loading from DB
public interface ElementOnMap extends Comparable<ElementOnMap> {
//    GeoLocation location= new GeoLocation(0.0, 0.0);
//    enum status {
//        WAITING,
//        ON_GOING,
//        FINISHED
//    }

    int getId();

    GeoLocation getLocation();

    Node getDest();

    Node getCurrentNode();

    Date getStartTime();

    Node getNextStop();

//    default status getStatus(){
//        return status.WAITING;
//    }todo add status

    @Override
    default int compareTo(ElementOnMap other){
        return (int) (getStartTime().getTime() - other.getStartTime().getTime());
    }
}
