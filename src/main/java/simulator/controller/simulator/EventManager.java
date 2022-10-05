package simulator.controller.simulator;

import utils.DS.Latch;
import simulator.model.users.Driver;
import simulator.model.users.Passenger;
import simulator.model.users.User;
import simulator.model.users.UserMap;
import utils.JsonHandler;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import static simulator.model.utils.UserMapHandler.printUserMapState;
import static utils.logs.LogHandler.LOGGER;
import static utils.Utils.*;

/**

 */
public class EventManager implements Runnable, SimulatorThread {
    private final Latch latch;
    private final ReentrantLock reentrantLock;
    private final ExecutorService pool;
    private final Queue<User> eventsQueue;
    private Date localTime;


    /* CONSTRUCTORS */

    public EventManager() {
        latch = Simulator.INSTANCE.getLatch();
        reentrantLock = new ReentrantLock();
        pool = Executors.newCachedThreadPool();

        localTime = UserMap.INSTANCE.getFirstEventTime();
        eventsQueue = UserMap.INSTANCE.getEventQueue();

        printUserMapState();
    }



    /* RUN */

    @Override
    public void run() {
        register(this);

        LOGGER.fine("RealTimeEvents star running");


        while(!eventsQueue.isEmpty()){
            /*  poll new event and wait till it is his choose time */
            User newEvent = eventsQueue.poll();

            long timeDiff = timeDiff(localTime, newEvent.getStartTime());
            sleep(timeDiff);
//            sleep(currTime.compareTo(newEvent.getStartTime()));

            /* jump in time to next event*/
            localTime = newEvent.getStartTime();

            /*  add new event */
            startEvent(newEvent);
            LOGGER.info("RealTimeEvents add: " + newEvent +".");
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
        try {
            reentrantLock.lock();
            lock(false);//todo combine
            if(newEvent instanceof Driver drive){
                UserMap.INSTANCE.startDrive(drive);
                pool.execute(drive);
            }else{
                UserMap.INSTANCE.startRequest((Passenger) newEvent);
            }
        } finally {
            reentrantLock.unlock();
        }

    }



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

    public ExecutorService getPool() {
        return pool;
    }
}
