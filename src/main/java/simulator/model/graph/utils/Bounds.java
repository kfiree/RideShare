package simulator.model.graph.utils;

import org.jetbrains.annotations.NotNull;


public final class Bounds {
    private static boolean bound;
    private static Double maxLatitude = 32.13073917015928, minLatitude = 32.0449580796914,
            maxLongitude = 34.852006, minLongitude = 34.72856;

    private Bounds(){}

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
        Bounds.bound = bound;
    }

    public static void updateBounds(@NotNull Double topLatitude, @NotNull Double bottomLatitude, @NotNull Double topLongitude, @NotNull Double bottomLongitude){
//        LogHandler.LOGGER.info("update bound to " + topLatitude+ ", " + bottomLatitude+ ", "+ topLongitude+ ", "+ bottomLongitude+ ", ");

        maxLatitude = topLatitude;
        minLatitude = bottomLatitude;
        maxLongitude = topLongitude;
        minLongitude = bottomLongitude;

        if(topLatitude <bottomLatitude || topLongitude < bottomLongitude) {
            boolean longitudeException = topLongitude < bottomLongitude;
            String coordinateType = longitudeException? "Longitude" : "Latitude";
            Double top = longitudeException? topLongitude : topLatitude,
                    bottom = longitudeException? bottomLongitude: bottomLatitude ;

//            Utils.throwException(
            throw new RuntimeException("Illegal Map Bounds. Top " + coordinateType + " bound can not be smaller then bottom " + coordinateType + ". " + top + " < " + bottom + ".");
        }

    }

    public static void updateBounds(@NotNull Coordinates topRightBound, @NotNull Coordinates bottomLeftBound){
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

    public static boolean inBound(Coordinates location){
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
}
