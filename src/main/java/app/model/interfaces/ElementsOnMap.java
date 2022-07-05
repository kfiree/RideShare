package app.model.interfaces;

import app.model.GeoLocation;
import app.model.Node;
import app.model.Path;

import java.util.Date;
//import static app.controller.GraphAlgo.distance;


public interface ElementsOnMap extends Comparable<ElementsOnMap> {
//    GeoLocation location= new GeoLocation(0.0, 0.0);

    String getId();

    GeoLocation getLocation();

    Node getDestination();

    Path getPath();

    Node getCurrNode();

    Date getStartTime();

    @Override
    default int compareTo(ElementsOnMap other){
        return (int) (getStartTime().getTime() - other.getStartTime().getTime());
    }
}
