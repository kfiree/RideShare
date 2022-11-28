package road_map.model.utils;

//import simulator.model.utils.GraphAlgo;
import road_map.RoadMapHandler;
import road_map.SimulatorUtils;

public interface Located {

    Coordinates getCoordinates();

    default boolean inBound() {
        return RoadMapHandler.inBound(getCoordinates());
    }

    default double distanceTo(Located other){
        return SimulatorUtils.distance(getCoordinates(), other.getCoordinates());
    }

    @Override
    boolean equals(Object o);

    @Override
    String toString();
}