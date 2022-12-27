package utils.DS;

import simulator.model.users.utils.UserEdge;

@FunctionalInterface
public interface UserEdgeOperation {
    void operate(UserEdge edge);
}
