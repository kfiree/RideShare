package view;

import controller.utils.GraphAlgo;
import model.Drive;
import model.Node;
import model.Path;
import model.RoadMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static controller.utils.LogHandler.LOGGER;

/**
 * TODO init pedestrians list
 */
public class RealTimeEvents implements Runnable{
    private List<Drive> events, startedEvents, eventsToSend;
    private final double timeSpeed;
    private Date currTime;

    public RealTimeEvents(double timeSpeed) {
        this.timeSpeed = timeSpeed;

        events = initDrives(5);
        currTime = events.get(0).getLeaveTime();

        startedEvents = new ArrayList<>();
        eventsToSend = new ArrayList<>();
    }

    @Override
    public void run() {
        LOGGER.fine("RealTimeEvents star running");

        while(!events.isEmpty()){

            // add first event in list to startedEvents
            Drive newDrive = events.remove(0);
            newDrive.setTimeSpeed(timeSpeed);
            Thread driveThread = new Thread(newDrive);
            driveThread.start();

            startedEvents.add(newDrive);


            int sleepTime = currTime.compareTo(newDrive.getLeaveTime());

            sleep(sleepTime);

            // jump in time to next event
            currTime = newDrive.getLeaveTime();
            LOGGER.info("RealTimeEvents add event. sleep time = " +sleepTime);
        }
    }

    private void sleep(long ms ) {
//        double sleepTime = ms / timeSpeed;
        double sleepTime = 3000;
        try { Thread.sleep(3000) ; }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    public List<Drive> initDrives(int drivesNum) {
        // drives variables
        List<Drive> drives = new ArrayList<>();
        List<Node> nodes = new ArrayList<>(RoadMap.getInstance().getNodes());
        Node src, dst;
        long sessionStartInMs = (new Date()).getTime();

        // init random indexes for nodes
        Random rand = new Random(1);
        int[] randomIndexes = rand.ints(drivesNum*2, 0, nodes.size()).toArray();

        //create drives
        for (int i = 0; i < drivesNum; i++) {
            src = nodes.get(randomIndexes[i*2]);
            dst = nodes.get(randomIndexes[i*2 +1 ]);

            drives.add(createDrive(src, dst, i, sessionStartInMs));
        }

        LOGGER.finer("init "+drives.size() + " drives.");

        return drives;
    }

    private Drive createDrive(Node src, Node dst, int i, long sessionStartInMs){
        Path shortestPath;
        Drive drive = null;

        if(src != null && dst != null ){
            shortestPath = GraphAlgo.getShortestPath(src, dst);

            if(shortestPath != null) {
                Date startTime = new Date(sessionStartInMs + (15000 * i));
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
