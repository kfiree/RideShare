package model.interfaces;

import model.GeoLocation;
import model.Node;

import java.util.Date;
//import static controller.utils.GraphAlgo.distance;


public interface ElementsOnMap extends Comparable<ElementsOnMap> {
//    GeoLocation location= new GeoLocation(0.0, 0.0);

    String getId();

    GeoLocation getLocation();

    Node getDestination();

    Node getCurrNode();

    Date getAskTime();

    @Override
    default int compareTo(ElementsOnMap other){
        return (int) (getAskTime().getTime() - other.getAskTime().getTime());
    }
}
