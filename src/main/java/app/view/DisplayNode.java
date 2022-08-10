package app.view;

import app.model.utils.Coordinates;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.MultiNode;

//todo check if can combine my node with this
public class DisplayNode extends MultiNode {
    Coordinates location;
    public DisplayNode(AbstractGraph displayGraph, Coordinates location, String id) {
        super(displayGraph, id);

        this.location = location;
    }

    public Coordinates getLocation() { return location; }
}
