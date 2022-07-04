package rideshare;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * todo check what package-private is.
 */
public class Utils {
    private static final SimpleDateFormat DATE_FORMATTER;
    public static final String ASSETS_PATH, LOGS_PATH, JSON_PATH;

    static{
        DATE_FORMATTER = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        ASSETS_PATH = "server/java/data/assets/";
        LOGS_PATH = "server/java/data/logs/";
        JSON_PATH = "server/java/data/maps/";
    }

    public static String FORMAT(Date date){
        return DATE_FORMATTER.format(date);
    }

    public static String FORMAT(double d){
        return String.format("%.2f", d);
    }
}
