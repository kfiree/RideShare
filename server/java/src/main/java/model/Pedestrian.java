package model;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Pedestrian extends Node {
    private final GeoLocation destination;
    private final Date askTime;

    public Pedestrian(String id, @NotNull GeoLocation location, @NotNull GeoLocation destination) {
        super(id, 0L, location);
        this.destination = destination;
        askTime = new Date();
    }

    public GeoLocation getDestination() {
        return destination;
    }

    public Date getAskTime() {
        return askTime;
    }

}
