package simulator.model.users;

import simulator.controller.Simulator;
import simulator.model.utils.UserEdge;
import road_map.model.graph.Node;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * todo make iterate on edges thread safe
 *      add only from here
 *      -
 *      - check if need hashtable for data structures that doesnt use get();
 */
public class UserMap {
    private final Hashtable<Integer, Driver> drives;
    private final Hashtable<Integer, Passenger> requests;
    private final ArrayList<Passenger> liveRequest;
    private final ArrayList<Driver> liveDrives;
    private final ArrayList<User> finished;
    protected static AtomicInteger keyGenerator = new AtomicInteger(-1);
    private Date firstEventTime, lastEventTime;
    private final HashSet<UserEdge> userEdges;//todo use or delete
    private Simulator simulator;
    public static ReentrantLock requestsLock = new ReentrantLock(), drivesLock = new ReentrantLock();

    /** CONSTRUCTORS  */
    private UserMap() {
        this.liveDrives = new ArrayList<>();
        this.liveRequest = new ArrayList<>();
        finished = new ArrayList<>();

        drives = new Hashtable<>();
        requests = new Hashtable<>();
        userEdges = new HashSet<>();
    }

    /**  Singleton specific properties */
    public static final UserMap INSTANCE = new UserMap();

    /* GETTERS */

    public Collection<Driver> getDrives() { return drives.values(); }

    public Driver getDriver(int id) { return drives.get(id); }

//    public Drive getOnGoingDrive(int id){
//        return onGoingDrives.get(id);
//    }

    public Collection<Driver> getLiveDrives() { return liveDrives; }

    public Collection<Passenger> getRequests() { return requests.values(); }

    public Collection<Passenger> getLiveRequest() { return liveRequest; }

    public ArrayList<User> getFinishedEvents() {
        return finished;
    }

    public Date getFirstEventTime() {
        return firstEventTime;
    }

    /*     SETTERS     */

    public void addDrive(Driver drive) {
        for(Passenger passenger : this.requests.values()){
            this.userEdges.add(new UserEdge(drive, passenger));
        }

        //connect driver to other riders
        connectDrivesToPassengers(drive);

        drives.put(drive.getId(), drive);
    }

    public Driver addDrive(Node src, Node dst, Long delayTime) {
        Driver drive = new Driver(src, dst, new Date(firstEventTime.getTime()+ delayTime));

        //connect driver to other riders
        connectDrivesToPassengers(drive);

        drives.put(drive.getId(), drive);
        return drive;
    }

    public void addRequest(Passenger passenger){
        requests.put(passenger.getId(), passenger);

        // connect passenger to drives
        connectRequestToPDrivers(passenger);
    }

    public void addRequest(Node src, Node dst, Date date) {
        Passenger passenger = new Passenger(src, dst, date);
        requests.put(passenger.getId(), passenger);

        // connect passenger to drives
        connectRequestToPDrivers(passenger);
    }

    public LinkedList<User> getEventQueue(){
        LinkedList<User> events = new LinkedList<>();

        events.addAll(getDrives());
        events.addAll(getRequests());

        events.sort(Comparator.comparing(User::getStartTime)
                .thenComparing(User::getStartTime));

        return events;
    }

    public void startRequest(Passenger passenger){
        this.liveRequest.add(passenger);
    }

    public void startDrive(Driver drive){
        this.liveDrives.add(drive);
    }

    public Date setFirstEventTime() {
        if(firstEventTime == null) {
            this.firstEventTime = new Date();
        }
        return this.firstEventTime;
    }

//    public void setFirstEventTime(Date date) {
//        if(firstEventTime == null) {
//            this.firstEventTime = date;
//        }
//    }

    /* REMOVE FROM GRAPH */
//    public void finishEvent(User user, Date date) {

    public void finishEvent(User user) {
        if(user instanceof Passenger passenger){
            liveRequest.remove(passenger);
            //dis-connect driver from all the passenger
//            userEdges.removeIf(edge -> edge.getDrive().equals(user));
        }else{
            liveDrives.remove(user);
//            //dis-connect passenger from all the driver
//            userEdges.removeIf(edge -> edge.getRider().equals(user));
        }
        finished.add(user);
    }

    public void matchPassenger(Passenger passenger){
        liveRequest.remove(passenger);

        //dis-connect passenger from all the driver
        userEdges.removeIf(edge -> edge.getRider().equals(passenger));
    }

    private void connectDrivesToPassengers(Driver drive) {
        requests.values().forEach(request -> {
            userEdges.add(new UserEdge(drive, request));
        });
    }

    private void connectRequestToPDrivers(Passenger request) {
        drives.values().forEach(drive -> {
            userEdges.add(new UserEdge(drive, request));
        });
    }


    @Override
    public String toString() {
        return "UserMap{" +
                ", total requests=" + requests.size() +
                ", total drives=" + drives.size() +
                ", started drives=" + liveDrives.size() +
                ", started requests=" + liveRequest.size() +
                '}';
    }
}
