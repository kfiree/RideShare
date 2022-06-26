package controller.utils;

import controller.utils.logs.LineFormat;
import controller.utils.logs.colorFormat;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * TODO sources:
 *  use LoggerExample.java    ->     https://examples.javacodegeeks.com/core-java/util/logging/java-util-logging-example/
 *  current reference         ->     https://www.journaldev.com/977/logger-in-java-logging-example
 *  close handler             ->     https://stackoverflow.com/questions/1154025/problem-with-java-util-logging-and-lock-file
 */
public class LogHandler{
    public static Logger LOGGER;

    static{
        LOGGER = Logger.getLogger(LogHandler.class.getName());
        LOGGER.setUseParentHandlers(false);
        LOGGER.setLevel(Level.ALL);

        try {
//            LogManager.getLogManager().readConfiguration(new FileInputStream(
//                    "server/java/src/main/java/controller/utils/logs/logging.properties"
//            ));

            //file handler
            Handler fileHandler = new FileHandler("server/java/data/logs/log_", 20000, 10);
            fileHandler.setFormatter(new LineFormat());
            fileHandler.setLevel(Level.ALL);

            //console handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new colorFormat());
            consoleHandler.setLevel(Level.ALL);

            //add custom handlers
            LOGGER.addHandler(fileHandler);
            LOGGER.addHandler(consoleHandler);

//            for(Handler handler : LOGGER.getHandlers()){
//                handler.setLevel(Level.ALL);
//            }

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
//        logHandler = this;
    }

    public static void closeLogHandlers(){
        for(Handler h: LOGGER.getHandlers()){
            System.out.println(h.toString());
            h.close();
        }
    }

    public static void log(Level severity, String msg) {
        LOGGER.log(severity, msg);
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


