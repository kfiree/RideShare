package model;

import controller.utils.GraphAlgo;
import controller.utils.MapUtils;

import model.interfaces.ElementsOnMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static controller.utils.LogHandler.LOGGER;
import static view.StyleUtils.dateFormatter;

/**
 * todo make long way with stop-point instead of paths
 *      make drives thread-pool
 */
public class Drive implements Runnable, ElementsOnMap {
    private static final int MAX_PASSENGERS_NUM = 1; //todo set to 3
    private static final int PICKUP_MAX_ADDITION_TO_PATH = 10 ;

    private final String type, id;//todo is type needed?
//    private String[] passengers = new String[maxPassengersNum];
    private List<Pedestrian> passengers = new ArrayList<>();
    private final Date leaveTime;
    private final Path path;
    private Edge currEdge;
    private Node currNode, destination;
    private double simulatorSpeed;
    private double estimatedDistance;


    public Drive(@NotNull Path path, String type, String id, Date leaveTime) {
        this.path = path;
        this.type = type;
        this.id = id;
        this.leaveTime = leaveTime;
        simulatorSpeed = 2;
        currEdge = path.getEdges().get(0);
        currNode = path.get_src();
        estimatedDistance = GraphAlgo.distance(currNode.getCoordinates(), getDestination().getCoordinates());
    }

    public Drive(@NotNull Node src, @NotNull Node dst, String type, String id, Date leaveTime) {
        path = GraphAlgo.getShortestPath(src, dst);
        this.type = type;
        this.id = id;
        this.leaveTime = leaveTime;
        simulatorSpeed = 2;
        currEdge = path.getEdges().get(0);
        destination = dst;
        currNode = src;

    }



    @Override
    public void run() {
        LOGGER.fine("Drive "+ id +" started.");
        MapUtils.validate(path != null, "can't start a drive if path is null. Drive owner id - " + id);

//        Iterator<Path> pathIterator = paths.iterator();
//        while(pathIterator.hasNext()){
//            Path path = pathIterator.next();
//            pathIndex++;
//
        while(currNode!=getDestination()){
            runOnPath();
        }
//            LOGGER.finer("Drive "+ id +" finished path "+ pathIndex+" out of "+paths.size()+" paths.");
//        }
        UsersMap.INSTANCE.finished(this);

        LOGGER.finest("Drive "+ id +" finished!.");

//        do{
//
//            LOGGER.info("drive " + getId() + " will get to "+ currEdge.getOtherEnd(currNode.getId()).getOsmID()
//                    +" sleeps for "+ String.format("%.2f", currEdge.getWeight()) + " seconds.");
//
//            sleep(currEdge.getWeight());
//
//            currNode = currEdge.getOtherEnd(currNode.getId());
//
//            currEdge = paths.getNext();
//
//        }while(currEdge != null);
//
//        LOGGER.finer("Drive "+ ownerId +" has reached destination.");
    }

    private void runOnPath(){
        int edgesIndex = 0;
        Iterator<Edge> edgesIterator = path.iterator();

        while(edgesIterator.hasNext()){
            Edge nextEdge = edgesIterator.next();
            edgesIndex++;
            if(edgesIndex % 3 ==0) {
                LOGGER.info("driver (" + getId() + ") " + String.format("%.2f", edgesIndex / (double) path.getSize() * 100) + "% complete.");
            }

            sleep(nextEdge.getWeight());

            /* if car not full yet */
            if(!isFull()) {
                    synchronized (this) {
                        /*  find close passenger to pick up */
                        for (Pedestrian pedestrian : UsersMap.INSTANCE.getWaitingForRide()) {

                            /* check addition to path if picked up */
//                            double heuristicCurrPathLength = GraphAlgo.distance(getLocation(), getDestination().getCoordinates());
                            double heuristicNewPathLength = GraphAlgo.distance(getLocation(), pedestrian.getLocation())
                                    + GraphAlgo.distance(pedestrian.getLocation(), pedestrian.getDestination().getCoordinates())
                                    + GraphAlgo.distance(pedestrian.getDestination().getCoordinates(), getDestination().getCoordinates());


                            double additionToPath = Math.abs(heuristicNewPathLength - estimatedDistance);
                            if (additionToPath < PICKUP_MAX_ADDITION_TO_PATH) {
                                LOGGER.fine(
                                        "drive " + id + " on change path to pick up passenger: " + pedestrian.getId() +
                                                ".\nPath length heuristics:" +
                                                "\n * Prev path: " + estimatedDistance +
                                                "\n * Picking passenger: " + heuristicNewPathLength +
                                                "\n * Addition to path : " + additionToPath
                                );

                                pickPassenger(pedestrian);
                                return; /* return to run() to start new path */
                            }
                        }
                    }
                }
//            if(!UsersMap.INSTANCE.getPedestrians().isEmpty()){
//
//                UsersMap.INSTANCE.getPedestrians().forEach(pedestrian -> {
//                    if(GraphAlgo.distance(getLocation(), pedestrian.getLocation())
//                            +GraphAlgo.distance(pedestrian.getDestination().getCoordinates(), getDestination().getCoordinates())<5){
//
//                        Object[] objects = UsersMap.INSTANCE.getPedestrians().stream().toArray();
//                        pickPassenger((Pedestrian) objects[0]);
//                        return;
//                    }
//                });
//            }

            /* if drive on passenger node*/
            for(Pedestrian passenger: passengers){
                if(passenger.getCurrNode() == currNode){
                    System.out.println(getId()+" pick "+passenger.getId()+" up ");
                    UsersMap.INSTANCE.finished(passenger);
                }
            }

            currEdge = nextEdge;
            currNode = currEdge.getOtherEnd(currNode.getId());
        }
    }

    public void pickPassenger(Pedestrian pedestrian){
            pedestrian.take();

            /* extend path to pick up pedestrian */
            path.addMiddlePath(pedestrian.getPath(), currEdge, currNode);

            passengers.add(pedestrian);
//            paths.remove(currPath);
//            paths.add(GraphAlgo.getShortestPath(currNode, pedestrian.getCurrNode()));
//            paths.add(pedestrian.getPath());
//            paths.add(GraphAlgo.getShortestPath(pedestrian.getDestination(), getDestination()));
    }

    public boolean isFull(){
        return passengers.size() >= MAX_PASSENGERS_NUM;
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
    public Date getAskTime() {
        return leaveTime;
    }

    public Edge getCurrEdge() { return currEdge; }

    public List<Edge> getPath(){
        return path.getEdges();
    }

//    public void addMiddlePath(Path currPath) {
//        this.currPath = currPath;
//    }//TODO add multiple middle paths (TSP)

    private void sleep(double sleepTime ) {
        if(sleepTime <1 ){
            LOGGER.warning("drive " + getId()+" sleep time "+ String.format("%.2f", sleepTime) +" seconds is too small.");
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
                ", leaveTime=" + dateFormatter.format(leaveTime) +
                ", paths=" + path.getSize() +
                ", currNode=" + currNode +
                '}';
    }
}