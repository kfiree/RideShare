package app.model.users;

/* third party */
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import app.controller.simulator.Simulator;
import app.controller.simulator.SimulatorThread;
import org.jetbrains.annotations.NotNull;

/* local */
import app.model.graph.Node;
import app.model.graph.Path;
import app.model.utils.GraphAlgo;
import utils.DS.Latch;
import utils.DS.RiderOperation;
import utils.DS.VolatilePriorityQueue;

/* static imports */
import static utils.logs.LogHandler.LOGGER;
import static utils.Utils.*;

/**
 * TODO add multiple middle paths (TSP):
 *         public void addPassenger(Path currPath) {
 *             this.currPath = currPath;
 *         }
*/
public class Driver extends User implements Runnable, SimulatorThread {
    private final ReentrantLock passengerLock;
    private final Latch latch;
    private static final int MAX_PASSENGERS_NUM = 2; //todo set to 3
    private final Queue<Passenger> passengers;
    private Path path;
    private Node currNode, finalDestination;
    private Passenger nextRider;
    private double originalTime, detoursTime;
    private final Date startTime;
    private Date localTime;
    private boolean pathChange, newPassenger;

    /* CONSTRUCTORS */

    private Driver(Date startTime) {
        this.latch = Simulator.INSTANCE.getLatch();;
        this.startTime = startTime;
        this.localTime = startTime;
        this.passengers = new VolatilePriorityQueue<>(
                Comparator.comparingDouble(rider -> rider.getNextStop().distanceTo(currNode)));
        passengerLock = new ReentrantLock();
    }

    public Driver(@NotNull Path path, Date startTime) {
        this(startTime);
        initPathVariables(path);
    }

    public Driver(@NotNull Node src, @NotNull Node dst, Date startTime) {
        this(startTime);
        initPathVariables(Objects.requireNonNull(GraphAlgo.getShortestPath(src, dst)));
    }

    public void initPathVariables(@NotNull Path path) {
        setPath(path);
        currNode = path.getSrc();
        finalDestination = path.getDest();
        originalTime = path.getWeight();
    }



    /* RUN */

    @Override
    public void run() {
        register(this);

        Thread.currentThread().setName(String.valueOf(id));

        LOGGER.fine("Drive "+ id +" started.");
        validate(getPath() != null, "can't choose a drive if path is null. Drive owner id - " + id);

        while(!passengers.isEmpty() || currNode!= getFinalDestination()){
            getNextDest();

            driveToNextStop();
        }

        UserMap.INSTANCE.finishUserEvent(this);


        finish();
    }

    private void driveToNextStop(){
        pathChange = false;

        Iterator<Node> nodeIter = getPath().iterator();
        Node nextNode = nodeIter.next();

        while(nodeIter.hasNext()){
            currNode = nextNode;
            nextNode = nodeIter.next();
            if(nextNode == this.finalDestination){
                System.out.println("stop");
            }

            long timeToNextNode = currNode.getEdgeTo(nextNode).getWeight();

            sleep(timeToNextNode);

//            originalTime -= timeToNextNode;

            lock(false);

            currNode = nextNode;

            if(pathChange){
                return;
            }
//            System.out.println("driver"+this.getId());
            latch.waitOnCondition();

        }
        pathChange = true;
    }

    private void finish() {
        unregister(this);
        LOGGER.finest("Drive "+ id +" finished!, total time : " + FORMAT(timeDiff(startTime, localTime)/ 60000.0) + " minutes.");
    }



    /* LOGIC */

    /**
     *  calculate the distance from the node to the closest stop of Driver
     *
     * @return distance to node
     */
    public double distTo(Node node){
        double minDist = Math.min(getLocation().distanceTo(node), getFinalDestination().distanceTo(node));

//        passengerOperation(rider -> );

        Comparator<Passenger> comparator = Comparator.
                comparingDouble(rider ->
                        Math.min(rider.getFinalDestination().distanceTo(node), rider.getLocation().distanceTo(node)));
        Collections.min(this.passengers, comparator);

        for(Passenger passenger : getPassengers()) {
            minDist = Math.min(minDist, passenger.getFinalDestination().distanceTo(node));
            if (!passenger.isCarNextTarget()) {
                minDist = Math.min(minDist, passenger.getLocation().distanceTo(node));
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
    public void addPassenger(Passenger passenger){
        /* extend path to pick up pedestrian */
        passenger.markMatched();
        addStop(passenger);

        if(passengers.peek() == passenger){
            if(nextRider != null) {
                nextRider.setCarNextTarget(false);
            }
            nextRider = passenger;
            detoursTime += distTo(nextRider.getFinalDestination()) + distTo(nextRider.getLocation());
            updatePath(nextRider.getNextStop());
        }
    }

    public void updatePath(Node dst) {//todo add null check
        this.pathChange = true;

        /* set new path without first node */
        Path shortestPath = GraphAlgo.getShortestPath(getLocation(), dst);
        shortestPath.getNodes().remove(0);

        setPath(shortestPath);
    }

    private void getNextDest(){//System.out.println(this.currNode.getId());
        if(pathChange){
            if (!passengers.isEmpty()) {
                passengerLock.lock();
                nextRider = passengers.poll();
                passengerLock.unlock();
                if(this.currNode == this.finalDestination){
                    System.out.println("stop");
                }

                if (!nextRider.isCarNextTarget()) { /* passenger has not picked up */
                    addStop(nextRider);
                    if(this.getLocation() == nextRider.getLocation()) { /*passenger picked up*/
                        UserMap.INSTANCE.finishUserEvent(nextRider);
                        nextRider.setCarNextTarget(true);
                        nextRider.setPickupTime(getTime());
                        updatePath(nextRider.getNextStop());
                    }
                } else { /* passenger dropped */
                    if (currNode == nextRider.getFinalDestination()) {
                        nextRider.setDropTime(Simulator.INSTANCE.time());
                        getNextDest();
                    }
                }
            } else {
                updatePath(getFinalDestination());
            }
        }
    }

    @Override
    public void setTime(Date date) {
        this.localTime = date;
    }

    private void addStop(Passenger passenger){
        passengerLock.lock();
        passengers.add(passenger);
        passengerLock.unlock();

    }

    public void setPath(Path path) {
        this.path = path;
    }



    /* GETTERS */

    public double getDetoursTime() {
        return detoursTime;
    }

    @Override
    public Node getFinalDestination() {
        return this.finalDestination;
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

    public Queue<Passenger> getPassengers() {
        return passengers;
    }

    public void passengerOperation(RiderOperation op){
        passengerLock.lock();
        passengers.forEach(op::operate);
        passengerLock.unlock();

    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public Node getNextStop() {
        return path.getDest();
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


    @Override
    public boolean equals(Object o) {
        if (o instanceof Driver) {
            return ((Driver) o).getId() == this.id;
        }
        return false;
    }

    /* SETTERS */

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", passengers=" + passengers.size() +
                ", currNode=" + currNode.getId() +
                ", localTime=" + FORMAT(localTime) +
                ", driving time" + FORMAT(this.originalTime / 60000) +" minutes."+
                '}';
    }

}
