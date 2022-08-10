package app.controller;

import app.model.users.Driver;
import app.model.users.Rider;
import app.model.users.User;
import app.model.users.UserMap;
import utils.JsonHandler;
import utils.SimulatorLatch;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

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
public class EventManager implements Runnable{
    private final ReentrantLock lock;
    private final ExecutorService pool;
    private final Queue<User> eventsQueue;
    private Date currTime;
    private final Simulator simulator;
    private SimulatorLatch latch;

    public EventManager() {
        lock = new ReentrantLock();
        pool = Executors.newCachedThreadPool();

        eventsQueue = UserMap.INSTANCE.getEventQueue();
        if(eventsQueue.isEmpty()){
            currTime = new Date();
        }else {
            currTime = eventsQueue.peek().getStartTime();
        }
        this.simulator = Simulator.INSTANCE;
    }

    @Override
    public void run() {
        LOGGER.fine("RealTimeEvents star running");

        this.latch  = SimulatorLatch.INSTANCE;

        while(!eventsQueue.isEmpty()){
            /*  poll new event and wait till it is his start time */
            User newEvent = eventsQueue.poll();

            long timeDiff = timeDiff(currTime, newEvent.getStartTime());
            sleep(timeDiff);
//            sleep(currTime.compareTo(newEvent.getStartTime()));

            /* jump in time to next event*/
            currTime = newEvent.getStartTime();

            /*  add new event */
            startEvent(newEvent);

            LOGGER.info("RealTimeEvents add: "//+newEvent +" event. at " + FORMAT(newEvent.getStartTime())+".");
            + "\nevent time "+ FORMAT(newEvent.getStartTime()) + "."
            + "\nSimulator time "+ FORMAT(simulator.time()) + ".");

            latch.waitIfPause();
        }

        finish();

    }

    private void finish(){
        JsonHandler.UserMapType.save();
        System.out.println("RealTimeEvents done.");
        LOGGER.info("RealTimeEvents done.");
//        try {
//        System.out.println("cyclicBarrier "+ simulator.cyclicBarrier.getNumberWaiting());
////            simulator.cyclicBarrier.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (BrokenBarrierException e) {
//            e.printStackTrace();
//        }
    }

    private void startEvent(User newEvent){
        try {
            lock.lock();
            lock(false);//todo combine
            if(newEvent instanceof Driver drive){
                UserMap.INSTANCE.startDrive(drive);
                pool.execute(drive);
            }else{
                UserMap.INSTANCE.startRequest((Rider) newEvent);
            }
        } finally {
            lock.unlock();
        }

    }

    public Date getTime() {
        return currTime;
    }

    /*
     * todo fix sleep time:
     *      double sleepTime = ms / timeSpeed;
     */
    private void sleep(long sleepTime ) {
//        long sleepTime = 3000;
        try {
            Thread.sleep((long) (sleepTime/ Simulator.INSTANCE.speed()));
        }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    public ExecutorService getPool() {
        return pool;
    }
}
