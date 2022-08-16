package app.model.users;

import app.controller.Simulator;
import app.model.graph.Node;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;

import static utils.Utils.FORMAT;



public class Rider extends User {
    private final Date askTime;
    private final Node src, dest;
    private final int id;
    private boolean taken;
    private boolean carNextTarget;
    private Date pickupTime, dropTime;
    private long totalTimeTraveled, timeWaited;


    public Rider(@NotNull Node currNode, @NotNull Node destination, Date askTime) {
        this.id = UserMap.keyGenerator.incrementAndGet();
        this.askTime = askTime;
        this.dest = destination;
        this.src = currNode;
    }

    public void markTaken() {
        /* remove this from waiting list */
        taken = true;
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        setPickupTime(Simulator.INSTANCE.time());
        UserMap.INSTANCE.pickPedestrian(this);
        System.out.println("pickup time: "+ pickupTime);
    }

    public boolean isTaken() {
        return taken;
    }

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

    public void setCarNextTarget(boolean nextTarget) {
        this.carNextTarget = nextTarget;
    }

    public boolean isCarNextTarget() {
        return carNextTarget;
    }

    @Override
    public Date getStartTime() { return askTime; }

    @Override
    public int getId() { return id; }

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
    public String toString() {
        if (this.dropTime != null) {
            return "Rider " + id + " picked up at: " + pickupTime + " Dropped at: " + dropTime
                    + " Total time travel: " + totalTimeTraveled;
        }
        return "Rider " + id + ", start time " + FORMAT(askTime) ;
    }
}
