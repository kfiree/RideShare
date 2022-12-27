package simulator.model.users.utils;

import simulator.model.users.Driver;
import simulator.model.users.Passenger;

public class UserEdge {
    private final Driver drive;
    private final Passenger passenger;
    private double weight;
    private final int id;
    private static int  keyGenerator = 0;


    public UserEdge(Driver drive, Passenger ride){
        this.id = ++keyGenerator;
        this.drive = drive;
        this.passenger = ride;
    }

    /*  GETTERS */

    public int getId() { return id; }

    public Driver getDrive() {
        return drive;
    }

    public Passenger getRider() {
        return passenger;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UserEdge) {
            return ((UserEdge) o).getId() == this.id;
        }
        return false;
    }

//    public Coordinates getLocation(){
//        return drive.getLocation();
//    }
}
