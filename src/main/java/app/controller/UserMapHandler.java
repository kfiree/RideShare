package app.controller;

import app.model.*;
import utils.JsonHandler;

import java.util.*;

import static utils.LogHandler.LOGGER;

public class UserMapHandler {
    private static UserMap userMap = UserMap.INSTANCE;
    private static RoadMap roadMap = RoadMap.INSTANCE;
    private static Random rand = new Random(1);

    /**  init events from DB */

    public static void initEvents(){
        JsonHandler.UserMapType.load();
    }

    public static void initRandomEvents(int driveNum, int pedestriansNum){
        userMap.setFirstEventTime();

        initRandDrives(driveNum);
        initRandRiders(pedestriansNum);
    }

    public static void initRandRiders(int requestsNum){
        List<Node> nodes = new ArrayList<>(roadMap.getNodes());
        long pedestriansStartTime = userMap.getFirstEventTime().getTime() + 10000; //10 seconds after drives
        Node src, dst;

        int[] randomIndexes = rand.ints(requestsNum* 2L, 0, nodes.size()).toArray();

        //create drives
        for (int i = 0; i < requestsNum; i++) {
            src = nodes.get(randomIndexes[i*2]);
            dst = nodes.get(randomIndexes[i*2 +1 ]);
            int timeAdded = rand.nextInt(75000);

            Rider rider = new Rider(src, dst, new Date(pedestriansStartTime + ((long) (750000 + timeAdded) * i)));
            userMap.addRequest(rider);
        }

        LOGGER.finer("init "+ requestsNum + " pedestrians.");
    }

    public static void initRandDrives(int drivesNum) {

        // drives variables
        List<Node> nodes = new ArrayList<>(RoadMap.INSTANCE.getNodes());
        Node src, dst;

        // init random indexes for nodes
        int[] randomIndexes = rand.ints(drivesNum* 2L, 0, nodes.size()).toArray();

        //create drives
        for (int i = 0; i < drivesNum; i++) {
            src = nodes.get(randomIndexes[i*2]);
            dst = nodes.get(randomIndexes[i*2 +1 ]);

            Path shortestPath;
            Drive drive = null;

            if(src != null && dst != null ){
                shortestPath = GraphAlgo.getShortestPath(src, dst);
                long timeAdded = rand.nextInt(75000);
                if(shortestPath != null) {
                    if(drive != null){
                        timeAdded = (long) (drive.getPath().getWeight()/2);
                    }
                    drive = userMap.addDrive(src, dst, ((long) (750000 + timeAdded) * i));

//                    drive = new Drive(shortestPath, new Date(firstEventTime.getTime() + ((long) (750000 + timeAdded) * i)) );
                }
            }

//            drives.put(drive.getId(), drive);
        }

        LOGGER.finer("init " + drivesNum + " drives.");
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
        userMap.getDrives().forEach(d ->
                System.out.println("drive " + d.getId() + " from " + d.getCurrentNode().getId() + " to " + d.getDest().getId() + ", size " + d.getPath().getSize())
        );

        userMap.getRequests().forEach(d ->
                System.out.println("request " + d.getId() + " from " + d.getCurrentNode().getId() + " to " + d.getDest().getId())
        );
    }
}
