package model;

import model.interfaces.ElementsOnMap;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static view.StyleUtils.dateFormatter;

public class Pedestrian implements ElementsOnMap {
    private final Date startTime;
    private final Node currNode, destination;
    private final String id;
    private boolean taken;

    public Pedestrian(String id, @NotNull Node currNode, @NotNull Node destination, Date startTime) {
        this.id = id;
        this.startTime = startTime;
        this.destination = destination;
        this.currNode = currNode;
    }

    public boolean isTaken() {
        return taken;
    }

    public void take() {
        taken = true;
    }

    @Override
    public Node getDestination() { return destination; }

    @Override
    public Node getCurrNode() { return currNode; }

    @Override
    public Date getStartTime() { return startTime; }

    @Override
    public String getId() { return id; }

    @Override
    public GeoLocation getLocation() { return currNode.getCoordinates(); }

    @Override
    public String toString() {
        return "Pedestrian{" +
                "id='" + id + '\'' +
                ", startTime=" + dateFormatter.format(startTime) +
                ", currNode=" + currNode +
                ", destination=" + destination +
                '}';
    }
}
