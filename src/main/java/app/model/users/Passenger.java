package app.model.users;

import app.model.graph.Node;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import static utils.Utils.FORMAT;



public class Passenger extends User {
    public enum State{
        Available("Available"), Matched("Matched"),
        Picked("Picked"), Dropped("Dropped");

        public String text;
        State(String name) {
            this.text = name;
        }

        @Override
        public String toString() {
            return "" + text;
        }
    }
    private State state;
    private final Date askTime;
    private final Node src, dest;
    private boolean carNextTarget;
    private Date pickupTime, dropTime;
    private long totalTimeTraveled, timeWaited;
    private boolean pickedup;

    public boolean isPickedup() {
        return pickedup;
    }

    public void setPickedup(boolean pickedup) {
        this.pickedup = pickedup;
    }

    /* CONSTRUCTORS  */
    public Passenger(@NotNull Node currNode, @NotNull Node destination, Date askTime) {
        state = State.Available;
        this.askTime = askTime;
        this.dest = destination;
        this.src = currNode;
        pickedup = false;
    }


    /* LOGIC */

    public void markMatched() {
        state = State.Matched;
        //remove user edges from graph
        UserMap.INSTANCE.matchPassenger(this);
    }

    public boolean isMatched() {
        return state == State.Matched;
    }



    /* GETTERS */

    public State state() {
        return state;
    }

    @Override
    public Node getNextStop(){
        if( isCarNextTarget()){
            return getFinalDestination();
        }else{
            return getLocation();
        }
    }

    @Override
    public Node getFinalDestination() { return dest; }

    @Override
    public Node getLocation() { return src; }

    @Override
    public Date getStartTime() { return askTime; }

    public Date getPickupTime(){
        return this.pickupTime;
    }

    public Date getDropTime() {
        return this.dropTime;
    }

    public long getTotalTimeTraveled() {
        return totalTimeTraveled;
    }

    public long getTimeWaited() {
        return timeWaited;
    }



    /* SETTERS */

    public void setCarNextTarget(boolean nextTarget) {
        this.carNextTarget = nextTarget;
    }

    public boolean isCarNextTarget() {
        return carNextTarget;
    }


    public void markedPickup(Date time) {
        state = State.Picked;
        this.pickupTime = time;
        timeWaited = (time.getTime() - askTime.getTime()) / (60*1000) % 60;
        UserMap.INSTANCE.finishEvent(this);
    }

    public void setDropTime(Date dropTime) {
        totalTimeTraveled = (dropTime.getTime() - pickupTime.getTime()) / (60*1000) % 60;
        this.dropTime = dropTime;
    }

    private void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Passenger) {
            return ((Passenger) o).getId() == this.id;
        }
        return false;
    }

//    @Override
//    public String toString() {
//        return "Passenger{" +
//                "id=" + id +
//                ", askTime=" + askTime +
//                ", src=" + src.getId() +
//                ", dest=" + dest.getId() +
//                '}';
//    }

    @Override
    public String toString() {
        if (this.dropTime != null) {
            return "Rider " + id + " picked up at: " + pickupTime + " Dropped at: " + dropTime
                    + " Total time travel: " + totalTimeTraveled;
        }
        return "Rider " + id + ", start time " + FORMAT(askTime) ;
    }
}
