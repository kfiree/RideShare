package simulator.controller;

import simulator.model.users.User;
import simulator.model.utils.UserEdge;
import org.jgrapht.alg.matching.KuhnMunkresMinimalWeightBipartitePerfectMatching;
import utils.DS.Latch;
import simulator.model.users.Driver;
import road_map.model.graph.Node;
import simulator.model.users.Passenger;
import simulator.model.users.UserMap;

import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.PriorityQueue;

import static utils.logs.LogHandler.LOGGER;
import static utils.Utils.lock;
import static utils.Utils.unLock;

import org.jgrapht.*;
import org.jgrapht.graph.*;

/**
 *
 */
public class MatchMaker implements Runnable, SimulatorThread {
    private final Latch latch;
    private static final int MAX_KM_ADDITION_TO_PATH, SECONDS_15;
    private Date localTime;


    /* CONSTRUCTORS */

    public MatchMaker() {
        latch = Simulator.INSTANCE.getLatch();
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
            latch.waitOnCondition();

            sleep(SECONDS_15);

            if(!UserMap.INSTANCE.getLiveRequest().isEmpty()){
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
        UserMap.INSTANCE.getDrives().forEach(graph::addVertex);
        UserMap.INSTANCE.getLiveRequest().forEach(graph::addVertex);

        KuhnMunkresMinimalWeightBipartitePerfectMatching<User, UserEdge> hungarian =
                new KuhnMunkresMinimalWeightBipartitePerfectMatching<>(
                        graph,
                        new HashSet<>(UserMap.INSTANCE.getDrives()),
                        new HashSet<>(UserMap.INSTANCE.getLiveRequest())
                );

        hungarian.getMatching();
    }

    public synchronized void matchMultiplePickup(){
        try {
            lock(true);
            for (Passenger passenger : UserMap.INSTANCE.getLiveRequest()) {
                if(!passenger.isMatched()) {
                    PriorityQueue<Driver> matches = new PriorityQueue<>(
                            Comparator.comparingDouble(drive -> detourCost(drive, passenger))
                    );

                    UserMap.INSTANCE.getLiveDrives().forEach(drive -> {
                        if (drive.canSqueezeOneMore()) {
                            matches.add(drive);
                        }
                    });
                    Driver bestMatch = matches.poll();

                    if (bestMatch == null) {
                        return;
                    }
                    double matchHeuristic = bestMatch.distanceTo(passenger);
                    if (matchHeuristic < MAX_KM_ADDITION_TO_PATH) {
                        bestMatch.addPassenger(passenger);
                    }

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


    /* MATCH COST HEURISTIC */




    /* SETTERS */

    @Override
    public void setTime(Date date) {
        localTime = date;
    }



    /* GETTERS */

    @Override
    public Date getTime() {
        return localTime;
    }

}
