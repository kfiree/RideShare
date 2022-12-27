package simulator;

import simulator.controller.Simulator;
import simulator.model.graph.utils.Bounds;
import utils.logs.LogHandler;

import java.util.Arrays;

/**
 *
 *  args[] {
 *      * map file path (.pbf file).
 * <p>
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
 *      * to operate with intellij with args[]
 *           edit configuration -> Name: APP -> Build and operate -> Program arguments -> insert relevant flags
 *
 *
 *      * to convert files:
 *           osmconvert64.exe ariel2.osm > file.pbf --out-pbf
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
 *       https://docs.google.com/document/d/1r4gWm002QzLkzu5Wc1mgb3C5IQuyaunEAUmF9_46nEg/edit#
 *
 */
public final class Main {
    private static Double SIMULATOR_SPEED;
    private static String CONSOLE_LOG_LEVEL, PBF_PATH;
    private static Boolean BOUNDS, SHOW_MAP, PARSE_FROM_PBF;
    private static final int DRIVE_NUM, REQUEST_NUM;
    private static final String INSTRUCTIONS;
    private Main() {}

    static{
        PARSE_FROM_PBF = false;
        PBF_PATH = "/Users//Desktop";
        SIMULATOR_SPEED = 2.0;
        BOUNDS = true;
        CONSOLE_LOG_LEVEL =
//                "SEVERE";
                "ALL";
        SHOW_MAP = true;
        DRIVE_NUM = 15;
        REQUEST_NUM = 20;
    }

    public static void main(String[] args) {

        ExampleCLI();

        init(args);
        LogHandler.LOGGER.finest("Let's GO!!");

        try {
            Thread thread = new Thread(Simulator.INSTANCE);
            Simulator.INSTANCE.init(SIMULATOR_SPEED, REQUEST_NUM, DRIVE_NUM, SHOW_MAP, BOUNDS, PARSE_FROM_PBF);
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            LogHandler.LOGGER.severe(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        } finally {
            LogHandler.closeLogHandlers();
            LogHandler.LOGGER.info("Finished!");
        }


        System.exit(0);
    }

    private static void init(String[] args){
         for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) == '-') {
                switch (args[i]) {
                    case "-m" -> PARSE_FROM_PBF = false;
                    case "-s" -> SIMULATOR_SPEED = Double.parseDouble(args[++i]);
                    case "-l" -> CONSOLE_LOG_LEVEL = args[++i];
                    case "-b" -> BOUNDS = args[++i].equals("y") ||
                            args[++i].equals("Y") || args[++i].equals("yes") || args[++i].equals("hell yeah");
                    case "-c" -> Bounds.updateBounds(
                            Double.parseDouble(args[++i]), Double.parseDouble(args[++i]),
                            Double.parseDouble(args[++i]), Double.parseDouble(args[++i])
                    );
                    case "-h" -> {
                        System.out.println(INSTRUCTIONS);
                        return;
                    }
                }
            }
        }
        LogHandler.ConsoleLevel(CONSOLE_LOG_LEVEL);
    }

    private static void ExampleCLI(){
        System.out.println("java -jar RideShare.jar -h\n\n" + INSTRUCTIONS
                + "\n\n\njava -jar RideShare.jar   -s 10  -l ALL  -b y\n");
    }

    static {
        INSTRUCTIONS =
                """
                Usage: rideShare.exe  [.pbf map path] [-n] [-s] [-l] [-b] [-c]\s
    
                Options:
                    -m                      Load PBF file (use saved map otherwise).
                    -s speed                Speed of simulator.
                                            Default: 10.
                    -l log-level            Log level for console can be a number or a string value of 'java.util.logging.Level'.
                    -b 'y'/'n'              Set bounds to the map.
                    -c max/min coordinates  Set bounds coordinates - top latitude, bottom latitude, top longitude, bottom longitude.
                                            Default: value will show center of israel.
                """;
    }

}


