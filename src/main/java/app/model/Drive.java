package app.model;

/* third party */
import java.util.*;
import app.model.interfaces.ElementsOnMap;
import org.jetbrains.annotations.NotNull;

/* local */
import app.controller.GraphAlgo;
import app.controller.MapUtils;

/* static imports */
import static app.Utils.FORMAT;
import static utils.LogHandler.LOGGER;
import static app.controller.MatchMaker.pickIfWorthIt;


/**
 * todo make long way with stop-point instead of paths
 *      make drives thread-pool
 */
public class Drive implements Runnable, ElementsOnMap {
    private static final int MAX_PASSENGERS_NUM = 1; //todo set to 3
    private final String type, id;  //todo is type needed?
    private List<Pedestrian> passengers = new ArrayList<>();
    private final Date leaveTime;
    private Path path;
    private Edge currEdge;
    private Node currNode, destination;
    private double simulatorSpeed, estimatedDistance;
    private boolean pathChange;

    private Drive(String type, String id, Date leaveTime) {
        this.type = type;
        this.id = id;
        this.leaveTime = leaveTime;
    }

    public Drive(@NotNull Path path, String type, String id, Date leaveTime) {
        this(type, id, leaveTime);
        initPathVariables(path);
    }

    public Drive(@NotNull Node src, @NotNull Node dst, String type, String id, Date leaveTime) {
        this(type, id, leaveTime);
        initPathVariables(GraphAlgo.getShortestPath(src, dst));
    }

    public void initPathVariables(@NotNull Path path) {
        this.path = path;
        currEdge = path.getEdges().get(0);
        currNode = path.get_src();
        destination = path.get_dest();
        estimatedDistance = GraphAlgo.distance(currNode.getCoordinates(), getDestination().getCoordinates());
    }




    @Override
    public void run() {
        LOGGER.fine("Drive "+ id +" started.");
        MapUtils.validate(path != null, "can't start a drive if path is null. Drive owner id - " + id);

        while(currNode!=getDestination()){
            runOnPath();
        }

        UserMap.INSTANCE.finished(this);

        LOGGER.finest("Drive "+ id +" finished!.");
    }

    private void runOnPath(){
        pathChange = false;

        int edgesIndex = 0;
        Iterator<Edge> edgesIterator = path.iterator();

        while(edgesIterator.hasNext()){
            Edge nextEdge = edgesIterator.next();
            edgesIndex++;
            if(edgesIndex % 3 ==0) {
                LOGGER.info("driver (" + getId() + ") " + FORMAT( edgesIndex / (double) path.getSize() * 100) + "% complete.");
            }

            sleep(nextEdge.getWeight());

            /* pick passenger on the way */
            for(Pedestrian passenger: passengers){
                if(passenger.getCurrNode() == currNode){
                    System.out.println(getId()+" pick "+passenger.getId()+" up ");
                    UserMap.INSTANCE.finished(passenger);
                }
            }

            /* search new passengers to pick */
            if(!isFull()) {
                pathChange = pickIfWorthIt(this);
            }

            if(pathChange){
                return;
            }else{
                currEdge = nextEdge;
                currNode = currEdge.getOtherEnd(currNode.getId());
            }
        }
    }

    public void pickPassenger(Pedestrian pedestrian){
            /* extend path to pick up pedestrian */
            path.addMiddlePath(pedestrian.getPath(), currNode); // todo change estimatedDistance and save on path

            passengers.add(pedestrian);
//            paths.remove(currPath);
//            paths.add(GraphAlgo.getShortestPath(currNode, pedestrian.getCurrNode()));
//            paths.add(pedestrian.getEdges());
//            paths.add(GraphAlgo.getShortestPath(pedestrian.getDestination(), getDestination()));
    }

    public boolean isFull(){
        return passengers.size() >= MAX_PASSENGERS_NUM;
    }

    public double getEstimatedDistance() {
        return estimatedDistance;
    }

    @Override
    public GeoLocation getLocation(){
        return currEdge == null? null : currNode.getCoordinates();
    }

    @Override
    public Node getDestination() {
        return path.get_dest();
//        return paths.get(paths.size() -1).get_dest(); // TODO check if last
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

    public List<Pedestrian> getPassengers() {
        return passengers;
    }

    @Override
    public Date getStartTime() {
        return leaveTime;
    }

    public Edge getCurrEdge() { return currEdge; }

    @Override
    public Path getPath(){
        return path;
    }

    public List<Edge> getEdges(){
        return path.getEdges();
    }

//    public void addMiddlePath(Path currPath) {
//        this.currPath = currPath;
//    }//TODO add multiple middle paths (TSP)

    private void sleep(double sleepTime ) {
        if(sleepTime <1 ){
            LOGGER.warning("drive " + getId()+" sleep time "+ FORMAT(sleepTime) +" seconds is too small.");
        }

        try { Thread.sleep( (long) (sleepTime * 1000 / simulatorSpeed)) ; }
        catch (InterruptedException e) {
            LOGGER.severe(e.getMessage() +"\n"+ Arrays.toString(e.getStackTrace()));
        }
    }

    public void setSimulatorSpeed(double simulatorSpeed){
        this.simulatorSpeed = simulatorSpeed;
    }



    @Override
    public String toString() {
        return "Drive{" +
                ", ownerId='" + id + '\'' +
                ", passengers=" + passengers.size() +
                ", leaveTime=" + FORMAT(leaveTime) +
                ", paths=" + path.getSize() +
                ", currNode=" + currNode +
                '}';
    }
}