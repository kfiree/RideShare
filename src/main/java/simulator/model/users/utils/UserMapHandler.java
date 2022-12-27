package simulator.model.users.utils;

import simulator.model.graph.Node;
import simulator.model.graph.Path;
import simulator.model.graph.RoadMap;
import simulator.model.users.Driver;
import simulator.model.users.Passenger;
import simulator.model.users.UserMap;
import utils.JsonHandler;
import utils.logs.LogHandler;

import java.util.*;

import static utils.Utils.FORMAT;

public class UserMapHandler {
    public static final UserMap userMap = UserMap.INSTANCE;
    private static final RoadMap roadMap = RoadMap.INSTANCE;
    private static final Random rand = new Random(1);
    private static final int CONST_EVENT_DIFF, RAND_MAX_EVENT_DIFF;

    static{
//        CONST_EVENT_DIFF = 750000;
        CONST_EVENT_DIFF = 0;
        RAND_MAX_EVENT_DIFF = 150000; // 2.5 minutes
    }


    /**  init events from DB */

    public static void initEvents(){
        JsonHandler.UserMapType.load("data/maps/tlv.json");
    }

    public static void initRandomEvents(int driveNum, int pedestriansNum){
        userMap.setFirstEventTime();

        initRandDrives(driveNum);
        initRandRiders(pedestriansNum);
    }

    public static void printUserMapState(){
        showDrives("Drives" , userMap.getDrives());
        showDrives("Live_Drives" , userMap.getLiveDrives());

        showRider("Passengers", userMap.getRequests());
        showRider("Live_Passengers", userMap.getLiveRequest());
        if(!userMap.getFinishedEvents().isEmpty()) {
            System.out.println("=========================");
            System.out.println("Finished");
            System.out.println("=========================");

            userMap.getFinishedEvents().forEach(u ->
                    System.out.println("{Passenger, " + u.getId() + ", " + FORMAT(u.getStartTime()) + "}")
            );
            System.out.println("\n");
        }

    }

    private static void showDrives(String msg, Collection<Driver> users){
        if(!users.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            users.forEach(u -> builder.append("{Drive " + u.getId() + ", choose " + FORMAT(u.getStartTime()) + ", time " + u.getOriginalTime() / 60000 + " minutes }\n"));
            LogHandler.LOGGER.info("\n=========================\n"+
                    msg + "\n=========================\n"+
                    builder + "\n");
        }
    }

    private static void showRider(String msg, Collection<Passenger> users){
        if(!users.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            users.forEach(u -> builder.append("{Passenger " + u.getId() + ", choose " + FORMAT(u.getStartTime()) + "}\n"));
            LogHandler.LOGGER.info("\n=========================\n"+
                    msg + "\n=========================\n"+
                    builder + "\n");
        }
    }

    public static void initRandRiders(int requestsNum){
        List<Node> nodes = new ArrayList<>(roadMap.getNodes()); //todo why clone?
        long pedestriansStartTime = userMap.getFirstEventTime().getTime() + 10000; //10 seconds after drives
        Node src, dst;

        int[] randomIndexes = rand.ints(requestsNum* 2L, 0, nodes.size()).toArray();

        //create drives
        for (int i = 0; i < requestsNum; i++) {
            src = nodes.get(randomIndexes[i*2]);
            dst = nodes.get(randomIndexes[i*2 +1 ]);
            int timeAdded = rand.nextInt(RAND_MAX_EVENT_DIFF);

            Passenger passenger = new Passenger(src, dst, new Date(pedestriansStartTime + ((long) (CONST_EVENT_DIFF + timeAdded) * i)));
            userMap.addRequest(passenger);
        }

        LogHandler.LOGGER.finer("init "+ requestsNum + " pedestrians.");
    }

    public static void initRandDrives(int drivesNum) {

        // drives variables
        List<Node> nodes = new ArrayList<>(RoadMap.INSTANCE.getNodes());//todo why clone?
        Node src, dst;

        // init random indexes for nodes
        int[] randomIndexes = rand.ints(drivesNum* 2L, 0, nodes.size()).toArray();

        //create drives
        for (int i = 0; i < drivesNum; i++) {
            src = nodes.get(randomIndexes[i*2]);
            dst = nodes.get(randomIndexes[i*2 +1 ]);

            Path shortestPath;
            Driver drive = null;

            if(src != null && dst != null ){
                shortestPath = GraphAlgo.getShortestPath(src, dst);
                long timeAdded = rand.nextInt(RAND_MAX_EVENT_DIFF);
                if(shortestPath != null) {
                    if(drive != null){
                        timeAdded = (long) (drive.getPath().getWeight()/2);
                    }
                    userMap.addDrive(src, dst, ((CONST_EVENT_DIFF + timeAdded) * i));

                }
            }

//            drives.put(drive.getId(), drive);
        }

        LogHandler.LOGGER.finer("init " + drivesNum + " drives.");
    }

    public static void initEventsInLine(int requestsNum){
        Random rand = new Random(1);
        Node src = RoadMap.INSTANCE.getNode(417772575L);
        Node dst = RoadMap.INSTANCE.getNode(1589498340L);

        Date startTime = userMap.setFirstEventTime();
        Path path = userMap.addDrive(src, dst, 5000L).getPath();

        ArrayList<Integer> randomIndexes = new ArrayList<>();


        Arrays.stream(rand.ints(requestsNum* 2L, 0, path.getSize()).toArray())
                .forEach(randomIndexes::add);

        int srcIndex, dstIndex;
        for (int i = 0; i < requestsNum*2; i+=2) {

            srcIndex = Math.min(randomIndexes.get(i),  randomIndexes.get(i + 1));
            dstIndex = Math.max(randomIndexes.get(i),  randomIndexes.get(i + 1));

            src = path.getNodes().get(srcIndex);
            dst = path.getNodes().get(dstIndex);


            userMap.addRequest(src, dst, new Date(startTime.getTime() + i* 100L));

        }
//        userMap.getDrives().forEach(d ->
//                System.out.println("drive " + d.getId() + " from " + d.getLocation().getId() + " to " + d.getFinalDestination().getId() + ", size " + d.getPath().getSize())
//        );
//
//        userMap.getRequests().forEach(d ->
//                System.out.println("request " + d.getId() + " from " + d.getLocation().getId() + " to " + d.getFinalDestination().getId())
//        );

        printUserMapState();
    }

}
