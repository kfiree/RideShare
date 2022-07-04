package rideshare.model.interfaces;

import rideshare.model.GeoLocation;
import rideshare.model.Node;
import rideshare.model.Path;

import java.util.Date;
//import static rideshare.controller.GraphAlgo.distance;


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
