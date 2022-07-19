package app.model;

public class UserEdge {
    private final Drive drive;
    private final Rider rider;
    private double weight;
    private final int id;
    private static int  keyGenerator = 0;


    public UserEdge(Drive drive, Rider ride){
        this.id = ++keyGenerator;
        this.drive = drive;
        this.rider = ride;
    }

    /*  GETTERS */

    public int getId() { return id; }

    public Drive getDrive() {
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

    public GeoLocation getLocation(){
        return drive.getLocation();
    }
}
