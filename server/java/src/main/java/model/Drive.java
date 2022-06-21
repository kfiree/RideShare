package model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Drive extends Path {
    private String driverType;
    private String driveOwnerId;
    private String[] passengers = new String[3];
    private Date leaveTime;

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

}