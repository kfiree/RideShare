package model;

public interface MapObject {

    String getId();

    public GeoLocation getCoordinates();

    @Override
    boolean equals(Object o);

    @Override
    String toString();
}
