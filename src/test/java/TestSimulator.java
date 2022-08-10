import app.controller.EventManager;
import app.controller.RoadMapHandler;
import app.model.graph.Node;
import app.model.graph.Path;
import app.model.graph.RoadMap;
import app.model.users.UserMap;
import org.junit.Before;
import org.junit.Test;
import utils.JsonHandler;

import java.util.*;

import static utils.LogHandler.LOGGER;
import static utils.LogHandler.closeLogHandlers;

public class TestSimulator {
    private static final UserMap userMap = UserMap.INSTANCE;
    private static final RoadMap roadMap = RoadMap.INSTANCE;
    private EventManager eventManager;

    @Before
    public void init() {
        LOGGER.finest("Let's GO!!");

        LOGGER.info( "Start parsing main map.");// : '" + pbfFilePath+"'");
        RoadMapHandler.setBounds(true);

        JsonHandler.RoadMapType.load();

//        Simulator simulator =new(15, true);

        closeLogHandlers();
        LOGGER.info("Finished!");

        System.exit(0);

    }
    @Test
    public void inLinePickUpsTest(){
//        initEventsInLine(3);
//        eventManager = new EventManager();


        pickupOrder1();
    }

    public void pickupOrder1(){
        int requestsNum = 3;
        Random rand = new Random(1);
        Node src = RoadMap.INSTANCE.getNode(417772575L);
        Node dst = RoadMap.INSTANCE.getNode(1589498340L);

        Date startTime = userMap.setFirstEventTime();
        Path path = userMap.addDrive(src, dst, 5000L).getPath();

        ArrayList<Integer> randomIndexes = new ArrayList<>();


        Arrays.stream(rand.ints(requestsNum* 2L, 0, path.getSize()).toArray())
                .forEach(randomIndexes::add);

//        randomIndexes.sort(Comparator.comparing(integer -> {i}));
        Collections.sort(randomIndexes);
//        Collections.sort(objectOfArrayList, Collections.reverseOrder());
        int srcIndex, dstIndex;
        for (int i = 0; i < requestsNum*2; i+=2) {

            srcIndex = Math.min(randomIndexes.get(i),  randomIndexes.get(i + 1));
            dstIndex = Math.max(randomIndexes.get(i),  randomIndexes.get(i + 1));

            src = path.getNodes().get(srcIndex);
            dst = path.getNodes().get(dstIndex);


            userMap.addRequest(src, dst, new Date(startTime.getTime() + i* 100L));

        }
        userMap.getDrives().forEach(d ->
                System.out.println("drive " + d.getId() + " from " + d.getLocation().getId() + " to " + d.getDestination().getId() + ", size " + d.getPath().getSize())
        );

        userMap.getRequests().forEach(d ->
                System.out.println("request " + d.getId() + " from " + d.getLocation().getId() + " to " + d.getDestination().getId())
        );
    }
}
