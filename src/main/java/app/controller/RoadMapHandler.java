package app.controller;

import app.model.GeoLocation;
import app.model.Node;

import org.jetbrains.annotations.NotNull;

import java.util.*;

import static utils.Utils.throwException;


/**
 *               |==================================|
 *               |===========| MAP UTILS |==========|
 *               |==================================|
 *
 *  manage map and objects on map
 *
 * @author  Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 1.0
 * @since   2021-06-20
 */
public final class RoadMapHandler {
    private static final Map<String, Node> locations= new HashMap<>();//TODO check if locations needed
    private static boolean bound;
    private static Double maxLatitude = 32.13073917015928, minLatitude = 32.0449580796914,
                        maxLongitude = 34.852006, minLongitude = 34.72856;



    /**
     *   |=================================|
     *   |======| MANAGE MAP BOUNDS |======|
     *   |=================================|
     *
     *  manage map bounds by coordinates where.
     *
     *      longitude = x axis
     *      latitude = y axis
     */

    public static void setBounds(boolean bound){
        RoadMapHandler.bound = bound;
    }

    public static void updateBounds(@NotNull Double topLatitude, @NotNull Double bottomLatitude, @NotNull Double topLongitude, @NotNull Double bottomLongitude){

        RoadMapHandler.maxLatitude = topLatitude;
        minLatitude = bottomLatitude;
        maxLongitude = topLongitude;
        minLongitude = bottomLongitude;

        if(topLatitude <bottomLatitude || topLongitude < bottomLongitude) {
            boolean longitudeException = topLongitude < bottomLongitude;
            String coordinateType = longitudeException? "Longitude" : "Latitude";
            Double top = longitudeException? topLongitude : topLatitude,
                    bottom = longitudeException? bottomLongitude: bottomLatitude ;
            throwException("Illegal Map Bounds. Top " + coordinateType + " bound can not be smaller then bottom " + coordinateType + ". " + top + " < " + bottom + ".");
        }

    }

    public static void updateBounds(@NotNull GeoLocation topRightBound, @NotNull GeoLocation bottomLeftBound){
        updateBounds(topRightBound.getLatitude(), bottomLeftBound.getLatitude(), topRightBound.getLongitude(), bottomLeftBound.getLongitude());
    }

    public static Double getMaxLatitude() {
        return maxLatitude;
    }

    public static Double getMinLatitude() {
        return minLatitude;
    }

    public static Double getMaxLongitude() {
        return maxLongitude;
    }

    public static Double getMinLongitude() {
        return minLongitude;
    }

    public static boolean inBound(GeoLocation location){
        return inBound(location.getLongitude(), location.getLatitude());
    }

    public static boolean inBound(double longitude, double latitude ){
        if(!bound) {
            return true;
        }

        boolean longitudeInBound = longitude < maxLongitude && longitude > minLongitude;
        boolean latitudeInBound = latitude < maxLatitude && latitude > minLatitude;

        return  latitudeInBound && longitudeInBound;
    }


    /*
     *   |================================|
     *   |=======| MAP PROPERTIES |=======|
     *   |================================|
     *
     *   manage objects on map (besides nodes and edges)
     */

    public static Map<String, Node> getLocations() {
        return locations;
    }

    public static void setLocations(Map<String, Node> locations) {
        RoadMapHandler.locations.putAll(locations);
    }

    public static String generateId() {
        return UUID.randomUUID().toString();
    }



    /* private constructor for static class */
    private RoadMapHandler() {}
}
