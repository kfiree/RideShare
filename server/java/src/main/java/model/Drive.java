package model;

import controller.utils.MapUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Drive extends Path implements Runnable  {
    private String driverType;
    private String driveOwnerId;
    private String[] passengers = new String[3];
    private Date leaveTime;
    private Path path;
    private Edge currentEdge;

//    public Drive(ArrayList<Edge> edges, Node start, Node end, String driverType, String driveOwnerId, Date leaveTime) { TODO fix
//        super(edges, start, end);
//        this.driverType = driverType;
//        this.driveOwnerId = driveOwnerId;
//        this.leaveTime = leaveTime;
//    }

    public Drive(@NotNull List<Node> nodes, String driverType, String driveOwnerId, Date leaveTime) {
        super(nodes);
        this.driverType = driverType;
        this.driveOwnerId = driveOwnerId;
        this.leaveTime = leaveTime;
    }

    @Override
    public void run() {

        MapUtils.validate(path != null, "can't start a drive if path is null");

        do{
            currentEdge = path.getNext();
            sleep(currentEdge.getWeight());
        }while(currentEdge != null);

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
        long ms = (long) (hour * 3600000);
        try { Thread.sleep( ms ) ; }
        catch (InterruptedException e) { e.printStackTrace(); }
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