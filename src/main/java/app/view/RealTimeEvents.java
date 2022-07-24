package app.view;

import app.model.*;
import app.model.interfaces.ElementsOnMap;
import utils.JsonHandler;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import static utils.LogHandler.LOGGER;
import static utils.Utils.FORMAT;
import static utils.Utils.lock;

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
public class RealTimeEvents implements Runnable{
    private final ReentrantLock lock;
    private final ExecutorService pool;
    private final Queue<ElementsOnMap> eventsQueue;
    private Date currTime;

    public RealTimeEvents() {
        lock = new ReentrantLock();
        pool = Executors.newCachedThreadPool();

        eventsQueue = UserMap.INSTANCE.getEventQueue();
        currTime = eventsQueue.peek().getStartTime();
    }

    @Override
    public void run() {
        LOGGER.fine("RealTimeEvents star running");

        while(!eventsQueue.isEmpty()){
            /*  poll new event and wait till it is his start time */
            ElementsOnMap newEvent = eventsQueue.poll();

            sleep(currTime.compareTo(newEvent.getStartTime()));

            /* jump in time to next event*/
            currTime = newEvent.getStartTime();

            /*  add new event */
            startEvent(newEvent);

            LOGGER.info("RealTimeEvents add "+newEvent +" event. at " + FORMAT(newEvent.getStartTime())+".");
        }

        JsonHandler.UserMapType.save();
        LOGGER.info("RealTimeEvents finished.");
    }

    private void startEvent(ElementsOnMap newEvent){
        try {
            lock.lock();
            lock(false);//todo combine
            if(newEvent instanceof Drive drive){
                UserMap.INSTANCE.startDrive(drive);
                pool.execute(drive);
            }else{
                UserMap.INSTANCE.startRequest((Rider) newEvent);
            }
        } finally {
            lock.unlock();
        }

    }

    /*
     * todo fix sleep time:
     *      double sleepTime = ms / timeSpeed;
     */
    private void sleep(long ms ) {
        long sleepTime = 3000;
        try {
            Thread.sleep(sleepTime);
        }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

}
