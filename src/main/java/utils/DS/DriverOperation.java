package utils.DS;

import app.model.users.Driver;

@FunctionalInterface
public interface DriverOperation {
    void operate(Driver driver);
}
