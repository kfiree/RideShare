package utils;

import javax.swing.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import static utils.LogHandler.LOGGER;

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

    public static String chooseFile() {
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
}
