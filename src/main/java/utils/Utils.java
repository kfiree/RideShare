package utils;

import org.apache.commons.math3.geometry.Point;

import java.awt.geom.Point2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;

import static utils.logs.LogHandler.LOGGER;

/**
 * todo check what package-private is.
 */
public class Utils {
    private static final ReentrantLock lock;
    private static final SimpleDateFormat DATE_FORMATTER;
    private static final SimpleDateFormat MINUTE_FORMATTER;
    public static final String ASSETS_PATH, LOGS_PATH, JSON_PATH;

    static{
        lock = new ReentrantLock();
        DATE_FORMATTER = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        MINUTE_FORMATTER = new SimpleDateFormat("HH:mm:ss");
        ASSETS_PATH = "data/assets/";
        LOGS_PATH = "data/logs/";
        JSON_PATH = "data/maps/";
    }

    public static String FORMAT(Date date){
        return MINUTE_FORMATTER.format(date);
    }

    public static String FORMAT(double d){
        return String.format("%.2f", d);
    }

    public static boolean validate(boolean condition, String errorMsg){
        if(!condition) {
            throwException(errorMsg );
        }
        return condition;
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


    public static long timeDiff(Date from, Date to){
        return to.getTime() - from.getTime();
    }

    public static void unLock(){
        lock.unlock();
    }


    public static double lineToPointDist(double px, double py, double x1, double y1, double x2, double y2){ // https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line
        double numerator = Math.abs((x2 - x1)*(y1 - py) - (x1 - px)*(y2 - y1));
        double denominator = Math.sqrt(Math.pow((x2-x1), 2) + Math.pow(y2-y1, 2));

        return numerator/denominator;
    }
}
