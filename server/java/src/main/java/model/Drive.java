package model;

import controller.utils.MapUtils;

import model.interfaces.ElementsOnMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static controller.utils.LogHandler.LOGGER;
import static view.StyleUtils.dateFormatter;

public class Drive implements Runnable, ElementsOnMap {
    private final String type, ownerId;//todo is type needed?
    private String[] passengers = new String[3];
    private final Date leaveTime;
    private final List<Path> paths = new ArrayList<>();
    private Path currPath;
    private Edge currEdge;
    private Node currNode;
    private double simulatorSpeed;


    public Drive(@NotNull Path path, String type, String ownerId, Date leaveTime) {
        paths.add(path);// = paths;
        this.type = type;
        this.ownerId = ownerId;
        this.leaveTime = leaveTime;
        simulatorSpeed = 2;
        currPath = paths.get(0);
        currEdge = currPath.getEdges().get(0);
        currNode = currPath.get_src();

    }

    @Override
    public void run() {
        LOGGER.fine("Drive "+ ownerId +" started.");
        MapUtils.validate(paths != null, "can't start a drive if path is null. Drive owner id - " + ownerId);
        int pathIndex =0;

        Iterator<Path> pathIterator = paths.iterator();
        while(pathIterator.hasNext()){
            Path path = pathIterator.next();
            pathIndex++;

            int edgesIndex = 0;
            Iterator<Edge> edgesIterator = path.iterator();

            while(edgesIterator.hasNext()){
                Edge nextEdge = edgesIterator.next();
                edgesIndex++;

                LOGGER.info("driver " + getId() + " will get to "+ nextEdge.getOtherEnd(currNode.getId()).getOsmID()
                        +" in "+ String.format("%.2f", nextEdge.getWeight()) + " seconds ("+ edgesIndex/path.getSize() +"% of the path)");

                sleep(nextEdge.getWeight());

                currEdge = nextEdge;
                currNode = currEdge.getOtherEnd(currNode.getId());
            }

            LOGGER.finer("Drive "+ ownerId +" finished path "+ pathIndex+" out of "+paths.size()+" paths.");
        }
        LOGGER.finest("Drive "+ ownerId +" finished!.");

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

    @Override
    public GeoLocation getLocation(){
        return currEdge == null? null : currNode.getCoordinates();
    }

    @Override
    public Node getDestination() {
        return paths.get(paths.size() -1).get_dest(); // TODO check if last
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
        return ownerId;
    }

    public String[] getPassengers() {
        return passengers;
    }

    @Override
    public Date getStartTime() {
        return leaveTime;
    }

    public void setPassengers(String[] passengers) {
        this.passengers = passengers;
    }

    public Edge getCurrEdge() { return currEdge; }

    public List<Edge> getCurrPath(){
        return currPath.getEdges();
    }

    public void addMiddlePath(Path currPath) {
        this.currPath = currPath;
    }//TODO add multiple middle paths (TSP)

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

//    @Override
//    public String toString() {
//        return "Drive{" +
//                "driverType='" + type + '\'' +
//                ", driveOwnerId='" + ownerId + '\'' +
//                ", passengers=" + Arrays.toString(passengers) +
//                ", leaveTime=" + leaveTime +
//                ", path=" + paths +
//                ", currentEdge=" + currEdge +
//                '}';
//    }


    @Override
    public String toString() {
        return "Drive{" +
                ", ownerId='" + ownerId + '\'' +
                ", passengers=" + Arrays.toString(passengers) +
                ", leaveTime=" + dateFormatter.format(leaveTime) +
                ", paths=" + paths.size() +
                ", currNode=" + currNode +
                '}';
    }
}