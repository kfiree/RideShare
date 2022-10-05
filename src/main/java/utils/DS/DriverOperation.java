package utils.DS;

import simulator.model.users.Driver;

@FunctionalInterface
public interface DriverOperation {
    void operate(Driver driver);
}
