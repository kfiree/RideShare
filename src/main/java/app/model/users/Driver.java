package app.model.users;

/* third party */
import java.util.*;

import app.controller.TimeSync;
import org.jetbrains.annotations.NotNull;

/* local */
import app.controller.Simulator;
import app.model.graph.Node;
import app.model.graph.Path;
import app.controller.GraphAlgo;
import utils.HashPriorityQueue;
import utils.SimulatorLatch;

/* static imports */
import static utils.LogHandler.LOGGER;
import static utils.Utils.*;

/**
 * TODO add multiple middle paths (TSP):
 *         public void addPassenger(Path currPath) {
 *             this.currPath = currPath;
 *         }
*/
public class Driver extends User implements Runnable, TimeSync{
    private static final int MAX_PASSENGERS_NUM = 2; //todo set to 3
    private final int id;
    private final HashPriorityQueue<Rider> passengers;
    private final Date leaveTime;
    private Path path;
    private Node currNode, destination;
    private Rider onTheWayTo;
    private double originalTime, detoursTime;
    private boolean pathChange;
    private Date localTime;
    private static SimulatorLatch latch = SimulatorLatch.INSTANCE;

    /** CONSTRUCTORS */

    private Driver(Date leaveTime) {
        this.id = UserMap.keyGenerator.incrementAndGet();
        this.leaveTime = leaveTime;
        this.passengers = new HashPriorityQueue<>(
                Comparator.comparingDouble(rider -> rider.getNextStop().distanceTo(currNode))
        );
    }

    public Driver(@NotNull Path path, Date leaveTime) {
        this(leaveTime);
        initPathVariables(path);
    }

    public Driver(@NotNull Node src, @NotNull Node dst, Date leaveTime) {
        this(leaveTime);
        initPathVariables(Objects.requireNonNull(GraphAlgo.getShortestPath(src, dst)));
    }

    public void initPathVariables(@NotNull Path path) {
        setPath(path);
        currNode = path.getSrc();
        destination = path.getDest();
        originalTime = path.getWeight();
    }



    /* RUN */

    @Override
    public void run() {
        register(this);

        LOGGER.fine("Drive "+ id +" started.");
        validate(getPath() != null, "can't start a drive if path is null. Drive owner id - " + id);

        while(!passengers.isEmpty() || currNode!= getDestination()){
            getNextDest();

            driveToNextStop();
        }

        UserMap.INSTANCE.finishUserEvent(this);

        finish();
    }

    private void driveToNextStop(){
        pathChange = false;

        List<Node> nodes = getPath().getNodes();
        Iterator<Node> nodeIter = getPath().iterator();
        Node nextNode = nodeIter.next();

        while(nodeIter.hasNext()){

            currNode = nextNode;
            nextNode = nodeIter.next();

            long timeToNextNode = currNode.getEdgeTo(nextNode).getWeight();

            sleep(timeToNextNode);

            originalTime -= timeToNextNode;

            lock(false);

            currNode = nextNode;

            if(pathChange){
                return;
            }


            latch.waitIfPause();
        }
        pathChange = true;
    }

    private void finish() {
        unregister(this);
        LOGGER.finest("Drive "+ id +" finished!.");
    }

    private void sleep(long sleepTime ) {
        if(sleepTime <1 ){
            LOGGER.warning("drive " + getId()+" sleep time "+ FORMAT(sleepTime) +" seconds is too small.");
        }

        try {
            Thread.sleep( (long) (sleepTime * 1000 / Simulator.INSTANCE.speed()));
        } catch (InterruptedException e) {
            LOGGER.severe(e.getMessage() +"\n"+ Arrays.toString(e.getStackTrace()));
        }
    }



    /* LOGIC */

    /**
     *  calculate the distance from the node to the closest stop of Driver
     *
     * @return distance to node
     */
    public double distTo(Node node){
        double minDist = Math.min(getLocation().distanceTo(node), getDestination().distanceTo(node));

        for(Rider rider: getPassengers()) {
            minDist = Math.min(minDist, rider.getDestination().distanceTo(node));
            if (!rider.isCarNextTarget()) {
                minDist = Math.min(minDist, rider.getLocation().distanceTo(node));
            }
        }
        return minDist;
    }

    /**
     * @return true if car not full
     */
    public boolean canSqueezeOneMore(){
        return passengers.size() < MAX_PASSENGERS_NUM;
    }



    /* SETTERS */

    /**
     * add passenger and calculate detour
     *
     * todo improve detour time heuristic
     */
    public void addPassenger(Rider rider){
        /* extend path to pick up pedestrian */
        rider.markTaken();
        addStop(rider);

        if(passengers.peek() == rider){
            if(onTheWayTo != null) {
                onTheWayTo.setCarNextTarget(false);
            }
            onTheWayTo = rider;
            detoursTime += distTo(onTheWayTo.getDestination()) + distTo(onTheWayTo.getLocation());
            updatePath(onTheWayTo.getNextStop());
        }
    }

    public void updatePath(Node dst) {//todo add null check
        this.pathChange = true;

        /* set new path without first node */
        Path shortestPath = GraphAlgo.getShortestPath(getLocation(), dst);
        shortestPath.getNodes().remove(0);

        setPath(shortestPath);
    }

    @Override
    public void setTime(Date date) {
        this.localTime = date;
    }

    private void addStop(Rider rider){
        passengers.add(rider);
    }

    public void setPath(Path path) {
        this.path = path;
    }



    /* GETTERS */

    private void getNextDest(){//System.out.println(this.currNode.getId());
        if(pathChange){
            if (!passengers.isEmpty()) {
                onTheWayTo = passengers.poll();

                if (!onTheWayTo.isCarNextTarget()) { /* passenger picked up */
//                    System.out.println(this.id + " picked up " + onTheWayTo.getId());
                    addStop(onTheWayTo);
                    passengers.add(onTheWayTo);
                    onTheWayTo.setCarNextTarget(true);
                    updatePath(onTheWayTo.getNextStop());
                    UserMap.INSTANCE.finishUserEvent(onTheWayTo);
                } else { /* passenger dropped */
                    if (currNode == onTheWayTo.getDestination()) {
//                        System.out.println(this.id + " dropped up " + onTheWayTo.getId());
                        getNextDest();
                    }
//                        updatePath(rider.getNextStop());
                }
            } else {
                updatePath(getDestination());
            }
        }
    }

    public double getDetoursTime() {
        return detoursTime;
    }

    @Override
    public Node getDestination() {
        return this.destination;
//        return paths.get(paths.size() -1).getDestination(); // TODO check if last
    }

    @Override
    public Node getLocation() {
        return currNode;
    }

    @Override
    public int getId() {
        return id;
    }

    public Set<Rider> getPassengers() {
        return passengers;
    }

    @Override
    public Date getStartTime() {
        return leaveTime;
    }

    @Override
    public Node getNextStop() {
        return getDestination();
    }

    public Path getPath(){
        return path;
    }

    public List<Node> getNodes(){
        return getPath().getNodes();
    }

    public double getOriginalTime() {
        return originalTime;
    }

    @Override
    public Date getTime() {
        return localTime;
    }



    /* SETTERS */
    @Override
    public String toString() {
        return "Drive{" +
                "id=" + id +
                ", passengers=" + passengers.size() +
                ", leaveTime=" + FORMAT(leaveTime) +
                ", pathSize=" + getPath().getSize() +
                ", currNode=" + currNode.getId() +
                '}';
    }
}


//    TimerTask moveToNext = new TimerTask(){
//
//        @Override
//        public void run() {
//            Iterator<Node> nodeIter = getPath().iterator();
//            Node nextNode = nodeIter.next();
//
//            while(nodeIter.hasNext()){
//                currNode = nextNode;
//                nextNode = nodeIter.next();
//
//                long timeToNextNode = currNode.getEdgeTo(nextNode).getWeight();
//
//                sleep(timeToNextNode);
//
//                originalTime -= timeToNextNode;
//
//                lock(false);
//
//                currNode = nextNode;
//
//            }
//        }
//    };
