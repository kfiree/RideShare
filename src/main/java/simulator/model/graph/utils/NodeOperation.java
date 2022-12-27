package simulator.model.graph.utils;

import simulator.model.graph.Node;

@FunctionalInterface
public interface NodeOperation {
    void operate(Node node);
}
