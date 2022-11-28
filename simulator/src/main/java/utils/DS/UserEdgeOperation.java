package utils.DS;

import simulator.model.utils.UserEdge;

@FunctionalInterface
public interface UserEdgeOperation {
    void operate(UserEdge edge);
}
