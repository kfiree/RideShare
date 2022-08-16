package app.controller.simulator;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import app.view.frames.ChooseRegionFrame;
import app.model.users.Passenger;
import utils.DS.Latch;
import app.view.MapView;
import com.opencsv.CSVWriter;
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

//        String region = ChooseRegionFrame.choose();
        String region = "tlv.json";

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

        if(show){
            latch.lock();
        }

        do{
//            System.out.println("Simulator");
            latch.waitOnCondition();

            if(show) {
                updateFrame();
            }

            sleep(SLEEP_BETWEEN_FRAMES);
        } while(isAlive());

        finish();
    }

    private void finish(){
        unregister(this);
        // Write csv the riders analytics
        writeRequestToCsv(UserMap.INSTANCE.getRequests());
        LOGGER.info("Simulator finished!.");
    }

    private void writeRequestToCsv(Collection<Passenger> riders) {
        riders = riders.stream().filter(rider -> rider.getPickupTime() != null && rider.getDropTime() != null).toList();
        File file = new File("data/logs/sum1.csv");
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = { "id", "ask_time", "pickup_time", "src", "drop_time", "dest", "total_time_waited", "total_time_traveled"};
            writer.writeNext(header);

            // add data to csv
            List<String[]> data = new ArrayList<String[]>();
            for (Passenger rider : riders) {
                String[] row = {
                        rider.getId()+"",
                        rider.getStartTime()+"",
                        rider.getPickupTime().toLocaleString(),
                        rider.getLocation().toString(),
                        rider.getDropTime().toLocaleString(),
                        rider.getDestination().toString(),
                        rider.getTimeWaited()+" Minutes",
                        rider.getTotalTimeTraveled()+" Minutes"
                };
                data.add(row);
            }

            // sum up some fields in the last row.
            long totalTimeAvg = riders.stream().mapToLong(Passenger::getTotalTimeTraveled).sum() / riders.size();
            long timeWaitedAvg = riders.stream().mapToLong(Passenger::getTimeWaited).sum() / riders.size();
            String[] summary = {"Summary", "--", "--", "--", "--", "--", timeWaitedAvg+" Minutes",totalTimeAvg+" Minutes"};
            data.add(summary);

            writer.writeAll(data);

            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
