package app.view;

import app.model.GeoLocation;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.MultiNode;

//todo check if can combine my node with this
public class DisplayNode extends MultiNode {
    GeoLocation location;
    public DisplayNode(AbstractGraph displayGraph, GeoLocation location, String id) {
        super(displayGraph, id);

        this.location = location;
    }

    public GeoLocation getLocation() { return location; }
}
