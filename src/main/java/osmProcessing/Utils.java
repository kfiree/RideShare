package osmProcessing;

public class Utils {

    private static Utils instance = new Utils();

    public static synchronized Utils getInstance() {
        return instance;
    }
    /**
     * This function will be adressed by all distance calculations
     * between two nodes in space
     * Current calculation method: Haversine Method
     * @param lat1 start node latitude
     * @param lon1 start node longitude
     * @param lat2 end node latitude
     * @param lon2 end node longitude
     * @return calculated distance between two nodes
     */
    //Function to find distance between two earth coordinates
    public double distance(double lat1, double lon1, double lat2, double lon2, String... unit) {
        String unit1 = unit.length > 0 ? unit[0] : "K";

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit1.equals("K")) {
            dist = dist * 1.609344;
        } else if (unit1.equals("N")) {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
