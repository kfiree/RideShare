package app.model;

/* third party */
import java.util.*;
import app.model.interfaces.ElementsOnMap;
import app.model.interfaces.Located;
import org.jetbrains.annotations.NotNull;

/* local */
import app.controller.GraphAlgo;

/* static imports */
import static utils.LogHandler.LOGGER;
import static utils.Utils.*;


/*
 * todo make long way with stop-point instead of paths
 *      make drives thread-pool
 */
public class Drive implements Runnable, ElementsOnMap, Located {
    private static final int MAX_PASSENGERS_NUM = 1; //todo set to 3
    private final String type, id;  //todo is type needed?
    private final List<Rider> passengers = new ArrayList<>();
    private final Date leaveTime;
    private Path path;
    private Node currNode, destination;
    private double simulatorSpeed, totalTime;
    private boolean pathChange;

    private Drive(String type, String id, Date leaveTime) {
        this.type = type;
        this.id = "D" + id;
        this.leaveTime = leaveTime;
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
        this.path = path;
        currNode = path.get_src();
        destination = path.getDest();
        totalTime = path.getWeight();
    }




    @Override
    public void run() {
        LOGGER.fine("Drive "+ id +" started.");
        validate(path != null, "can't start a drive if path is null. Drive owner id - " + id);

        while(currNode!=getDestination()){
            runOnPath();
        }

        UserMap.INSTANCE.finished(this);

        LOGGER.finest("Drive "+ id +" finished!.");
    }

    private void runOnPath(){
        pathChange = false;
        int nodeIndex = 0;

        Iterator<Node> nodeIter = path.iterator();
        Node nextNode = nodeIter.next();

        while(nodeIter.hasNext()){
            currNode = nextNode;
            nextNode = nodeIter.next();

            if(nodeIndex % 5 ==0) {
                LOGGER.info("driver (" + getId() + ") " + FORMAT( ++nodeIndex / (double) path.getSize() * 100) + "% complete.");
            }

            Double timeToNextNode = currNode.getEdgeTo(nextNode).getWeight();

            sleep(timeToNextNode);

            totalTime -= timeToNextNode;

            /* pick passenger on the way */
            for(Rider passenger: passengers){

                if(passenger.getCurrNode() == currNode){
                    //todo check add second condition  ->   !UserMap.INSTANCE.getFinished().contains(passenger)

                    System.out.println(this.id +" picked " +passenger.getId()+".");
                    UserMap.INSTANCE.finished(passenger);
                }
            }


            lock(false);
            if(pathChange){
                return;
            }else{
                currNode = nextNode;
            }
        }
    }

    public void pickPassenger(Rider rider){
        /* extend path to pick up pedestrian */
        System.out.println(this.id +" on his way to " +rider.getId()+".");
        path.addMiddlePath(rider.getPath(), currNode);
        this.pathChange = true;
        passengers.add(rider);
    }

    public boolean isFull(){
        return passengers.size() >= MAX_PASSENGERS_NUM;
    }

    public double getTotalTime() {
        return totalTime;
    }

    @Override
    public GeoLocation getLocation(){
        return currNode.getLocation();
    }

    @Override
    public Node getDestination() {
        return this.destination;
//        return paths.get(paths.size() -1).getDest(); // TODO check if last
    }

    @Override
    public Node getCurrNode() {
        return currNode;
    }

    public String getType() {
        return type;
    }

    @Override
    public String getId() {
        return id;
    }

    public List<Rider> getPassengers() {
        return passengers;
    }

    @Override
    public Date getStartTime() {
        return leaveTime;
    }


    @Override
    public Path getPath(){
        return path;
    }

    public List<Node> getNodes(){
        return path.getNodes();
    }


    /* TODO add multiple middle paths (TSP):
        public void addMiddlePath(Path currPath) {
            this.currPath = currPath;
        }
    */
    private void sleep(double sleepTime ) {
        if(sleepTime <1 ){
            LOGGER.warning("drive " + getId()+" sleep time "+ FORMAT(sleepTime) +" seconds is too small.");
        }

        try {
            Thread.sleep( (long) (sleepTime * 1000 / simulatorSpeed));
        } catch (InterruptedException e) {
            LOGGER.severe(e.getMessage() +"\n"+ Arrays.toString(e.getStackTrace()));
        }
    }

    public void setSimulatorSpeed(double simulatorSpeed){
        this.simulatorSpeed = simulatorSpeed;
    }



    @Override
    public String toString() {
        return "Drive{" +
                "id='" + id.substring(1) + '\'' +
                ", long id='" + id + '\'' +
                ", passengers=" + passengers.size() +
                ", leaveTime=" + FORMAT(leaveTime) +
                ", pathSize=" + path.getSize() +
                ", currNode=" + currNode.getId() +
                '}';
    }
}