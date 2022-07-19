package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import static utils.LogHandler.LOGGER;

/**
 * todo check what package-private is.
 */
public class Utils {
    private static final ReentrantLock lock;
    private static final SimpleDateFormat DATE_FORMATTER;
    public static final String ASSETS_PATH, LOGS_PATH, JSON_PATH;

    static{
        lock = new ReentrantLock();
        DATE_FORMATTER = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        ASSETS_PATH = "data/assets/";
        LOGS_PATH = "data/logs/";
        JSON_PATH = "data/maps/";
    }

    public static String FORMAT(Date date){
        return DATE_FORMATTER.format(date);
    }

    public static String FORMAT(double d){
        return String.format("%.2f", d);
    }

    public static boolean validate(boolean condition, String errorMsg){
        if(!condition) {
            throwException(errorMsg );
        }
        return !condition;
    }

    public static void throwException(String errorMsg){
        try {
            LOGGER.severe(errorMsg);
            throw new Exception(errorMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void lock(boolean makeThemWait){
        lock.lock();
        if(!makeThemWait){
            unLock();
        }
    }

    public static void unLock(){
        lock.unlock();
    }
}
