package road_map.model.utils;

//import simulator.model.utils.GraphAlgo;
import road_map.RoadMapHandler;
import road_map.Utils;

public interface Located {

    Coordinates getCoordinates();

    default boolean inBound() {
        return RoadMapHandler.inBound(getCoordinates());
    }

    default double distanceTo(Located other){
        return Utils.distance(getCoordinates(), other.getCoordinates());
    }

    @Override
    boolean equals(Object o);

    @Override
    String toString();
}