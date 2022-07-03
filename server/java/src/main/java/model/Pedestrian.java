package model;

import controller.utils.GraphAlgo;
import model.interfaces.ElementsOnMap;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static view.StyleUtils.dateFormatter;

public class Pedestrian implements ElementsOnMap {
    private final Date askTime;
    private final Node currNode, destination;
    private final String id;
    private final Path path;
    private boolean taken;

    public Pedestrian(String id, @NotNull Node currNode, @NotNull Node destination, Date askTime) {
        this.id = id;
        this.askTime = askTime;
        this.destination = destination;
        this.currNode = currNode;
        path = GraphAlgo.getShortestPath(currNode, destination);
    }

    public void take() {//todo remove from waiting list
        /* remove this from waiting list */
        taken = true;
        UsersMap.INSTANCE.pickPedestrian(this);
    }

    public boolean isTaken() {
        return taken;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public Node getDestination() { return destination; }

    @Override
    public Node getCurrNode() { return currNode; }

    @Override
    public Date getAskTime() { return askTime; }

    @Override
    public String getId() { return id; }

    @Override
    public GeoLocation getLocation() { return currNode.getCoordinates(); }

    @Override
    public String toString() {
        return "Pedestrian{" +
                "id='" + id + '\'' +
                ", askTime=" + dateFormatter.format(askTime) +
                ", currNode=" + currNode +
                ", destination=" + destination.getOsmID() +
                '}';
    }
}
