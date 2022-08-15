package utils.DS;

import app.model.graph.Node;

@FunctionalInterface
public interface NodeOperation {
    void operate(Node node);
}
