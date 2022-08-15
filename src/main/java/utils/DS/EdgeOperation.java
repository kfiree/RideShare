package utils.DS;

import app.model.graph.Edge;

@FunctionalInterface
public interface EdgeOperation {
    void operate(Edge edge);
}
