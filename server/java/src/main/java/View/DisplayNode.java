package View;

import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.MultiNode;
import model.ONode;

public class DisplayNode extends MultiNode {

    private ONode nodeData;

    public DisplayNode(AbstractGraph graph, ONode node) {
        super(graph, node.getOsm_Id().toString());

        this.nodeData = node;
    }

    public ONode getData() {
        return nodeData;
    }
}
