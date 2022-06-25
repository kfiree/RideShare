package model;

import controller.utils.MapUtils;
import static controller.utils.LogHandler.log;

import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

public class Drive implements Runnable  {//TODO make not implements
    private String driverType;
    private String driveOwnerId;
    private String[] passengers = new String[3];
    private Date leaveTime;
    private Path path;
    private Edge currentEdge;
    private double timeSpeed;

//    public Drive(ArrayList<Edge> edges, Node start, Node end, String driverType, String driveOwnerId, Date leaveTime) { TODO fix
//        super(edges, start, end);
//        this.driverType = driverType;
//        this.driveOwnerId = driveOwnerId;
//        this.leaveTime = leaveTime;
//    }

    public Drive(@NotNull Path path, String driverType, String driveOwnerId, Date leaveTime) {
        this.path = path;
        this.driverType = driverType;
        this.driveOwnerId = driveOwnerId;
        this.leaveTime = leaveTime;
        currentEdge = path.getNext();

    }

    @Override
    public void run() {
        log(Level.FINER, "Drive "+driveOwnerId+" started.");
        MapUtils.validate(path != null, "can't start a drive if path is null. Drive owner id - " + driveOwnerId);

        do{
            System.out.println("drive " + this.getDriveOwnerId() + " sleeps for "+ currentEdge.getWeight());
            sleep(currentEdge.getWeight());
            currentEdge = path.getNext();
        }while(currentEdge != null);
        log(Level.FINER, "Drive "+driveOwnerId+" finished.");
    }

    public String getDriverType() {
        return driverType;
    }

    public String getDriveOwnerId() {
        return driveOwnerId;
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

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Edge getCurrentEdge() {
        return currentEdge;
    }


//TODO check if correct time unit.
//TODO control speed with static var

    private void sleep(double hour ) {
        long ms = 5000;
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
                "driverType='" + driverType + '\'' +
                ", driveOwnerId='" + driveOwnerId + '\'' +
                ", passengers=" + Arrays.toString(passengers) +
                ", leaveTime=" + leaveTime +
                ", path=" + path +
                ", currentEdge=" + currentEdge +
                '}';
    }
}