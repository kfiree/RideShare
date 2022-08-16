package app.controller.simulator;


import java.util.Date;

import app.view.frames.ChooseRegionFrame;
import utils.DS.Latch;
import app.view.MapView;
import utils.JsonHandler;
import app.model.graph.RoadMap;
import app.model.users.UserMap;

import static app.model.utils.RoadMapHandler.CreateMap;
import static app.model.utils.UserMapHandler.initRandomEvents;
import static utils.logs.LogHandler.LOGGER;
import static utils.Utils.validate;


public class Simulator implements Runnable, SimulatorThread {
    private final Latch latch;
    private static long SLEEP_BETWEEN_FRAMES;
    private double speed;
    private boolean show;

    private EventManager eventManager;
    private MatchMaker cupid;
    private Thread eventsThread, cupidThread;
    private Date localTime;
    private final MapView mapView = MapView.INSTANCE;


    /* CONSTRUCTORS */

    private Simulator(){
        SLEEP_BETWEEN_FRAMES = 2000;
        this.latch = new Latch();
    }

    /** Singleton specific properties */
    public static final Simulator INSTANCE = new Simulator();

    public void init(double simulatorSpeed, int requestNum, int driveNum, boolean show, boolean bounds, boolean createFromPBF){
        validate(simulatorSpeed > 0, "Illegal simulator speed "+ simulatorSpeed + ".");

        String region = ChooseRegionFrame.choose();
//        String region = "tlv.json";

        if (createFromPBF || region.equals("Custom")) {
            LOGGER.info( "Start parsing main map.");
            CreateMap(ChooseRegionFrame.chooseFile());
            JsonHandler.RoadMapType.save();
        } else {
            LOGGER.info( "Read road map from JSON.");
            JsonHandler.RoadMapType.load(region);
        }

        LOGGER.info("Map is ready. Map = " + RoadMap.INSTANCE);

//         initEventsInLine(requestNum);
        initRandomEvents(driveNum, requestNum);

        this.speed = simulatorSpeed;
        this.show = show;

    }



    /* RUN */

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

        // choose simulator
        eventsThread = new Thread(eventManager);
        eventsThread.start();

        // choose cupid
        cupidThread = new Thread(cupid);
        cupidThread.start();


        latch.lock();

        do{
//            System.out.println("Simulator");
            latch.waitOnCondition();

            if(show) {
                updateFrame();
            }

            sleep(SLEEP_BETWEEN_FRAMES);
        }while(isAlive());

        finish();
    }

    private void finish(){
        unregister(this);
        LOGGER.info("Simulator finished!.");
    }

    private void updateFrame(){
        mapView.update();
    }

    public boolean isAlive(){
        return eventsThread.isAlive() || !UserMap.INSTANCE.getDrives().isEmpty();
    }



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

    public Latch getLatch() {
        return this.latch;
    }

    public void getState(){
        UserMap.INSTANCE.getOnGoingDrives();
        UserMap.INSTANCE.getPendingRequests();
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
