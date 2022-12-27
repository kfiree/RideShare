package utils.DS;

import simulator.model.users.Passenger;

@FunctionalInterface
public interface RiderOperation {
    void operate(Passenger passenger);
}
