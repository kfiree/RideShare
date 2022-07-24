package app.model.interfaces;

import app.model.GeoLocation;
import app.model.Node;
import app.model.Path;

import java.util.Date;
//import static app.controller.GraphAlgo.distance;

//TODO remove "D" & "R" when loading from DB
public interface ElementsOnMap extends Comparable<ElementsOnMap> {
//    GeoLocation location= new GeoLocation(0.0, 0.0);
//    enum status {
//        WAITING,
//        ON_GOING,
//        FINISHED
//    }

    String getId();

    GeoLocation getLocation();

    Node getDest();

    Node getCurrentNode();

    Date getStartTime();

//    default status getStatus(){
//        return status.WAITING;
//    }todo add status

    @Override
    default int compareTo(ElementsOnMap other){
        return (int) (getStartTime().getTime() - other.getStartTime().getTime());
    }
}
