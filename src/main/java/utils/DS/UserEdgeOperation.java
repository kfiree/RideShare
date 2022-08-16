package utils.DS;

import app.model.utils.UserEdge;

@FunctionalInterface
public interface UserEdgeOperation {
    void operate(UserEdge edge);
}
