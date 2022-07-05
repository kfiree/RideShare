package utils;

import utils.logs.LineFormat;
import utils.logs.colorFormat;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * sources:
 *      use LoggerExample.java    ->     https://examples.javacodegeeks.com/core-java/util/logging/java-util-logging-example/
 *      current reference         ->     https://www.journaldev.com/977/logger-in-java-logging-example
 *      close handler             ->     https://stackoverflow.com/questions/1154025/problem-with-java-util-logging-and-lock-file
 */
public class LogHandler{
    public static Logger LOGGER;
    public static ConsoleHandler CONSOLE_HANDLER;
    public static Handler FILE_HANDLER;

    static{
        LOGGER = Logger.getLogger(LogHandler.class.getName());
        LOGGER.setUseParentHandlers(false);
        LOGGER.setLevel(Level.ALL);

        try {
//            LogManager.getLogManager().readConfiguration(new FileInputStream(
//                    "src/main/java/rideshare.controller/utils/logs/logging.properties"
//            ));

            //file handler src/data/logs
            FILE_HANDLER = new FileHandler("data/logs/log_", 20000, 10);
            FILE_HANDLER.setFormatter(new LineFormat());
            FILE_HANDLER.setLevel(Level.ALL);

            //console handler
            CONSOLE_HANDLER = new ConsoleHandler();
            CONSOLE_HANDLER.setFormatter(new colorFormat());
            CONSOLE_HANDLER.setLevel(Level.ALL);

            //add custom handlers
            LOGGER.addHandler(FILE_HANDLER);
            LOGGER.addHandler(CONSOLE_HANDLER);

//            for(Handler handler : LOGGER.getHandlers()){
//                handler.setLevel(Level.ALL);
//            }

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
//        logHandler = this;
    }

    public static void ConsoleLevel(String lvl) {
        CONSOLE_HANDLER.setLevel(Level.parse(lvl));
    }

    public static void HandlerLevel(String lvl) {
        FILE_HANDLER.setLevel(Level.parse(lvl));
    }

    public static void Level(String lvl) {
        ConsoleLevel(lvl);
        HandlerLevel(lvl);
    }

    public static void closeLogHandlers(){
        for(Handler h: LOGGER.getHandlers()){
            LOGGER.info("Closing" + h.toString());
            h.close();
        }
    }


    public static void main(String[] args) {

        LOGGER.severe( "log sever!");
        LOGGER.warning( "log warning");
        LOGGER.info( "log info");
        LOGGER.config( "log config");
        LOGGER.fine( "log fine");
        LOGGER.finer( "log finer");
        LOGGER.finest( "log finest");

    }

}


