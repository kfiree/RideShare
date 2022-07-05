package app.controller;

import app.model.Drive;
import app.model.Pedestrian;
import app.model.UserMap;

import java.util.Collection;

import static utils.LogHandler.LOGGER;

/** todo replace pickIfWorthIt(Drive drive) with pickIfWorthIt( ) (without drivers calling this method). */
public class MatchMaker implements Runnable{
    private static final int MAX_KM_ADDITION_TO_PATH;

    static{
         MAX_KM_ADDITION_TO_PATH = 10;
    }

    public static synchronized void pickIfWorthIt(){

        Collection<Pedestrian> requests = UserMap.INSTANCE.getRequests();
        Collection<Drive> drives = UserMap.INSTANCE.getDrives();

        for(Drive drive: drives) {
            /*  find close passenger to pick up */
            for (Pedestrian pedestrian : requests) {
                /* check addition to path if picked up */

                if (isWorthIt(drive, pedestrian)) {
                    pedestrian.markTaken();
                    drive.pickPassenger(pedestrian);
                }
            }
        }
    }

    /**
     *
     * @param drive
     * @return true if picked someone up
     */
    public static synchronized boolean pickIfWorthIt(Drive drive){

        /*  find close passenger to pick up */
        for (Pedestrian pedestrian : UserMap.INSTANCE.getRequests()) {
            /* check addition to path if picked up */


            if(isWorthIt(drive, pedestrian)){
                pedestrian.markTaken();
                drive.pickPassenger(pedestrian);
                return true;
            }
        }

        return false;
    }

    /** heuristic for pickup*/
    private static boolean isWorthIt(Drive d, Pedestrian p){
        double distanceTo = GraphAlgo.distance(d.getLocation(), p.getLocation()),
                addedPathDistance = GraphAlgo.distance(p.getLocation(), p.getDestination().getCoordinates()),
                distanceFrom = GraphAlgo.distance(p.getDestination().getCoordinates(), d.getDestination().getCoordinates());

        double newPathLength_heuristic = distanceTo + addedPathDistance + distanceFrom;

        boolean worthIt = newPathLength_heuristic < d.getEstimatedDistance() + MAX_KM_ADDITION_TO_PATH;

        if(worthIt) {
            LOGGER.fine(
                    "drive " + d.getId() + " change path to pick up passenger: " + p.getId() +
                            ".\nPath length heuristics:" +
                            "\n * Prev path: " + d.getEstimatedDistance() +
                            "\n * new path: " + newPathLength_heuristic
            );
        }

        return worthIt;
    }

    @Override
    public void run() {

    }
}
