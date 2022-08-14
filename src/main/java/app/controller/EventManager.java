package app.controller;

import app.model.users.Driver;
import app.model.users.Rider;
import app.model.users.User;
import app.model.users.UserMap;
import utils.JsonHandler;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import static app.controller.UserMapHandler.printUserMapState;
import static utils.LogHandler.LOGGER;
import static utils.Utils.*;

/**
 * TODO
 *      * thread managing:
 *          - cyclicBarrier & Phaser
 *              src:
 *              * my question - https://stackoverflow.com/questions/73027321/executorservice-make-all-threads-wait
 *              * https://www.baeldung.com/java-cyclic-barrier#:~:text=A%20CyclicBarrier%20is%20a%20synchronizer,common%20point%20before%20continuing%20execution.
 *              * https://www.geeksforgeeks.org/java-util-concurrent-cyclicbarrier-java/
 *              * read://https_www.geeksforgeeks.org/?url=https%3A%2F%2Fwww.geeksforgeeks.org%2Fjava-util-concurrent-phaser-class-in-java-with-examples%2F
 *          - pool
 *              src:
 *              * https://www.baeldung.com/java-executor-service-tutorial
 *          -
 *      * time unit conversion:
 *          - check first!          ===>          https://stackoverflow.com/questions/21852754/convert-minutes-to-milliseconds-java-android
 *          - https://www.geeksforgeeks.org/timeunit-convert-method-in-java-with-examples/#:~:text=The%20convert()%20method%20of,occur%20while%20using%20this%20method.
 *          - https://www.baeldung.com/java-add-hours-date
 *          - https://stackabuse.com/how-to-get-the-number-of-days-between-dates-in-java/
 */
public class EventManager implements Runnable, SimulatorThread {
    private final Latch latch;
    private final ReentrantLock reentrantLock;
    private final ExecutorService pool;
    private final Queue<User> eventsQueue;
    private Date localTime;
    private final Simulator simulator;


    /* CONSTRUCTORS */

    public EventManager() {
        this.latch = Latch.INSTANCE;
        reentrantLock = new ReentrantLock();
        pool = Executors.newCachedThreadPool();

        localTime = UserMap.INSTANCE.getFirstEventTime();
        eventsQueue = UserMap.INSTANCE.getEventQueue();
        this.simulator = Simulator.INSTANCE;

        printUserMapState();
    }



    /* RUN */

    @Override
    public void run() {
        register(this);

        LOGGER.fine("RealTimeEvents star running");


        while(!eventsQueue.isEmpty()){
            /*  poll new event and wait till it is his start time */
            User newEvent = eventsQueue.poll();

            long timeDiff = timeDiff(localTime, newEvent.getStartTime());
            sleep(timeDiff);
//            sleep(currTime.compareTo(newEvent.getStartTime()));

            /* jump in time to next event*/
            localTime = newEvent.getStartTime();

            /*  add new event */
            startEvent(newEvent);

            LOGGER.info("RealTimeEvents add: " + newEvent
            + "\nevent time "+ FORMAT(newEvent.getStartTime()) + "."
            + "\nSimulator time "+ FORMAT(simulator.time()) + ".");

//            System.out.println("eventManager");
            latch.waitOnCondition();

        }

        finish();

    }

    private void finish(){
        JsonHandler.UserMapType.save();
        LOGGER.info("EventManager finished!.");
        unregister(this);
    }

    private void startEvent(User newEvent){
//        System.out.println("Starting " + newEvent);
        try {
            reentrantLock.lock();
            lock(false);//todo combine
            if(newEvent instanceof Driver drive){
                UserMap.INSTANCE.startDrive(drive);
                pool.execute(drive);
            }else{
                UserMap.INSTANCE.startRequest((Rider) newEvent);
            }
        } finally {
            reentrantLock.unlock();
        }

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

    public ExecutorService getPool() {
        return pool;
    }
}
