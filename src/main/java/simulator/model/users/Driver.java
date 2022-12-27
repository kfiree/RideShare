package simulator.model.users;

/* third party */
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import simulator.controller.Simulator;
import simulator.controller.SimulatorThread;
import org.jetbrains.annotations.NotNull;

/* local */
import simulator.model.users.utils.GraphAlgo;
import simulator.model.graph.Node;
import simulator.model.graph.Path;
import utils.DS.Latch;
import utils.DS.RiderOperation;
import utils.DS.VolatilePriorityQueue;
import utils.Utils;
import utils.logs.LogHandler;

/* static imports */

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
    private Node currNode, destination;
    private Passenger nextPassenger;
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
        destination = path.getDest();
        originalTime = path.getWeight();
    }



    /* RUN */

    @Override
    public void run() {
        register(this);

        Thread.currentThread().setName(String.valueOf(id));

        LogHandler.LOGGER.fine("Drive "+ id +" started.");
        Utils.validate(getPath() != null, "can't choose a drive if path is null. Drive owner id - " + id);

        while(currNode!= getFinalDestination()){
            getNextDest();

            driveToNextStop();
        }

        UserMap.INSTANCE.finishEvent(this);


        finish();
    }

    private void driveToNextStop(){
        pathChange = false;

        Iterator<Node> nodeIter = getPath().iterator();
        Node nextNode = nodeIter.next();

        while(nodeIter.hasNext()){
            currNode = nextNode;
            nextNode = nodeIter.next();

            long timeToNextNode = currNode.getEdgeTo(nextNode).getWeight();

            sleep(timeToNextNode);

//            originalTime -= timeToNextNode;

            Utils.lock(false);

            currNode = nextNode;

            if(pathChange){
                return;
            }

            latch.waitOnCondition();

        }
        if(currNode != this.destination){
            pathChange = true;
        }
    }

    private void finish() {
        unregister(this);
        LogHandler.LOGGER.finest("Drive "+ id +" finished!, total time : " + (Utils.timeDiff(startTime, localTime)/ 60000.0) + " minutes.");
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
            if(nextPassenger != null && !passenger.isPickedup()) {
                nextPassenger.setCarNextTarget(false);
            }
            nextPassenger = passenger;
            detoursTime += distTo(nextPassenger.getFinalDestination()) + distTo(nextPassenger.getLocation());
            updatePath(nextPassenger.getNextStop());
        }
    }

    public void updatePath(Node dst) {//todo add null check
        this.pathChange = true;

        /* set new path without first node */
        Path shortestPath = GraphAlgo.getShortestPath(getLocation(), dst);
        shortestPath.getNodes().remove(0);

        setPath(shortestPath);
    }

    private void getNextDest(){
        if(pathChange && !passengers.isEmpty()) {
            passengerLock.lock();
            nextPassenger = passengers.poll();
            passengerLock.unlock();

            switch (nextPassenger.state()){
                case Matched -> {
                    addStop(nextPassenger);

                    if(getLocation() == nextPassenger.getLocation()){
                        nextPassenger.markedPickup(getTime());
                        updatePath(nextPassenger.getFinalDestination());
                    }else{
                        updatePath(nextPassenger.getLocation());
                    }

                }
                case Picked -> {
                    nextPassenger.setDropTime(Simulator.INSTANCE.time());
                    nextPassenger = null;
                    getNextDest();
                }
                case Dropped, Available  -> Utils.throwException("Bad pickup order. passenger can't be 'Dropped' or 'Available'.");
            }
        }else {
            updatePath(getFinalDestination());
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
        return this.destination;
//        return paths.get(paths.size() -1).getFinalDestination(); // TODO check if last
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
        if(nextPassenger == null){
            return getFinalDestination();
        }else{
            return nextPassenger.getNextStop();
        }
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

    public Passenger getNextPassenger() {
        return nextPassenger;
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
        return "Drive " + id + ", choose time =" + Utils.FORMAT(this.startTime) +", weight :  " + this.originalTime / 60000 +" minutes.";
    }
}


//    TimerTask moveToNext = new TimerTask(){
//
//        @Override
//        public void operate() {
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
