package road_map.model.utils;

import road_map.model.graph.Node;

@FunctionalInterface
public interface NodeOperation {
    void operate(Node node);
}
