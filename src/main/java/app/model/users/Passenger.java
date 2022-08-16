package app.model.users;

import app.controller.simulator.Simulator;
import app.model.graph.Node;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import static utils.Utils.FORMAT;



public class Passenger extends User {
    private final Date askTime;
    private final Node src, dest;
    private boolean matched;
    private boolean carNextTarget;
    private Date pickupTime, dropTime;
    private long totalTimeTraveled, timeWaited;


    /* CONSTRUCTORS  */
    public Passenger(@NotNull Node currNode, @NotNull Node destination, Date askTime) {
        this.askTime = askTime;
        this.dest = destination;
        this.src = currNode;
    }


    /* LOGIC */

    public void markMatched() {
        /* remove this from waiting list */
        matched = true;

        //remove user edges from graph
        UserMap.INSTANCE.matchPassenger(this);
    }

    public boolean isMatched() {
        return matched;
    }

//    public boolean distTo(Node node) {
//        return Math.min(getDestination().distanceTo(node), getLocation().distanceTo(node));
//    }



    /* GETTERS */

    @Override
    public Node getNextStop(){
        if( isCarNextTarget()){
            return getDestination();
        }else{
            return getLocation();
        }
    }

    @Override
    public Node getDestination() { return dest; }

    @Override
    public Node getLocation() { return src; }

    @Override
    public Date getStartTime() { return askTime; }



    /* SETTERS */

    public void setCarNextTarget(boolean nextTarget) {
        this.carNextTarget = nextTarget;
    }

    public boolean isCarNextTarget() {
        return carNextTarget;
    }



    public void setPickupTime(Date pickupTime) {
        this.pickupTime = pickupTime;
        timeWaited = (pickupTime.getTime() - askTime.getTime()) / (60*1000) % 60;
    }

    public void setDropTime(Date dropTime) {
        totalTimeTraveled = (dropTime.getTime() - pickupTime.getTime()) / (60*1000) % 60;
        this.dropTime = dropTime;
    }

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

    @Override
    public boolean equals(Object o) {
        if (o instanceof Passenger) {
            return ((Passenger) o).getId() == this.id;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", askTime=" + askTime +
                ", src=" + src.getId() +
                ", dest=" + dest.getId() +
                '}';
    }

    //    @Override
    //    public String toString() {
    //        if (this.dropTime != null) {
    //            return "Rider " + id + " picked up at: " + pickupTime + " Dropped at: " + dropTime
    //                    + " Total time travel: " + totalTimeTraveled;
    //        }
    //        return "Rider " + id + ", start time " + FORMAT(askTime) ;
    //    }
}
