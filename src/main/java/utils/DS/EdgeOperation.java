package utils.DS;

import road_map.model.graph.Edge;

@FunctionalInterface
public interface EdgeOperation {
    void operate(Edge edge);
}
