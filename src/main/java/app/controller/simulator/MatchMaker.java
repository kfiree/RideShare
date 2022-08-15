package app.controller.simulator;

import app.model.utils.GraphAlgo;
import utils.DS.Latch;
import app.model.users.Driver;
import app.model.graph.Node;
import app.model.users.Rider;
import app.model.users.UserMap;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;

import static java.lang.Thread.sleep;
import static utils.logs.LogHandler.LOGGER;
import static utils.Utils.lock;
import static utils.Utils.unLock;

/* todo replace matchBruteForce1Pickup(Drive drive) with matchBruteForce1Pickup( ) (without drivers calling this method).
 *      might use https://github.com/frankfarrell/kds4j
 * */
public class MatchMaker implements Runnable, SimulatorThread {
    private final Latch latch;
    private static final int MAX_KM_ADDITION_TO_PATH, SECONDS_15;
    private Simulator simulator = Simulator.INSTANCE;
    private Date localTime;


    /* CONSTRUCTORS */

    public MatchMaker() {
        this.latch = Simulator.INSTANCE.getLatch();;
        localTime = UserMap.INSTANCE.getFirstEventTime();
    }

    static{
         MAX_KM_ADDITION_TO_PATH = 10;
         SECONDS_15 = 15000;
    }



    /* RUN */

    @Override
    public void run() {
        register(this);


        while(Simulator.INSTANCE.isAlive()){ //todo fix while by fixing 'simulator.isAlive()'
//            System.out.println("MatchMaker");
            latch.waitOnCondition();

            sleep(SECONDS_15);

            if(!UserMap.INSTANCE.getPendingRequests().isEmpty()){
                matchMultiplePickup();
            }
        }

        finish();
    }

    private void finish() {
        unregister(this);
        LOGGER.finest("MatchMaker finished!.");
    }



    /* MATCH ALGORITHMS */

    public synchronized void matchMultiplePickup(){
        try {
            lock(true);
            for (Rider rider : UserMap.INSTANCE.getPendingRequests()) {
                if(!rider.isTaken()) {
                    PriorityQueue<Driver> matches = new PriorityQueue<>(
                            Comparator.comparingDouble(drive -> detourCost(drive, rider))
                    );

                    UserMap.INSTANCE.getOnGoingDrives().forEach(drive -> {
                        if (drive.canSqueezeOneMore()) {
                            matches.add(drive);
                        }
                    });
                    Driver bestMatch = matches.poll();

                    if (bestMatch == null) return;
                    double matchHeuristic = bestMatch.distanceTo(rider);
                    if (matchHeuristic < MAX_KM_ADDITION_TO_PATH) {
//                        System.out.println("Match " + bestMatch.getId() + " with " + rider.getId() + ", match heuristic:" + matchHeuristic);
                        bestMatch.addPassenger(rider);
                    }
//                else{
//                    System.out.println("Match too expensive, " + bestMatch.getId() + " with " + rider.getId()+ ", match heuristic:" + matchHeuristic);
//                }

                    return;
                }
            }
        }finally {
            unLock();
        }
    }

    private double detourCost(Driver drive, Rider rider){
        Node driveSrc, riderSrc, driveDest, riderDest;

        driveSrc = drive.getLocation();
        driveDest = drive.getDestination();
        riderSrc = rider.getLocation();
        riderDest = rider.getDestination();

        return -1*( driveSrc.distanceTo(riderSrc) + driveDest.distanceTo(riderDest) + drive.getDetoursTime());

    }

    /** @return true if picked someone up */
    public synchronized void matchBruteForce1Pickup(){
        try {
            lock(true);
            Collection<Rider> requests = UserMap.INSTANCE.getPendingRequests();
            Collection<Driver> drives = UserMap.INSTANCE.getOnGoingDrives();

            for (Driver drive : drives) {
                if(drive.canSqueezeOneMore()) {
                    matchBruteForce1Pickup(drive, requests);
                }
            }
        } finally {
            unLock();
        }
    }

    public synchronized boolean matchBruteForce1Pickup(Driver drive, Collection<Rider> requests){

        /*  find close passenger to pick up */
        for (Rider rider : requests) {
            /* check addition to path if picked up */


            if(isWorthItBruteForceSolution(drive, rider)){
                rider.markTaken();
                drive.addPassenger(rider);
                return true;
            }
        }

        return false;
    }


    /* MATCH COST HEURISTIC */

    private boolean isWorthItBruteForceSolution(Driver d, Rider p){// good for 1 pickup only per drive
        double distanceTo = GraphAlgo.distance(d.getCoordinates(), p.getCoordinates()),
                addedPathDistance = GraphAlgo.distance(p.getCoordinates(), p.getCoordinates()),
                distanceFrom = GraphAlgo.distance(p.getDestination().getCoordinates(), d.getDestination().getCoordinates());

        double newPathLength_heuristic = distanceTo + addedPathDistance + distanceFrom;

        boolean worthIt = newPathLength_heuristic < d.getOriginalTime() + MAX_KM_ADDITION_TO_PATH;

        if(worthIt) {
            LOGGER.fine(
                    "drive " + d.getId() + " change path to pick up passenger: " + p.getId() +
                            ".\nPath length heuristics:" +
                            "\n * Prev path: " + d.getOriginalTime() +
                            "\n * new path: " + newPathLength_heuristic
            );
        }

        return worthIt;
    }

    private boolean isWorthItBitBetterSolution(Driver d, Rider p){
        double distanceTo = GraphAlgo.distance(d.getCoordinates(), p.getCoordinates()),

                distanceFrom = GraphAlgo.distance(p.getDestination().getCoordinates(), d.getDestination().getCoordinates());

        double newPathLength_heuristic = distanceTo + distanceFrom;

        boolean worthIt = newPathLength_heuristic < d.getOriginalTime() + MAX_KM_ADDITION_TO_PATH;

        if(worthIt) {
            LOGGER.fine(
                    "drive " + d.getId() + " change path to pick up passenger: " + p.getId() +
                            ".\nPath length heuristics:" +
                            "\n * Prev path: " + d.getOriginalTime() +
                            "\n * new path: " + newPathLength_heuristic
            );
        }

        return worthIt;
    }



    /* SETTERS */

    @Override
    public void setTime(Date date) {
        this.localTime = date;
    }



    /* GETTERS */

    @Override
    public Date getTime() {
        return localTime;
    }

}
