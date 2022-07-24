import app.controller.GraphAlgo;
import app.controller.RoadMapUtils;
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

import static utils.LogHandler.*;

/**
 *
 *  args[] {
 *      * map file path (.pbf file).
 *
 *      * flags:
 *           -node := set main node of graph. all nodes that aren't connected to it will be removed
 *           -speed := simulator speed
 *           -log := console log level
 *           -bound := set bounds to map
 *  }
 *
 *
 *
 *
 *    Use case:
 *      * to run with intellij with args[]
 *           edit configuration -> Name: APP -> Build and run -> Program arguments -> insert relevant flags
 *
 *
 *      * to convert files:
 *           osmconvert64.exe ariel2.osm > arielpbf.pbf --out-pbf
 *
 *
 *
 * TODO list:
 *                  /------------------------------------------------\
 *                  |/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\|
 *                  |/\/\/\/\/\/\/\/\|  SH!T TO DO  |/\/\/\/\/\/\/\/\|
 *                  |/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\|
 *                  \------------------------------------------------/
 *       .
 *       . sources document:  https://docs.google.com/document/d/1r4gWm002QzLkzu5Wc1mgb3C5IQuyaunEAUmF9_46nEg/edit#
 *
 *
 *
 * |==========================================================================================|
 * |=====================================   ALGORITHMS   =====================================|
 * |==========================================================================================|
 *
 *  todo:
 *          1st Priority:
 *              - 1) Make drive-pedestrian matchBruteForce1Pickup (may be base algo on Shortest Hamiltonian Path Problem (SHPP)).
 *    .
 *          Last priority:
 *              -
 *    .
 *                          SOURCES (unchecked sources := '??')
 *                          ---------------------------------
 *    .
 *    .
 *      1 Make drive-pedestrian matchBruteForce1Pickup algorithms:
 *          1.1 matchBruteForce1Pickup with split routes that address user profiles
 *              src: https://journals.plos.org/plosone/article?id=10.1371/journal.pone.0229674
 *              .
 *          1.2 Wolfram-Alpha - FindShortestTour function. (explanation : https://mathematica.stackexchange.com/questions/23733/how-does-findshortesttour-work)
 *              src: https://www.wolframalpha.com/input?t=crmtb01&i=FindShortestTour%5B%7B%7B0%2C+0%7D%2C+%7B1%2C+0%7D%2C+%7B0%2C+1%7D%2C+%7B1%2C+1%7D%2C+%7B0%2C+536870913%7D%7D%5D
 *              src: https://reference.wolfram.com/language/ref/FindShortestTour.html
 *              .
 *          1.3 naive solution for 1 drive, can work on a fully connected graph where nodes are pedestrians and edges weights are shortest path to each one
 *              src: https://www.geeksforgeeks.org/building-an-undirected-graph-and-finding-shortest-path-using-dictionaries-in-python/
 *              .
 *          1.4 shared taxi algo with pricing methods:
 *              src: https://www.sciencedirect.com/science/article/pii/S277258632200003X#bib0036
 *              .
 *          1.5 ?? (shared taxi algo):
 *              src: https://hal.archives-ouvertes.fr/hal-01488042v2/document
 *              .
 *          1.6 ?? (shared taxi algo)
 *              src: https://www.hindawi.com/journals/mpe/2021/5572200/
 *              .
 *          1.7 ?? (shared taxi algo):
 *              src: https://www.researchgate.net/publication/261216476_Design_and_Modeling_of_Real-Time_Shared-Taxi_Dispatch_Algorithms
 *              .
 *
 * |==========================================================================================|
 * |======================================   PROJECT   =======================================|
 * |==========================================================================================|
 *
 *      TODO project:
 *          Priority 1:
 *              - add Pedestrian and Drives to RoadMap
 *              - add summary of run
 *              - ReadMe (use javadocs).
 *              - Address warnings - optimize project.
 *              - Add 'alias-like' in Reader.java and MapView.java (org.openstreetmap.osmosis.core.domain.v0_6.Node and org.graphstream.Node)
 *                  src: https://stackoverflow.com/questions/2447880/change-name-of-import-in-java-or-import-two-classes-with-the-same-name
 *                  src: https://itecnote.com/tecnote/java-change-name-of-import-in-java-or-import-two-classes-with-the-same-name/
 *              .
 *          Last priority:
 *              -
 *
 * |==========================================================================================|
 * |==============================   SIMULATOR INFRASTRUCTURE   ==============================|
 * |==========================================================================================|
 *
 *      TODO Simulator infrastructure:
 *          Priority 1:
 *              - Enforce thread-safe for all shared methods, datastructures and variables.
 *                  * src: https://www.geeksforgeeks.org/synchronization-in-java/
 *              - Check if car not moving for x time.
 *          Priority 2:
 *              - Save graph instead of recreating it.
 *              - Use only int id of classes.
 *              - Add @assertions.
 *              - Finish CLI --- sources: http://jcommander.org/  || https://commons.apache.org/proper/commons-cli/.
 *              .
 *          \\ Last priority \\:
 *              - Make accept .osm?.
 *              - STYLE
 *                  * color highwayType
 *                  * style by class type
 *                  * add ui.class
 *                  * Add loader.
 *                  * show node data (on debug mode?)
 *                  * Set on hovering style
 *                      - src: https://stackoverflow.com/questions/70745672/get-graphicedge-at-mouse-hovering-in-graphstream/70775617#70775617
 *                  * text-visibility-mode: The text visibility mode describe when the optional label of elements should be printed:
 *                      - src https://graphstream-project.org/doc/Advanced-Concepts/GraphStream-CSS-Reference/1.3/
 *                  * show only first digit in long car/user id
 *
 *
 * ==========================================================================================
 *
 */
public final class App{
    private static Long  NODE_IN_MAIN_COMPONENT;
    private static Double SIMULATOR_SPEED;
    private static String CONSOLE_LOG_LEVEL, PBF_PATH;
    private static Boolean BOUNDS, SHOW_ALL_PATHS, LOAD_FROM_JSON;
    private static int DRIVE_NUM, REQUEST_NUM;

    static{
        LOAD_FROM_JSON = true;
        PBF_PATH = "data/maps/israel.pbf";
        NODE_IN_MAIN_COMPONENT =2432701015L;
        SIMULATOR_SPEED = 20.0;
        BOUNDS = true;
        CONSOLE_LOG_LEVEL =
                "SEVERE";
//                "ALL";
        SHOW_ALL_PATHS = false;
        DRIVE_NUM = 1;
        REQUEST_NUM = 2;
    }

    public static void main(String[] args) {
//        CLI();

        init(args);
        LOGGER.finest("Let's GO!!");

        LOGGER.info( "Start parsing main map.");// : '" + pbfFilePath+"'");
        RoadMapUtils.setBounds(BOUNDS);

        if(LOAD_FROM_JSON){
            JsonHandler.RoadMapType.load();
        }else{
            CreateMap();
            JsonHandler.RoadMapType.save();
        }


        LOGGER.info("Map is ready. Map = " + RoadMap.INSTANCE);


        UserMap.INSTANCE.initRandomEvents(DRIVE_NUM, REQUEST_NUM);
//        RealTimeEvents.init();

        MapView.instance.show(SIMULATOR_SPEED, SHOW_ALL_PATHS);

        closeLogHandlers();
        LOGGER.info("Finished!");

        System.exit(0);
    }

    public static void CreateMap() {
        if(PBF_PATH == null){
            PBF_PATH = chooseFile();
        }


        try {
            InputStream inputStream = new FileInputStream(PBF_PATH);

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
            GraphAlgo.removeNodesThatNotConnectedTo(RoadMap.INSTANCE.getNode(NODE_IN_MAIN_COMPONENT));

        } catch (FileNotFoundException e) {
            LOGGER.severe("File not found!, "+e.getMessage());
            JOptionPane.showMessageDialog(new JFrame(), "File not found!", "ERROR",
                    JOptionPane.ERROR_MESSAGE);

            System.exit(0);
        }
    }

    /*
     * TODO add:
     *          - set bounds coordinates
     *          -
     *          - set NODE_IN_MAIN_COMPONENT to closest given coordinates
     *
 *          fix to read arguments between flags
     */
    private static void init(String[] args){
         for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) == '-') {
                switch (args[i]) {
                    case "-m" -> LOAD_FROM_JSON = false;
                    case "-n" -> NODE_IN_MAIN_COMPONENT = Long.parseLong(args[++i]);
                    case "-s" -> SIMULATOR_SPEED = Double.parseDouble(args[++i]);
                    case "-l" -> CONSOLE_LOG_LEVEL = args[++i];
                    case "-b" -> BOUNDS = args[++i].equals("y") ||
                            args[++i].equals("Y") || args[++i].equals("yes") || args[++i].equals("hell yeah");
                    case "-c" -> RoadMapUtils.updateBounds(
                            Double.parseDouble(args[++i]), Double.parseDouble(args[++i]),
                            Double.parseDouble(args[++i]), Double.parseDouble(args[++i])
                    );
                    case "-h" -> {
                        System.out.println(Instructions);
                        return;
                    }
                }
            }
        }
        ConsoleLevel(CONSOLE_LOG_LEVEL);
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

    private App() {}

    private static void CLI(){
        System.out.println("java -jar RideShare.jar -h\n\n" + Instructions
                + "\n\n\njava -jar RideShare.jar   -s 10  -l ALL  -b y\n");
    }

    private static final String Instructions =
            """
            Usage: rideShare.exe  [.pbf map path] [-n] [-s] [-l] [-b] [-c]\s

            Options:
                -m                      Load PBF file (use saved map otherwise).
                -n osm-node-id          Node id that will be in map, while the rest of the nodes will be cleaned from map (in order to keep graph connected).
                                        Default: value will show center of israel.
                -s speed                Speed of simulator.
                                        Default: 10.
                -l log-level            Log level for console can be a number or a string value of 'java.util.logging.Level'.
                -b 'y'/'n'              Set bounds to the map.
                -c max/min coordinates  Set bounds coordinates - top latitude, bottom latitude, top longitude, bottom longitude.  
                                        Default: value will show center of israel.
            """;
}


