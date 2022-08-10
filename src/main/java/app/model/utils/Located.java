package app.model.utils;

import app.controller.GraphAlgo;
import app.controller.RoadMapHandler;
import app.model.users.User;
import app.model.utils.Coordinates;

public interface Located {

    Coordinates getCoordinates();

    default boolean inBound() {
        return RoadMapHandler.inBound(getCoordinates());
    }

    default double distanceTo(Located other){
        return GraphAlgo.distance(getCoordinates(), other.getCoordinates());
    }

    @Override
    boolean equals(Object o);

    @Override
    String toString();
}