package model.interfaces;

import model.GeoLocation;

public interface Located {

    GeoLocation getCoordinates();

    boolean inBound();

    @Override
    boolean equals(Object o);

    @Override
    String toString();//TODO add more methods
}