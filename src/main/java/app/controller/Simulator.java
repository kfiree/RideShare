package app.controller;

import app.controller.osm_processing.Parser;
import app.controller.osm_processing.Reader;
import app.model.RoadMap;
import app.model.UserMap;
import app.view.MapView;
import crosby.binary.osmosis.OsmosisReader;
import utils.JsonHandler;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
//import java.util.concurrent.BrokenBarrierException;
//import java.util.concurrent.CyclicBarrier;

import static app.controller.GraphAlgo.minToMs;
import static app.controller.UserMapHandler.initEventsInLine;
import static app.controller.UserMapHandler.initRandomEvents;
import static utils.LogHandler.LOGGER;
import static utils.Utils.FORMAT;
import static utils.Utils.validate;

public class Simulator implements Runnable{
    private double speed;
    private boolean show;
    private EventManager events;
    private MatchMaker cupid;
    private Thread eventsThread, cupidThread;
    private Date time;
//    public final CyclicBarrier cyclicBarrier;
    private Thread thread;
    private MapView mapView = MapView.instance;
    private static long SLEEP_BETWEEN_FRAMES;

    /** CONSTRUCTORS */
    private Simulator(){
//        this.cyclicBarrier = new CyclicBarrier(2);
        SLEEP_BETWEEN_FRAMES = 2000;
    }

    /** Singleton specific properties */
    public static final Simulator INSTANCE = new Simulator();

    /** Singleton thread */
    public void init(double simulatorSpeed, int requestNum, int driveNum, boolean show, boolean bounds, boolean loadJSON){
        validate(simulatorSpeed > 0, "Illegal simulator speed "+ simulatorSpeed + ".");

        RoadMapHandler.setBounds(bounds);

        if(loadJSON){
            LOGGER.info( "Read road map from JSON.");
            JsonHandler.RoadMapType.load();
        }else{
            LOGGER.info( "Start parsing main map.");
            CreateMap();
            JsonHandler.RoadMapType.save();
        }

        LOGGER.info("Map is ready. Map = " + RoadMap.INSTANCE);

        // initEventsInLine(requestNum);
        initRandomEvents(driveNum, requestNum);

        this.speed = simulatorSpeed;
        this.events = new EventManager(this);
        this.cupid = new MatchMaker(this);
        this.show = show;

    }

    @Override
    public void run() {
        time = events.getTime();

        // start simulator
        eventsThread = new Thread(events);
         eventsThread.start();

        // start cupid
        cupidThread = new Thread(cupid);
        cupidThread.start();

        if(show){
            mapView.init(this);

            do{
                sleep();
                mapView.update();
            }while(isAlive());
        }

        finish();
    }

    private void finish(){
        System.out.println("Simulator done.");
        LOGGER.info("Simulator done.");
//        try {
//            System.out.println("simulator | cyclicBarrier "+ cyclicBarrier.getNumberWaiting());
//            cyclicBarrier.await();
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (BrokenBarrierException e) {
//            e.printStackTrace();
//        }
    }

    public boolean isAlive(){
        return true;
//        return eventsThread.isAlive() || !UserMap.INSTANCE.getDrives().isEmpty();
    }

    private void sleep() {
        time = new Date(time.getTime()+SLEEP_BETWEEN_FRAMES);

        try {
            Thread.sleep(SLEEP_BETWEEN_FRAMES) ;
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    /** GETTERS */

    public Date time() {
        return time;
    }

    public double speed() {
        return speed;
    }

    public EventManager events() {
        return events;
    }

    public Thread eventsThread() {
        return eventsThread;
    }

    public Thread cupidThread() {
        return cupidThread;
    }

    /** SETTERS */

    public void setSpeed(double simulatorSpeed) {
        this.speed = simulatorSpeed;
    }

    public void setEventsManager(EventManager events) {
        this.events = events;
    }


    /** CREATE MAP */
    public static void CreateMap() {
        String pbfFilePath  = chooseFile();

        try {
            InputStream inputStream = new FileInputStream(pbfFilePath);

            // read from osm pbf file:
            Reader reader = new Reader();
            OsmosisReader osmosisReader = new OsmosisReader(inputStream);
            osmosisReader.setSink(reader);

            // initial parsing of the .pbf file:
            osmosisReader.run();

            // secondary parsing of ways/creation of edges:
            Parser parser = new Parser();
            parser.parseMapWays(reader.getWays());

            // get riders & drivers
            GraphAlgo.removeNodesThatNotConnectedTo(RoadMap.INSTANCE.getNode(2432701015L));

        } catch (FileNotFoundException e) {
            LOGGER.severe("File not found!, "+e.getMessage());
            JOptionPane.showMessageDialog(new JFrame(), "File not found!", "ERROR",
                    JOptionPane.ERROR_MESSAGE);

            System.exit(0);
        }
    }

    private static String chooseFile() {
        // JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        JFileChooser jfc = new JFileChooser("data");
        jfc.setDialogTitle("Select .osm.pbf file to read");
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        return "Not A Valid Path";
    }
}
