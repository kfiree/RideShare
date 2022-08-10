package app.model;

import app.controller.GraphAlgo;
import app.controller.Simulator;
import app.model.interfaces.ElementOnMap;
import utils.JsonHandler;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import static utils.LogHandler.LOGGER;

/**
 * todo make iterate on edges thread safe
 *      add only from here
 *      -
 *      - check if need hashtable for data structures that doesnt use get();
 */
public class UserMap {
    private final Hashtable<Integer, Drive> drives;
    private final Hashtable<Integer, Rider> requests;
    private final ArrayList<Rider>  pendingRequests;
    private final ArrayList<Drive> onGoingDrives;
    private final ArrayList<ElementOnMap> finished;
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

    public Collection<Drive> getDrives() { return drives.values(); }

//    public Drive getOnGoingDrive(int id){
//        return onGoingDrives.get(id);
//    }

    public Collection<Drive> getOnGoingDrives() { return onGoingDrives; }

    public Collection<Rider> getRequests() { return requests.values(); }

    public Collection<Rider> getPendingRequests() { return pendingRequests; }

    public ArrayList<ElementOnMap> getFinishedEvents() {
        return finished;
    }

    public Date getFirstEventTime() {
        return firstEventTime;
    }

    /*     SETTERS     */

    public void addDrive(Drive drive) {
        for(Rider rider: this.requests.values()){
            this.userEdges.add(new UserEdge(drive, rider));

        }
        drives.put(drive.getId(), drive);
    }

    public Drive addDrive(Node src, Node dst, Long delayTime) {
        Drive drive = new Drive(src, dst, new Date(firstEventTime.getTime()+ delayTime));
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

    public LinkedList<ElementOnMap> getEventQueue(){
        LinkedList<ElementOnMap> events = new LinkedList<>();

        events.addAll(getDrives());
        events.addAll(getRequests());

        events.sort(Comparator.comparing(ElementOnMap::getStartTime)
                .thenComparing(ElementOnMap::getStartTime));

        return events;
    }

    public void startRequest(Rider rider){
        this.pendingRequests.add(rider);
    }

    public void startDrive(Drive drive){
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

    public void finishUserEvent(ElementOnMap element) {
        if(element instanceof Drive){
            onGoingDrives.remove(element);
        }else{
            pendingRequests.remove(element);
        }
        finished.add(element);
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
