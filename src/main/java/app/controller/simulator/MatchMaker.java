package app.controller.simulator;

import app.model.users.User;
import app.model.utils.GraphAlgo;
import app.model.utils.UserEdge;
import org.jgrapht.alg.matching.KuhnMunkresMinimalWeightBipartitePerfectMatching;
import utils.DS.Latch;
import app.model.users.Driver;
import app.model.graph.Node;
import app.model.users.Passenger;
import app.model.users.UserMap;

import java.util.*;

import static java.lang.Thread.sleep;
import static utils.logs.LogHandler.LOGGER;
import static utils.Utils.lock;
import static utils.Utils.unLock;

import org.jgrapht.*;
import org.jgrapht.graph.*;

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

    public void shtuiut() {
        Graph<User, UserEdge> graph = new SimpleGraph<>(UserEdge.class);
        UserMap.INSTANCE.getDrives().forEach(driver -> graph.addVertex(driver));
        UserMap.INSTANCE.getPendingRequests().forEach(request -> graph.addVertex(request));

        KuhnMunkresMinimalWeightBipartitePerfectMatching<User, UserEdge> hungarian =
                new KuhnMunkresMinimalWeightBipartitePerfectMatching<>(
                        graph,
                        new HashSet<>(UserMap.INSTANCE.getDrives()),
                        new HashSet<>(UserMap.INSTANCE.getPendingRequests())
                );

        hungarian.getMatching();
    }

    public synchronized void matchMultiplePickup(){
        try {
            lock(true);
            for (Passenger passenger : UserMap.INSTANCE.getPendingRequests()) {
                if(!passenger.isMatched()) {
                    PriorityQueue<Driver> matches = new PriorityQueue<>(
                            Comparator.comparingDouble(drive -> detourCost(drive, passenger))
                    );

                    UserMap.INSTANCE.getOnGoingDrives().forEach(drive -> {
                        if (drive.canSqueezeOneMore()) {
                            matches.add(drive);
                        }
                    });
                    Driver bestMatch = matches.poll();

                    if (bestMatch == null) return;
                    double matchHeuristic = bestMatch.distanceTo(passenger);
                    if (matchHeuristic < MAX_KM_ADDITION_TO_PATH) {
//                        System.out.println("Match " + bestMatch.getId() + " with " + rider.getId() + ", match heuristic:" + matchHeuristic);
                        bestMatch.addPassenger(passenger);
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

    private double detourCost(Driver drive, Passenger passenger){
        Node driveSrc, riderSrc, driveDest, riderDest;

        driveSrc = drive.getLocation();
        driveDest = drive.getFinalDestination();
        riderSrc = passenger.getLocation();
        riderDest = passenger.getFinalDestination();

        return -1*( driveSrc.distanceTo(riderSrc) + driveDest.distanceTo(riderDest) + drive.getDetoursTime());

    }

    /** @return true if picked someone up */
    public synchronized void matchBruteForce1Pickup(){
        try {
            lock(true);
            Collection<Passenger> requests = UserMap.INSTANCE.getPendingRequests();
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

    public synchronized boolean matchBruteForce1Pickup(Driver drive, Collection<Passenger> requests){

        /*  find close passenger to pick up */
        for (Passenger passenger : requests) {
            /* check addition to path if picked up */


            if(isWorthItBruteForceSolution(drive, passenger)){
                passenger.markMatched();
                drive.addPassenger(passenger);
                return true;
            }
        }

        return false;
    }


    /* MATCH COST HEURISTIC */

    private boolean isWorthItBruteForceSolution(Driver d, Passenger p){// good for 1 pickup only per drive
        double distanceTo = GraphAlgo.distance(d.getCoordinates(), p.getCoordinates()),
                addedPathDistance = GraphAlgo.distance(p.getCoordinates(), p.getCoordinates()),
                distanceFrom = GraphAlgo.distance(p.getFinalDestination().getCoordinates(), d.getFinalDestination().getCoordinates());

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

    private boolean isWorthItBitBetterSolution(Driver d, Passenger p){
        double distanceTo = GraphAlgo.distance(d.getCoordinates(), p.getCoordinates()),

                distanceFrom = GraphAlgo.distance(p.getFinalDestination().getCoordinates(), d.getFinalDestination().getCoordinates());

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
