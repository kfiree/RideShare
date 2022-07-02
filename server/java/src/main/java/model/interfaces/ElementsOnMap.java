package model.interfaces;

import model.GeoLocation;
import model.Node;

import java.util.Date;
//import static controller.utils.GraphAlgo.distance;
import java.util.Comparator;

public interface ElementsOnMap extends Comparable<ElementsOnMap> {
//    GeoLocation location= new GeoLocation(0.0, 0.0);

    String getId();

    GeoLocation getLocation();

    Node getDestination();

    Node getCurrNode();

    Date getStartTime();

    @Override
    default int compareTo(ElementsOnMap other){
        return (int) (getStartTime().getTime() - other.getStartTime().getTime());
    }
}
