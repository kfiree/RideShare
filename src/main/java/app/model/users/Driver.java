package app.model.users;

/* third party */
import java.time.LocalDateTime;
import java.util.*;

import app.controller.Simulator;
import app.controller.SimulatorThread;
import app.view.MapView;
import org.jetbrains.annotations.NotNull;

/* local */
import app.model.graph.Node;
import app.model.graph.Path;
import app.controller.GraphAlgo;
import utils.HashPriorityQueue;
import app.controller.Latch;

/* static imports */
import static app.controller.UserMapHandler.userMap;
import static utils.LogHandler.LOGGER;
import static utils.Utils.*;

/**
 * TODO add multiple middle paths (TSP):
 *         public void addPassenger(Path currPath) {
 *             this.currPath = currPath;
 *         }
*/
public class Driver extends User implements Runnable, SimulatorThread {
    private final Latch latch;
    private static final int MAX_PASSENGERS_NUM = 2; //todo set to 3
    private final int id;
    private final HashPriorityQueue<Rider> passengers;
    private Path path;
    private Node currNode, destination;
    private Rider onTheWayTo;
    private double originalTime, detoursTime;
    private final Date startTime;
    private Date localTime;
    private boolean pathChange;

    /* CONSTRUCTORS */

    private Driver(Date startTime) {
        this.latch = Latch.INSTANCE;
        this.id = UserMap.keyGenerator.incrementAndGet();
        this.startTime = startTime;
        this.localTime = startTime;
        this.passengers = new HashPriorityQueue<>(
                Comparator.comparingDouble(rider -> rider.getNextStop().distanceTo(currNode))
        );
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

        Iterator<Node> nodeIter = getPath().iterator();
        Node nextNode = nodeIter.next();

        while(nodeIter.hasNext()){

            currNode = nextNode;
            nextNode = nodeIter.next();

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
        LOGGER.finest("Drive "+ id +" finished!, total time : " + (timeDiff(startTime, localTime)/ 60000.0) + " minutes.");
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
                        onTheWayTo.setDropTime(Simulator.INSTANCE.time());
                        System.out.println("rider stat: "+ onTheWayTo.toString());
                        getNextDest();
                    }
//                        updatePath(rider.getNextStop());
                }
            } else {
                updatePath(getDestination());
            }
        }
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
        return startTime;
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
        return "Drive " + id + ", start time =" + FORMAT(this.startTime) +", weight :  " + this.originalTime / 60000 +" minutes.";
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
