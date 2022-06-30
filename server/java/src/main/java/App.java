import controller.utils.GraphAlgo;
import controller.utils.MapUtils;
import view.MapView;
import crosby.binary.osmosis.OsmosisReader;
import model.RoadMap;
import controller.osm_processing.*;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static controller.utils.LogHandler.*;

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
 *           edit configuration -> Name: APP -> Build and run -> Program arguments -> insert: server/java/data/israel.pbf and relevant flags
 *
 *
 *      * to convert files:
 *           osmconvert64.exe ariel2.osm > arielpbf.pbf --out-pbf
 *
 * TODO :
 *      - make methods and datastructures thread safe.
 *      - use only int id of classes
 *      - add assertion @
 *      - add cli                http://jcommander.org/      ||      https://commons.apache.org/proper/commons-cli/
 *      - make vars final after initialize
 *      -  set & get map user drive | sub map | match rider and drivers | lock drive
 *      -  add loader
 *      - make accept .osm?
 */
public final class App{
    private static Long  NODE_IN_MAIN_COMPONENT;
    private static Double SIMULATOR_SPEED;
    private static String CONSOLE_LOG_LEVEL, MAP_PATH;
    private static Boolean BOUNDS;

    public static void main(String[] args) {
        System.out.println("java -jar RideShare.jar -h\n\n" + Instructions
                + "\n\n\njava -jar RideShare.jar   -s 10  -l ALL  -b y");

        init(args);
        LOGGER.finest("Let's GO!!");

        LOGGER.info( "Start parsing main map.");// : '" + pbfFilePath+"'");
        MapUtils.setBounds(BOUNDS);
        CreateMap(MAP_PATH);

        LOGGER.info("Map is ready. Map = " + RoadMap.getInstance());
        MapView.getInstance().show(SIMULATOR_SPEED);

        closeLogHandlers();
        LOGGER.info("Finished!");

        System.exit(0);
    }

    public static void CreateMap(String pathToPBF) {
        InputStream inputStream;

        try {
            inputStream = new FileInputStream(pathToPBF);

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
            GraphAlgo.removeNodesThatNotConnectedTo(RoadMap.getInstance().getNode(NODE_IN_MAIN_COMPONENT));

        } catch (FileNotFoundException e) {
            LOGGER.severe("File not found!, "+e.getMessage());
            JOptionPane.showMessageDialog(new JFrame(), "File not found!", "ERROR",
                    JOptionPane.ERROR_MESSAGE);

            System.exit(0);
        }
    }

    /**
     * TODO add:
     *          - set bounds coordinates
     *          -
     *          - set NODE_IN_MAIN_COMPONENT to closest given coordinates
     *
 *          fix to read arguments between flags
     */
    private static void parseArgs(String[] args){
        if(args.length == 0){ MAP_PATH = chooseFile(); }
        else if(args[0].equals("-h")){ System.out.println(Instructions); }
        else { MAP_PATH = args[0]; }

        MAP_PATH = args.length == 0 ? chooseFile() : args[0];
        int i = 1;

        while(i<args.length){
            if(args[i].charAt(0) == '-'){
                switch (args[i]){
                    case "-n" -> NODE_IN_MAIN_COMPONENT =  Long.parseLong(args[++i]);
                    case "-s" -> SIMULATOR_SPEED = Double.parseDouble(args[++i]);
                    case "-l" -> CONSOLE_LOG_LEVEL = args[++i];
                    case "-b" -> BOUNDS = args[++i].equals("y") ||
                            args[++i].equals("Y") || args[++i].equals("yes") || args[++i].equals("hell yeah");
                    case "-c" -> MapUtils.updateBounds(
                            Double.parseDouble(args[++i]), Double.parseDouble(args[++i]),
                            Double.parseDouble(args[++i]), Double.parseDouble(args[++i])
                    );
                }
            }
        }

    }

    private static void init(String[] args){
        parseArgs(args);

        if( NODE_IN_MAIN_COMPONENT == null){ NODE_IN_MAIN_COMPONENT =2432701015L;}
        if( SIMULATOR_SPEED == null){ SIMULATOR_SPEED = 10.0; }
        if( BOUNDS == null){ BOUNDS = true; }
        if( CONSOLE_LOG_LEVEL == null){ CONSOLE_LOG_LEVEL = "ALL"; }

        ConsoleLevel(CONSOLE_LOG_LEVEL);
    }

    private static String chooseFile() {
        // JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        JFileChooser jfc = new JFileChooser("server/java/data");
        jfc.setDialogTitle("Select .osm.pbf file to read");
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        return "Not A Valid Path";
    }

    private App() {}

    private static final String Instructions =
            """
            Usage: rideShare.exe  [.pbf map path] [-n] [-s] [-l] [-b] [-c]\s

            Options:
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


