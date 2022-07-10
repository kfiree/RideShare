package app.model.interfaces;

import app.model.GeoLocation;

public interface Located {

    GeoLocation getLocation();

    default boolean inBound() {
        return true;
    }

    @Override
    boolean equals(Object o);

    @Override
    String toString();
}