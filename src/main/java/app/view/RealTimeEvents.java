package app.view;

import app.controller.GraphAlgo;
import app.model.*;
import app.model.interfaces.ElementsOnMap;

import java.util.*;

import static utils.LogHandler.LOGGER;
import static app.view.MapView.simulatorStartDate;
import static app.Utils.FORMAT;

/**
 * TODO init pedestrians list
 */
public class RealTimeEvents implements Runnable{
    private PriorityQueue<ElementsOnMap> eventsQueue, events;
    private final List<ElementsOnMap> newEvents, eventsToSend;
    private final double simulatorSpeed;
    private Date currTime;

    private final Random rand = new Random(5);

    public RealTimeEvents(double simulatorSpeed) {
        this.simulatorSpeed = simulatorSpeed;

        events = initEvents(10, 5);
        currTime = events.peek().getStartTime();


        newEvents = new ArrayList<>();
        eventsToSend = new ArrayList<>();
    }

    @Override
    public void run() {
        LOGGER.fine("RealTimeEvents star running");
        String eventType = "Drive";
        while(!events.isEmpty()){
            /*  poll new event and wait till it is his start time*/
            ElementsOnMap newEvent = events.poll();

            int sleepTime = currTime.compareTo(newEvent.getStartTime());

            sleep(sleepTime);

            // jump in time to next event
            currTime = newEvent.getStartTime();

            /*  add new event */
            if(newEvent instanceof Drive){
                Drive newDrive = (Drive) newEvent;

                newDrive.setSimulatorSpeed(simulatorSpeed);

                UserMap.INSTANCE.addDrive(newDrive);

                newEvents.add(newDrive);
            }else{
                Pedestrian newPedestrian = (Pedestrian) newEvent;
                UserMap.INSTANCE.addRequest(newPedestrian);
                newEvents.add(newPedestrian);
            }

            LOGGER.info("RealTimeEvents add "+newEvent +" event. at " + FORMAT(newEvent.getStartTime())+".");
        }
        LOGGER.info("RealTimeEvents finished.");

    }

    public PriorityQueue<ElementsOnMap> initEvents(int driveNum, int pedestriansNum){
        eventsQueue = new PriorityQueue<>();

        eventsQueue.addAll(initDrives(driveNum));
        eventsQueue.addAll(initPedestrians(pedestriansNum));

        return eventsQueue;
    }

    public List<ElementsOnMap> initPedestrians(int pedestrianNum){
        List<ElementsOnMap> pedestrians = new ArrayList<>();
        List<Node> nodes = new ArrayList<>(RoadMap.INSTANCE.getNodes());
        long pedestriansStartTime = simulatorStartDate.getTime() + 10000;
        Node src, dst;

        int[] randomIndexes = rand.ints(pedestrianNum*2, 0, nodes.size()).toArray();

        //create drives
        for (int i = 0; i < pedestrianNum; i++) {
            src = nodes.get(randomIndexes[i*2]);
            dst = nodes.get(randomIndexes[i*2 +1 ]);
            int timeAdded = rand.nextInt(75000);
            Date startTime = new Date(pedestriansStartTime + ((750000+timeAdded) * i));
            pedestrians.add(new Pedestrian("0"+i, src, dst, startTime));
        }

        LOGGER.finer("init "+pedestrians.size() + " pedestrians.");

        return pedestrians;

    }

    public List<ElementsOnMap> initDrives(int drivesNum) {
        // drives variables
        List<ElementsOnMap> drives = new ArrayList<>();
        List<Node> nodes = new ArrayList<>(RoadMap.INSTANCE.getNodes());
        Node src, dst;

        //TODO when read events from file will work - make date the first event start time
        simulatorStartDate = new Date();

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
                Date startTime = new Date(simulatorStartDate.getTime() + ((750000+timeAdded) * i));
                drive = new Drive(shortestPath, "unknown" , String.valueOf(i), startTime );

                if(drive == null){
                    LOGGER.severe("drive from " + src + " to "+ dst + " was not created, Drive(Id: "
                            + i +", Date: " + FORMAT(startTime) +" = , Path).");
                }
//                validate(drive != null,"drive from " + src + " to "+ dst + " was not created, Drive(Id: "+ i +", Date: "+startTime.+" = , Path).");
            }
        }


        return drive;
    }

    /**
     *  TODO maybe make synchronized
     */
    public List<ElementsOnMap> getNewEvents() {
        eventsToSend.clear();
        eventsToSend.addAll(newEvents);

        newEvents.clear();
        return eventsToSend;
    }

    private void sleep(long ms ) {
//        double sleepTime = ms / timeSpeed;
        double sleepTime = 3000;
        try { Thread.sleep(3000) ; }
        catch (InterruptedException e) { e.printStackTrace(); }
    }
}
