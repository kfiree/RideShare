package controller.utils;

import model.Drive;
import model.GeoLocation;
import model.Node;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static controller.utils.LogHandler.LOGGER;


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
public final class MapUtils {
    private static final Map<String, Node> _riders = new HashMap<>(), locations= new HashMap<>();//TODO check if locations needed
    private static final Map<String, Drive>   drives = new HashMap<>();
    private static boolean bound;
    private static Double _topLatitude = 32.13073917015928, _bottomLatitude = 32.0449580796914,
                        _topLongitude = 34.852006, _bottomLongitude = 34.72856;

    private MapUtils() {}

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
        MapUtils.bound = bound;
    }

    public static void updateBounds(@NotNull Double topLatitude, @NotNull Double bottomLatitude, @NotNull Double topLongitude, @NotNull Double bottomLongitude){

        _topLatitude = topLatitude;
        _bottomLatitude = bottomLatitude;
        _topLongitude = topLongitude;
        _bottomLongitude = bottomLongitude;

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
        return _topLatitude;
    }

    public static Double getMinLatitude() {
        return _bottomLatitude;
    }

    public static Double getMaxLongitude() {
        return _topLongitude;
    }

    public static Double getMinLongitude() {
        return _bottomLongitude;
    }

    public static boolean inBound(GeoLocation location){
        return inBound(location.getLongitude(), location.getLatitude());
    }

    public static boolean inBound(double longitude, double latitude ){
        if(!bound) {
            return true;
        }

        boolean longitudeInBound = longitude < _topLongitude && longitude > _bottomLongitude;
        boolean latitudeInBound = latitude < _topLatitude && latitude > _bottomLatitude;

        return  latitudeInBound && longitudeInBound;
    }

    /**
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
        MapUtils.locations.putAll(locations);
    }

    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public static void validate(boolean condition, String errorMsg){
        if(!condition) {
            throwException(errorMsg);
        }

    }

    public static void throwException(String errorMsg){
        try {
            LOGGER.severe(errorMsg);

            throw new Exception(errorMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
