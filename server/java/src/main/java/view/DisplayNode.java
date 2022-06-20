package view;

import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.MultiNode;
import model.Node;

public class DisplayNode extends MultiNode {

    private Node nodeData;

    public DisplayNode(AbstractGraph displayGraph, Node node) {
        super(displayGraph, node.getOsmID().toString());

        this.nodeData = node;
    }

    public Node getData() {
        return nodeData;
    }
}
