package view;

import controller.utils.GraphAlgo;
import model.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static controller.utils.LogHandler.LOGGER;
import static view.MapView.date;
import static view.MapView.clockNode;
import static view.StyleUtils.dateFormatter;

/**
 * TODO init pedestrians list
 */
public class RealTimeEvents implements Runnable{
    private List<Drive> events, startedEvents, eventsToSend;
    private final double simulatorSpeed;
    private Date currTime;
    private Random rand = new Random(1);

    public RealTimeEvents(double simulatorSpeed) {
        this.simulatorSpeed = simulatorSpeed;

        events = initDrives(10);
        currTime = events.get(0).getLeaveTime();

        startedEvents = new ArrayList<>();
        eventsToSend = new ArrayList<>();

        date = new Date();
    }

    @Override
    public void run() {
        LOGGER.fine("RealTimeEvents star running");

        while(!events.isEmpty()){
            // add first event in list to startedEvents
            Drive newDrive = events.remove(0);
            newDrive.setSimulatorSpeed(simulatorSpeed);
            Thread driveThread = new Thread(newDrive);
            driveThread.start();

            startedEvents.add(newDrive);

            int sleepTime = currTime.compareTo(newDrive.getLeaveTime());

            sleep(sleepTime);

            // jump in time to next event
            currTime = newDrive.getLeaveTime();
            LOGGER.info("RealTimeEvents add event. sleep time = " +sleepTime);
        }
        LOGGER.info("RealTimeEvents finished.");

    }

    private void sleep(long ms ) {
//        double sleepTime = ms / timeSpeed;
        double sleepTime = 3000;
        try { Thread.sleep(3000) ; }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    public List<Pedestrian> initPedestrians(int pedestrianNum){
        List<Pedestrian> pedestrians = new ArrayList<>();
        List<Node> nodes = new ArrayList<>(RoadMap.getInstance().getNodes());
        Node src, dst;

        int[] randomIndexes = rand.ints(pedestrianNum*2, 0, nodes.size()).toArray();

        //create drives
        for (int i = 0; i < pedestrianNum; i++) {
            src = nodes.get(randomIndexes[i*2]);
            dst = nodes.get(randomIndexes[i*2 +1 ]);
            int timeAdded = rand.nextInt(75000);
            Date startTime = new Date(date.getTime() + ((750000+timeAdded) * i));
            pedestrians.add(new Pedestrian("p"+i, src.getCoordinates(), dst.getCoordinates(), startTime));

        }

        LOGGER.finer("init "+pedestrians.size() + " pedestrians.");

        return pedestrians;

    }

    public List<Drive> initDrives(int drivesNum) {
        // drives variables
        List<Drive> drives = new ArrayList<>();
        List<Node> nodes = new ArrayList<>(RoadMap.getInstance().getNodes());
        Node src, dst;



        // init random indexes for nodes
        int[] randomIndexes = rand.ints(drivesNum*2, 0, nodes.size()).toArray();

        //create drives
        for (int i = 0; i < drivesNum; i++) {
            src = nodes.get(randomIndexes[i*2]);
            dst = nodes.get(randomIndexes[i*2 +1 ]);

            drives.add(createDrive(src, dst, i));
        }

        LOGGER.finer("init "+drives.size() + " drives.");

        return drives;
    }

    private Drive createDrive(Node src, Node dst, int i){
        Path shortestPath;
        Drive drive = null;

        if(src != null && dst != null ){
            shortestPath = GraphAlgo.getShortestPath(src, dst);
            int timeAdded = rand.nextInt(75000);
            if(shortestPath != null) {
                Date startTime = new Date(date.getTime() + ((750000+timeAdded) * i));
                drive = new Drive(shortestPath, "unknown" , String.valueOf(i), startTime );

                if(drive == null){
                    LOGGER.severe("drive from " + src + " to "+ dst + " was not created, Drive(Id: "+ i +", Date: " + startTime +" = , Path).");
                    //TODO add formatter for date
                }
//                validate(drive != null,"drive from " + src + " to "+ dst + " was not created, Drive(Id: "+ i +", Date: "+startTime.+" = , Path).");
            }
        }


        return drive;
    }

    public List<Drive> getStartedEvents() {
        eventsToSend.clear();
        eventsToSend.addAll(startedEvents);

        startedEvents.clear();
        return eventsToSend; //TODO maybe make synchronized
    }
}
