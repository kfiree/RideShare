package model;

import controller.utils.MapUtils;

import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.Date;
import static controller.utils.LogHandler.LOGGER;


public class Drive implements Runnable  {//TODO make not implements
    private final String type, ownerId;
    private String[] passengers = new String[3];
    private final Date leaveTime;
    private final Path path;
    private Edge currentEdge;
    private double timeSpeed;


    public Drive(@NotNull Path path, String type, String OwnerId, Date leaveTime) {
        this.path = path;
        this.type = type;
        this.ownerId = OwnerId;
        this.leaveTime = leaveTime;
        currentEdge = path.getNext();

    }

    @Override
    public void run() {
        LOGGER.fine("Drive "+ ownerId +" started.");
        MapUtils.validate(path != null, "can't start a drive if path is null. Drive owner id - " + ownerId);

        do{
            System.out.println("drive " + this.getOwnerId() + " sleeps for "+ currentEdge.getWeight());
            sleep(currentEdge.getWeight());
            currentEdge = path.getNext();
        }while(currentEdge != null);
        LOGGER.finer("Drive "+ ownerId +" finished.");
    }

    public String getType() {
        return type;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String[] getPassengers() {
        return passengers;
    }

    public Date getLeaveTime() {
        return leaveTime;
    }

    public void setPassengers(String[] passengers) {
        this.passengers = passengers;
    }

    public Edge getCurrentEdge() {
        return currentEdge;
    }


//TODO check if correct time unit.
//TODO control speed with static var

    private void sleep(double hour ) {
        long ms = (long) (5000*timeSpeed);
//        long ms = (long) (hour * 3600000);
        try { Thread.sleep( ms ) ; }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void setTimeSpeed(double timeSpeed){
        this.timeSpeed = timeSpeed;
    }
    @Override
    public String toString() {
        return "Drive{" +
                "driverType='" + type + '\'' +
                ", driveOwnerId='" + ownerId + '\'' +
                ", passengers=" + Arrays.toString(passengers) +
                ", leaveTime=" + leaveTime +
                ", path=" + path +
                ", currentEdge=" + currentEdge +
                '}';
    }
}