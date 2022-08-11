package app.controller;

import app.model.graph.RoadMap;
import app.model.users.UserMap;
import app.view.MapView;
import utils.JsonHandler;
import utils.SimulatorLatch;

import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;
//import java.util.concurrent.BrokenBarrierException;
//import java.util.concurrent.CyclicBarrier;

import static app.controller.RoadMapHandler.CreateMap;
import static app.controller.UserMapHandler.initRandomEvents;
import static utils.LogHandler.LOGGER;
import static utils.Utils.*;

public class Simulator implements Runnable, TimeSync{
    private static long SLEEP_BETWEEN_FRAMES;
    private double speed;
    private boolean show;

    private EventManager eventManager;
    private MatchMaker cupid;
    private Thread eventsThread, cupidThread;
    private Date localTime;
    private final MapView mapView = MapView.instance;
    private ReentrantLock simulatorLock = new ReentrantLock();
    private SimulatorLatch latch;
//    private ArrayList<Object> threads = new ArrayList<>();
//    public final CyclicBarrier cyclicBarrier;


    /* CONSTRUCTORS */

    private Simulator(){
//        this.cyclicBarrier = new CyclicBarrier(2);
        SLEEP_BETWEEN_FRAMES = 2000;
    }

    /** Singleton specific properties */
    public static final Simulator INSTANCE = new Simulator();

    public void init(double simulatorSpeed, int requestNum, int driveNum, boolean show, boolean bounds, boolean createFromPBF){
        validate(simulatorSpeed > 0, "Illegal simulator speed "+ simulatorSpeed + ".");
        this.latch = SimulatorLatch.INSTANCE;
        RoadMapHandler.setBounds(bounds);

        if (createFromPBF) {
            LOGGER.info( "Start parsing main map.");
            CreateMap();
            JsonHandler.RoadMapType.save();
        } else {
            LOGGER.info( "Read road map from JSON.");
            JsonHandler.RoadMapType.load();
        }

        LOGGER.info("Map is ready. Map = " + RoadMap.INSTANCE);

        // initEventsInLine(requestNum);
        initRandomEvents(driveNum, requestNum);

        this.speed = simulatorSpeed;
        this.show = show;

    }



    /* RUN */
//int i = 0;
    @Override
    public void run() {
        localTime = UserMap.INSTANCE.getFirstEventTime();

        register(this);

        this.eventManager = new EventManager();
        this.cupid = new MatchMaker();
        if(show) {
            mapView.init(this);

            updateFrame();
            sleep(SLEEP_BETWEEN_FRAMES);
        }

        // start simulator
        eventsThread = new Thread(eventManager);
        eventsThread.start();

        // start cupid
        cupidThread = new Thread(cupid);
        cupidThread.start();

        if(show){
            do{
                sleep(SLEEP_BETWEEN_FRAMES);
                updateFrame();
//                i++;
//                if(i%10 == 0){
//                    TimeSync.showThreadsData();
//                }
            }while(isAlive());
        }

        finish();
    }

    private void finish(){
        unregister(this);
        LOGGER.info("Simulator finished!.");
    }

    private void updateFrame(){
        mapView.update();

        latch.waitIfPause();
    }

    public boolean isAlive(){
        return eventsThread.isAlive() || !UserMap.INSTANCE.getDrives().isEmpty();
    }

//    private void sleep() {
//        localTime = new Date(localTime.getTime()+SLEEP_BETWEEN_FRAMES);
//
//        try {
//            Thread.sleep((long) (SLEEP_BETWEEN_FRAMES/this.speed()));
//            addTime(SLEEP_BETWEEN_FRAMES);
//        }catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }



    /* GETTERS */

    public Date time() {
        return localTime;
    }

    public double speed() {
        return speed;
    }

    public EventManager events() {
        return eventManager;
    }

    public Thread eventsThread() {
        return eventsThread;
    }

    public Thread cupidThread() {
        return cupidThread;
    }

    @Override
    public Date getTime() {
        return localTime;
    }



    /* SETTERS */

    public void setSpeed(double simulatorSpeed) {
        this.speed = simulatorSpeed;
    }

    public void setEventsManager(EventManager events) {
        this.eventManager = events;
    }

    @Override
    public void setTime(Date date) {
        this.localTime = date;
    }

}
