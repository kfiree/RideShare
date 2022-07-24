package app.model;

/* third party */
import java.util.*;

import app.model.interfaces.ElementOnMap;
import app.model.interfaces.Located;
import app.view.MapView;
import org.jetbrains.annotations.NotNull;

/* local */
import app.controller.GraphAlgo;
import utils.HashPriorityQueue;

/* static imports */
import static utils.LogHandler.LOGGER;
import static utils.Utils.*;


/*
 * todo make long way with stop-point instead of paths
 *      make drives thread-pool
 */
public class Drive implements Runnable, ElementOnMap, Located {
    private static final int MAX_PASSENGERS_NUM = 2; //todo set to 3
    private final String type, id;  //todo is type needed?
    private final HashPriorityQueue<Rider> stopsOnTheWay;
//    private final PriorityQueue<Rider> passengers;
//    private final HashMap<Node, ElementsOnMap> stops = new HashMap<>();
    private final Date leaveTime;
    private Path path;
    private Node currNode, destination;
    private double totalTime, detoursTime;
    private boolean pathChange;

    /** CONSTRUCTORS */

    private Drive(String type, String id, Date leaveTime) {
        this.type = type;
        this.id = "D" + id;
        this.leaveTime = leaveTime;
        this.stopsOnTheWay = new HashPriorityQueue<>(
                Comparator.comparingDouble(rider -> rider.getNextStop().distanceTo(currNode))
        );
    }

    public Drive(String id, @NotNull Path path, String type, Date leaveTime) {
        this(type, id, leaveTime);
        initPathVariables(path);
    }

    public Drive(@NotNull Node src, @NotNull Node dst, String type, String id, Date leaveTime) {
        this(type, id, leaveTime);
        initPathVariables(Objects.requireNonNull(GraphAlgo.getShortestPath(src, dst)));
    }

    public void initPathVariables(@NotNull Path path) {
        setPath(path);
        currNode = path.getSrc();
        destination = path.getDest();
        totalTime = path.getWeight();
    }



    /** RUN */

    @Override
    public void run() {
        LOGGER.fine("Drive "+ id +" started.");
        validate(getPath() != null, "can't start a drive if path is null. Drive owner id - " + id);

        while(!stopsOnTheWay.isEmpty() || currNode!=getDest()){
            getNextDest();

            driveToNextStop();
        }

        UserMap.INSTANCE.finished(this);

        LOGGER.finest("Drive "+ id +" finished!.");
    }

    private void driveToNextStop(){
        pathChange = false;
        int nodeIndex = 0;

        Iterator<Node> nodeIter = getPath().iterator();
        Node nextNode = nodeIter.next();

        while(nodeIter.hasNext()){
            nodeIndex++;
            currNode = nextNode;
            nextNode = nodeIter.next();

            if(nodeIndex % 5 ==0) {
                LOGGER.info("driver (" + getId() + ") " + FORMAT( nodeIndex / (double) getPath().getSize() * 100) + "% complete.");
            }

            Double timeToNextNode = currNode.getEdgeTo(nextNode).getWeight();

            sleep(timeToNextNode);

            totalTime -= timeToNextNode;

//            System.out.println("curr " + currNode.getOsmID() + " ,  nextNode " + nextNode.getOsmID());

            /* pick passenger on the way */
            lock(false);

            if(pathChange){
                return;
            }

            currNode = nextNode;

        }
    }


    /**
     * todo improve detour time heuristic
     *
     * add  detour to pick new passenger
     * @param rider
     */
    public void addDetour(Rider rider){
        /* extend path to pick up pedestrian */
        addStop(rider);

        if(stopsOnTheWay.peek() == rider){
            detoursTime += distTo(rider.getDest()) + distTo(rider.getCurrentNode());
            setPathTo(rider.getNextStop());
        }
    }

    private void getNextDest(){
        if(!stopsOnTheWay.isEmpty()){
            if(currNode == stopsOnTheWay.peek().getNextStop()) {
                Rider rider = stopsOnTheWay.poll();

                if (!rider.isPickedUp()) { /* passenger picked up */
                    addStop(rider);
                    stopsOnTheWay.add(rider);
                    rider.setPickUp();
                    setPathTo(rider.getNextStop());
                    UserMap.INSTANCE.finished(rider);
                } else { /* passenger dropped */
                    if(currNode == rider.getDest()) {
                        getNextDest();
                    }
                    setPathTo(rider.getNextStop());
                }
            }
        }else{
            setPathTo(getDest());
        }
    }





    /* TODO add multiple middle paths (TSP):
        public void addDetour(Path currPath) {
            this.currPath = currPath;
        }
    */
    private void sleep(double sleepTime ) {
        if(sleepTime <1 ){
            LOGGER.warning("drive " + getId()+" sleep time "+ FORMAT(sleepTime) +" seconds is too small.");
        }

        try {
            Thread.sleep( (long) (sleepTime * 1000 / MapView.simulatorSpeed));
        } catch (InterruptedException e) {
            LOGGER.severe(e.getMessage() +"\n"+ Arrays.toString(e.getStackTrace()));
        }
    }


    public double distTo(Node node){
        double minDist = Math.min(getCurrentNode().distanceTo(node), getDest().distanceTo(node));

        for(Rider rider: getPassengers()) {
            minDist = Math.min(minDist, rider.getDest().distanceTo(node));
            if (!rider.isPickedUp()) {
                minDist = Math.min(minDist, rider.getCurrentNode().distanceTo(node));
            }
        }
        return minDist;
    }

    /** GETTERS */

    public double getDetoursTime() {
        return detoursTime;
    }

//    private Node getNextPassengerStop(ElementOnMap rider){
//        return rider.isPickedUp()? rider.getDest() : rider.getCurrentNode();
//    }

    public boolean canSqueezeOneMore(){
        return stopsOnTheWay.size() < MAX_PASSENGERS_NUM;
    }

    public double getTotalTime() {
        return totalTime;
    }

    @Override
    public GeoLocation getLocation(){
        return currNode.getLocation();
    }

    @Override
    public Node getDest() {
        return this.destination;
//        return paths.get(paths.size() -1).getDest(); // TODO check if last
    }

    @Override
    public Node getCurrentNode() {
        return currNode;
    }

    public String getType() {
        return type;
    }

    @Override
    public String getId() {
        return id;
    }

    public Set<Rider> getPassengers() {
        return stopsOnTheWay;
    }

    @Override
    public Date getStartTime() {
        return leaveTime;
    }

    private void addStop(Rider rider){
        stopsOnTheWay.add(rider);
    }

    @Override
    public Node getNextStop() {
        return getDest();
    }

    public Path getPath(){
        return path;
    }

    public void setPath(Path path) {
        System.out.println(path + " path changed " + this.path);
        this.path = path;
    }

    public void setPathTo(Node dst) {//todo add null check
        this.pathChange = true;
        setPath(GraphAlgo.getShortestPath(getCurrentNode(), dst));
    }

    public List<Node> getNodes(){
        return getPath().getNodes();
    }


    @Override
    public String toString() {
        return "Drive{" +
                "id='" + id.substring(1) + '\'' +
                ", long id='" + id + '\'' +
                ", passengers=" + stopsOnTheWay.size() +
                ", leaveTime=" + FORMAT(leaveTime) +
                ", pathSize=" + getPath().getSize() +
                ", currNode=" + currNode.getId() +
                '}';
    }
}