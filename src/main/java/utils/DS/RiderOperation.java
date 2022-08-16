package utils.DS;

import app.model.users.Passenger;

@FunctionalInterface
public interface RiderOperation {
    void operate(Passenger passenger);
}
