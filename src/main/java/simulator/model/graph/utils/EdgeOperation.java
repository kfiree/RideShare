package simulator.model.graph.utils;

import simulator.model.graph.Edge;

@FunctionalInterface
public interface EdgeOperation {
    void operate(Edge edge);
}
