package app.model;

import app.controller.GraphAlgo;
import app.controller.Simulator;
import app.model.interfaces.ElementOnMap;
import utils.JsonHandler;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static utils.LogHandler.LOGGER;

/**
 * todo make iterate on edges thread safe
 *      add only from here
 *      -
 *      - check if need hashtable for data structures that doesnt use get();
 */
public class UserMap {
    private final Hashtable<Integer, Drive> drives, onGoingDrives;
    private final Hashtable<Integer, Rider> requests, pendingRequests;
    private final HashSet<ElementOnMap> finished;
    protected static AtomicInteger keyGenerator = new AtomicInteger(-1);
    private Date firstEventTime;
    private final HashSet<UserEdge> userEdges;//todo use or delete
    private Simulator simulator;

    /** CONSTRUCTORS  */
    private UserMap() {
        this.onGoingDrives = new Hashtable<>();
        this.pendingRequests = new Hashtable<>();
        drives = new Hashtable<>();
        requests = new Hashtable<>();
        finished = new HashSet<>();
        userEdges = new HashSet<>();
    }

    /**  Singleton specific properties */
    public static final UserMap INSTANCE = new UserMap();

    /* GETTERS */

    public Collection<Drive> getDrives() { return drives.values(); }

    public Collection<Drive> getOnGoingDrives() { return onGoingDrives.values(); }

    public Collection<Rider> getRequests() { return requests.values(); }

    public Collection<Rider> getPendingRequests() { return pendingRequests.values(); }

    public HashSet<ElementOnMap> getFinishedEvents() {
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
        this.pendingRequests.put(rider.getId(), rider);
    }

    public void startDrive(Drive drive){
        this.onGoingDrives.put(drive.getId(), drive);
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

    public void finishedDriveOrPickedUp(ElementOnMap element) {
        if(element instanceof Drive){
            drives.remove(element.getId());
        }else{
            requests.remove(element.getId());
        }
        finished.add(element);
    }

    public void pickPedestrian(Rider rider){
        pendingRequests.remove(rider.getId());
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
