package controller.utils;

import java.io.IOException;
import java.util.logging.*;


public class LogHandler{
    public static LogHandler logHandler;
    public static Handler fileHandler;
    public static Logger logger;

    public LogHandler(){
        logger = Logger.getLogger(LogHandler.class.getName());

        try {
            fileHandler = new FileHandler("server/java/src/main/java/controller/utils/logs/logger.log", 2000, 5);
            fileHandler.setFormatter(new LineFormat());
            //adding custom handler
//            logger.addHandler(new controller.utils.logger.MyHandler());
            logger.addHandler(fileHandler);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        logHandler = this;
    }

    public void closeHandler(){
        fileHandler.close();
        for(Handler h: logger.getHandlers()){
            System.out.println(h.toString());
            h.close();
        }
    }
    public static void log(Level severity, String msg) {
        logger.log(severity, msg);
    }

//    public static void main(String[] args) {
//        LogHandler logger1 = new LogHandler();
//        logger1.log(Level.INFO, "sup??");
//        logger1.log(Level.SEVERE, "ohh noooo!!!");
//
//        logger1.closeHandler();
//    }

}


