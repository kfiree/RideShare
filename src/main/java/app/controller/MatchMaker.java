package app.controller;

import app.model.Drive;
import app.model.Rider;
import app.model.UserMap;

import java.util.Collection;

import static java.lang.Thread.sleep;
import static utils.LogHandler.LOGGER;
import static utils.Utils.lock;
import static utils.Utils.unLock;

/* todo replace match(Drive drive) with match( ) (without drivers calling this method).
 *      might use https://github.com/frankfarrell/kds4j
 * */
public class MatchMaker implements Runnable{
    private static final int MAX_KM_ADDITION_TO_PATH;
    private MatchMaker() {}

    public static final MatchMaker INSTANCE = new MatchMaker();

    static{
         MAX_KM_ADDITION_TO_PATH = 10;
    }

    public void init(){

    }

    public synchronized void match(){
        try {
            lock(true);
            Collection<Rider> requests = UserMap.INSTANCE.getPendingRequests();
            Collection<Drive> drives = UserMap.INSTANCE.getOnGoingDrives();

            for (Drive drive : drives) {
                if(!drive.isFull()) {
                    match(drive, requests);
                }
            }
        } finally {
            unLock();
        }
    }

    /** @return true if picked someone up */
    public synchronized boolean match(Drive drive, Collection<Rider> requests){

        /*  find close passenger to pick up */
        for (Rider rider : requests) {
            /* check addition to path if picked up */


            if(isWorthItBruteForceSolution(drive, rider)){
                rider.markTaken();
                drive.pickPassenger(rider);
                return true;
            }
        }

        return false;
    }


    /* heuristic for pickup*/

    private boolean isWorthItBruteForceSolution(Drive d, Rider p){
        double distanceTo = GraphAlgo.distance(d.getLocation(), p.getLocation()),
                addedPathDistance = GraphAlgo.distance(p.getLocation(), p.getDestination().getLocation()),
                distanceFrom = GraphAlgo.distance(p.getDestination().getLocation(), d.getDestination().getLocation());

        double newPathLength_heuristic = distanceTo + addedPathDistance + distanceFrom;

        boolean worthIt = newPathLength_heuristic < d.getTotalTime() + MAX_KM_ADDITION_TO_PATH;

        if(worthIt) {
            LOGGER.fine(
                    "drive " + d.getId() + " change path to pick up passenger: " + p.getId() +
                            ".\nPath length heuristics:" +
                            "\n * Prev path: " + d.getTotalTime() +
                            "\n * new path: " + newPathLength_heuristic
            );
        }

        return worthIt;
    }

    private boolean isWorthItBitBetterSolution(Drive d, Rider p){
        double distanceTo = GraphAlgo.distance(d.getLocation(), p.getLocation()),

                distanceFrom = GraphAlgo.distance(p.getDestination().getLocation(), d.getDestination().getLocation());

        double newPathLength_heuristic = distanceTo + distanceFrom;

        boolean worthIt = newPathLength_heuristic < d.getTotalTime() + MAX_KM_ADDITION_TO_PATH;

        if(worthIt) {
            LOGGER.fine(
                    "drive " + d.getId() + " change path to pick up passenger: " + p.getId() +
                            ".\nPath length heuristics:" +
                            "\n * Prev path: " + d.getTotalTime() +
                            "\n * new path: " + newPathLength_heuristic
            );
        }

        return worthIt;
    }

    @Override
    public void run() {
        while(true){
            try {
                sleep(15000);
                match();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
