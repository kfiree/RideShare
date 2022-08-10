package app.model.utils;

import app.model.users.Driver;
import app.model.users.Rider;
import app.model.utils.Coordinates;

public class UserEdge {
    private final Driver drive;
    private final Rider rider;
    private double weight;
    private final int id;
    private static int  keyGenerator = 0;


    public UserEdge(Driver drive, Rider ride){
        this.id = ++keyGenerator;
        this.drive = drive;
        this.rider = ride;
    }

    /*  GETTERS */

    public int getId() { return id; }

    public Driver getDrive() {
        return drive;
    }

    public Rider getRider() {
        return rider;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

//    public Coordinates getLocation(){
//        return drive.getLocation();
//    }
}
