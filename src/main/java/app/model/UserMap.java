package app.model;

import app.controller.GraphAlgo;
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
    private final HashSet<UserEdge> userEdges;
    private Date firstEventTime;
    protected static AtomicInteger keyGenerator = new AtomicInteger(-1);

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

    public Drive addDrive(Node src, Node dst, Date date) {
        Drive drive = new Drive(src, dst, date);
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

    public void initEventsInLine(int requestsNum){
        Random rand = new Random(1);
        Node src = RoadMap.INSTANCE.getNode(417772575L);
        Node dst = RoadMap.INSTANCE.getNode(1589498340L);

        this.firstEventTime = new Date();
        Path path = UserMap.INSTANCE.addDrive(src, dst, new Date(this.firstEventTime.getTime() + 5000)).getPath();

        ArrayList<Integer> randomIndexes = new ArrayList<>();


        Arrays.stream(rand.ints(requestsNum* 2L, 0, path.getSize()).toArray())
                .forEach(randomIndexes::add);

        int srcIndex, dstIndex;
        for (int i = 0; i < requestsNum*2; i+=2) {

            srcIndex = Math.min(randomIndexes.get(i),  randomIndexes.get(i + 1));
            dstIndex = Math.max(randomIndexes.get(i),  randomIndexes.get(i + 1));

            src = path.getNodes().get(srcIndex);
            dst = path.getNodes().get(dstIndex);


            UserMap.INSTANCE.addRequest(src, dst, new Date(this.firstEventTime.getTime() + i* 100L));

        }
        UserMap.INSTANCE.drives.values().forEach(d ->
                System.out.println("drive " + d.getId() + " from " + d.getCurrentNode().getId() + " to " + d.getDest().getId() + ", size " + d.getPath().getSize())
        );

        UserMap.INSTANCE.requests.values().forEach(d ->
                System.out.println("request " + d.getId() + " from " + d.getCurrentNode().getId() + " to " + d.getDest().getId())
        );
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

            Rider rider = new Rider(src, dst, new Date(pedestriansStartTime + ((long) (750000 + timeAdded) * i)));
            requests.put(rider.getId(), rider);
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
                    drive = new Drive(shortestPath, new Date(firstEventTime.getTime() + ((long) (750000 + timeAdded) * i)) );
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
