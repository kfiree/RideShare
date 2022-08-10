package app.model.users;

import app.controller.Simulator;
import app.model.utils.UserEdge;
import app.model.graph.Node;

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
    private final Hashtable<Integer, Rider> requests;
    private final ArrayList<Rider>  pendingRequests;
    private final ArrayList<Driver> onGoingDrives;
    private final ArrayList<User> finished;
    protected static AtomicInteger keyGenerator = new AtomicInteger(-1);
    private Date firstEventTime;
    private final HashSet<UserEdge> userEdges;//todo use or delete
    private Simulator simulator;
    public static ReentrantLock requestsLock = new ReentrantLock(), drivesLock = new ReentrantLock();
    /** CONSTRUCTORS  */
    private UserMap() {
        this.onGoingDrives = new ArrayList<>();
        this.pendingRequests = new ArrayList<>();
        finished = new ArrayList<>();

        drives = new Hashtable<>();
        requests = new Hashtable<>();
        userEdges = new HashSet<>();
    }

    /**  Singleton specific properties */
    public static final UserMap INSTANCE = new UserMap();

    /* GETTERS */

    public Collection<Driver> getDrives() { return drives.values(); }

//    public Drive getOnGoingDrive(int id){
//        return onGoingDrives.get(id);
//    }

    public Collection<Driver> getOnGoingDrives() { return onGoingDrives; }

    public Collection<Rider> getRequests() { return requests.values(); }

    public Collection<Rider> getPendingRequests() { return pendingRequests; }

    public ArrayList<User> getFinishedEvents() {
        return finished;
    }

    public Date getFirstEventTime() {
        return firstEventTime;
    }

    /*     SETTERS     */

    public void addDrive(Driver drive) {
        for(Rider rider: this.requests.values()){
            this.userEdges.add(new UserEdge(drive, rider));

        }
        drives.put(drive.getId(), drive);
    }

    public Driver addDrive(Node src, Node dst, Long delayTime) {
        Driver drive = new Driver(src, dst, new Date(firstEventTime.getTime()+ delayTime));
        drives.put(drive.getId(), drive);
        return drive;
    }

    public void addRequest(Rider rider){
        requests.put(rider.getId(), rider);
    }

    public void addRequest(Node src, Node dst, Date date) {
        Rider rider = new Rider(src, dst, date);
        requests.put(rider.getId(), rider);
    }

    public LinkedList<User> getEventQueue(){
        LinkedList<User> events = new LinkedList<>();

        events.addAll(getDrives());
        events.addAll(getRequests());

        events.sort(Comparator.comparing(User::getStartTime)
                .thenComparing(User::getStartTime));

        return events;
    }

    public void startRequest(Rider rider){
        this.pendingRequests.add(rider);
    }

    public void startDrive(Driver drive){
        this.onGoingDrives.add(drive);
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

    public void finishUserEvent(User user) {
        if(user instanceof Driver){
            onGoingDrives.remove(user);
        }else{
            pendingRequests.remove(user);
        }
        finished.add(user);
    }

    public void pickPedestrian(Rider rider){
        pendingRequests.remove(rider);
    }


    @Override
    public String toString() {
        return "UserMap{" +
                ", total requests=" + requests.size() +
                ", total drives=" + drives.size() +
                ", started drives=" + onGoingDrives.size() +
                ", started requests=" + pendingRequests.size() +
                '}';
    }
}
