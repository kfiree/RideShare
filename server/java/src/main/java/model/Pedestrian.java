package model;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Pedestrian extends Node {
    private final GeoLocation destination;
    private final Date startTime;

    public Pedestrian(String id, @NotNull GeoLocation location, @NotNull GeoLocation destination, Date startTime) {
        super(id, 0L, location);
        this.destination = destination;
        this.startTime = startTime;
    }

    public GeoLocation getDestination() {
        return destination;
    }

    public Date getStartTime() {
        return startTime;
    }

}
