package app.model;

import app.controller.GraphAlgo;
import app.model.interfaces.ElementOnMap;
import utils.JsonHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static utils.LogHandler.LOGGER;

/**
 * todo make iterate on edges thread safe
 *      add only from here
 *      -
 *      - check if need hashtable for data structures that doesnt use get();
 */
public class UserMap {
    private final Hashtable<String, Drive> drives, onGoingDrives;
    private final Hashtable<String, Rider> requests, pendingRequests;
    private final HashSet<ElementOnMap> finished;
    private final HashSet<UserEdge> userEdges;
    private Date firstEventTime;

    /** CONSTRUCTORS  */
    public UserMap() {
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

    public HashSet<ElementOnMap> getFinished() {
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

    public void addDrive(Node src, Node dst, String type, String id, Date date) {
        drives.put(id, new Drive(src, dst, type, id, date));
    }

    public void addRequest(Rider rider) {
        requests.put(rider.getId(), rider);
    }

    public void addRequest(String id, Node src, Node dst, Date date) {
        requests.put(id, new Rider(id, src, dst, date));
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

    /**  init events from DB */
    public void initEvents(){
        JsonHandler.UserMapType.load();
    }

    public void initRandomEvents(int driveNum, int pedestriansNum){
        this.firstEventTime = new Date();
        Random rand = new Random(1);

        initRandDrives(driveNum, rand);
        initRandRiders(pedestriansNum, rand);
    }

    public void initRandRiders(int requestsNum, Random rand){
        List<Node> nodes = new ArrayList<>(RoadMap.INSTANCE.getNodes());
        long pedestriansStartTime = firstEventTime.getTime() + 10000; //10 seconds after drives
        Node src, dst;

        int[] randomIndexes = rand.ints(requestsNum* 2L, 0, nodes.size()).toArray();

        //create drives
        for (int i = 0; i < requestsNum; i++) {
            src = nodes.get(randomIndexes[i*2]);
            dst = nodes.get(randomIndexes[i*2 +1 ]);
            int timeAdded = rand.nextInt(75000);

            requests.put(String.valueOf(i), new Rider(String.valueOf(i), src, dst, new Date(pedestriansStartTime + ((long) (750000 + timeAdded) * i))));
        }

        LOGGER.finer("init "+ requestsNum + " pedestrians.");
    }

    public void initRandDrives(int drivesNum, Random rand) {

        // drives variables
        List<Node> nodes = new ArrayList<>(RoadMap.INSTANCE.getNodes());
        Node src, dst;

        // init random indexes for nodes
        int[] randomIndexes = rand.ints(drivesNum* 2L, 0, nodes.size()).toArray();

        //create drives
        for (int i = 0; i < drivesNum; i++) {
            src = nodes.get(randomIndexes[i*2]);
            dst = nodes.get(randomIndexes[i*2 +1 ]);

            Path shortestPath;
            Drive drive = null;

            if(src != null && dst != null ){
                shortestPath = GraphAlgo.getShortestPath(src, dst);
                int timeAdded = rand.nextInt(75000);
                if(shortestPath != null) {
                    drive = new Drive(String.valueOf(i), shortestPath, "unknown", new Date(firstEventTime.getTime() + ((long) (750000 + timeAdded) * i)) );
                }
            }

            drives.put(drive.getId(), drive);
        }

        LOGGER.finer("init " + drivesNum + " drives.");
    }

    /* REMOVE FROM GRAPH */

    public void finished(ElementOnMap element) {
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
