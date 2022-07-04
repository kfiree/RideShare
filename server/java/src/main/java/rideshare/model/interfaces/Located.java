package rideshare.model.interfaces;

import rideshare.model.GeoLocation;

public interface Located {

    GeoLocation getCoordinates();

    boolean inBound();

    @Override
    boolean equals(Object o);

    @Override
    String toString();
}