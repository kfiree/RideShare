package model;

import controller.utils.MapUtils;

import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static controller.utils.LogHandler.LOGGER;


public class Drive implements Runnable { //TODO make not implements
    private final String type, ownerId;
    private String[] passengers = new String[3];
    private final Date leaveTime;
    private final Path path;
    private Edge currEdge;
    private Node currNode;
    private double timeSpeed;


    public Drive(@NotNull Path path, String type, String OwnerId, Date leaveTime) {
        this.path = path;
        this.type = type;
        ownerId = OwnerId;
        this.leaveTime = leaveTime;
        timeSpeed = 2;
        currEdge = path.getNext();
        currNode = path.get_src();;

    }

    @Override
    public void run() {
        LOGGER.fine("Drive "+ ownerId +" started.");
        MapUtils.validate(path != null, "can't start a drive if path is null. Drive owner id - " + ownerId);

        do{
            sleep(currEdge.getWeight());

            currNode = currEdge.getOtherEnd(currNode.getId());

            currEdge = path.getNext();

        }while(currEdge != null);

        LOGGER.finer("Drive "+ ownerId +" has reached destination.");
    }

    public GeoLocation getLocation(){
        return currEdge == null? null : currNode.getCoordinates();
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

    public Edge getCurrEdge() { return currEdge; }

    public List<Edge> getEdges(){
        return path.getEdges();
    }
//TODO check if correct time unit.
//TODO control speed with static var

    private void sleep(double sleepTime ) {
        if(sleepTime <1 ){
            LOGGER.warning("drive " + getOwnerId()+" sleep time "+ String.format("%.2f", sleepTime) +" seconds is too small.");
        }else{
            LOGGER.info("drive " + getOwnerId() + " sleeps for "+ String.format("%.2f", sleepTime) + " seconds.");
        }

        try { Thread.sleep( (long)sleepTime * 1000) ; }
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
                ", currentEdge=" + currEdge +
                '}';
    }
}