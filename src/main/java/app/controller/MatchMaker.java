package app.controller;

import app.model.Drive;
import app.model.Node;
import app.model.Rider;
import app.model.UserMap;
import app.view.MapView;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

import static java.lang.Thread.sleep;
import static utils.LogHandler.LOGGER;
import static utils.Utils.lock;
import static utils.Utils.unLock;

/* todo replace matchBruteForce1Pickup(Drive drive) with matchBruteForce1Pickup( ) (without drivers calling this method).
 *      might use https://github.com/frankfarrell/kds4j
 * */
public class MatchMaker implements Runnable{
    private static final int MAX_KM_ADDITION_TO_PATH;
    private Simulator simulator;

    public MatchMaker(Simulator simulator) {
        this.simulator = simulator;
    }

    static{
         MAX_KM_ADDITION_TO_PATH = 10;
    }


    public synchronized void matchMultiplePickup(){
        try {
            lock(true);
            for (Rider rider : UserMap.INSTANCE.getPendingRequests()) {
                PriorityQueue<Drive> matches = new PriorityQueue<>(
                        Comparator.comparingDouble(drive -> detourCost(drive, rider))
                );

                UserMap.INSTANCE.getOnGoingDrives().forEach(drive -> {
                    if(drive.canSqueezeOneMore()){
                        matches.add(drive);
                    }
                });
                Drive bestMatch = matches.poll();
                if(bestMatch != null) {
                    System.out.println("Match " + bestMatch.getId() + " with " + rider.getId());
                    assert bestMatch != null;
                    bestMatch.addDetour(rider);
                    rider.markTaken();
                }
                return;
            }
        }finally {
            unLock();
        }
    }

    private double detourCost(Drive drive, Rider rider){
        Node driveSrc, riderSrc, driveDest, riderDest;

        driveSrc = drive.getCurrentNode();
        driveDest = drive.getDest();
        riderSrc = rider.getCurrentNode();
        riderDest = rider.getDest();

        return -1*( driveSrc.distanceTo(riderSrc) + driveDest.distanceTo(riderDest) + drive.getDetoursTime());

    }

    /** @return true if picked someone up */
    public synchronized void matchBruteForce1Pickup(){
        try {
            lock(true);
            Collection<Rider> requests = UserMap.INSTANCE.getPendingRequests();
            Collection<Drive> drives = UserMap.INSTANCE.getOnGoingDrives();

            for (Drive drive : drives) {
                if(drive.canSqueezeOneMore()) {
                    matchBruteForce1Pickup(drive, requests);
                }
            }
        } finally {
            unLock();
        }
    }


    public synchronized boolean matchBruteForce1Pickup(Drive drive, Collection<Rider> requests){

        /*  find close passenger to pick up */
        for (Rider rider : requests) {
            /* check addition to path if picked up */


            if(isWorthItBruteForceSolution(drive, rider)){
                rider.markTaken();
                drive.addDetour(rider);
                return true;
            }
        }

        return false;
    }


    /* heuristic for pickup*/

    private boolean isWorthItBruteForceSolution(Drive d, Rider p){
        double distanceTo = GraphAlgo.distance(d.getLocation(), p.getLocation()),
                addedPathDistance = GraphAlgo.distance(p.getLocation(), p.getDest().getLocation()),
                distanceFrom = GraphAlgo.distance(p.getDest().getLocation(), d.getDest().getLocation());

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

    private boolean isWorthItBitBetterSolution(Drive d, Rider p){
        double distanceTo = GraphAlgo.distance(d.getLocation(), p.getLocation()),

                distanceFrom = GraphAlgo.distance(p.getDest().getLocation(), d.getDest().getLocation());

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

    @Override
    public void run() {
        while(true){
            try {
                sleep((long) (15000/ simulator.speed()));
                if(!UserMap.INSTANCE.getPendingRequests().isEmpty()){
                    matchMultiplePickup();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
