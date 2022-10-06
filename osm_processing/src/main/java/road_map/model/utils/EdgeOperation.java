package road_map.model.utils;

import road_map.model.graph.Edge;

@FunctionalInterface
public interface EdgeOperation {
    void operate(Edge edge);
}
