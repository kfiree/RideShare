package app.model.utils;

import app.controller.osm_processing.Parser;
import app.controller.osm_processing.Reader;
import app.model.graph.Node;

import crosby.binary.osmosis.OsmosisReader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLOutput;
import java.util.*;

import static app.model.utils.GraphAlgo.extractLargestCC;
import static utils.logs.LogHandler.LOGGER;
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
    private RoadMapHandler() {}


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
        System.out.println("update bound to " + topLatitude+ ", " + bottomLatitude+ ", "+ topLongitude+ ", "+ bottomLongitude+ ", ");
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

    /* CREATE MAP */
    public static void CreateMap(String path) {
        String pbfFilePath =  path;
                //"data/maps/osm/berlin.pbf";
                //ChooseRegion.choose();

        try {
            InputStream inputStream = new FileInputStream(pbfFilePath);

            // read from osm pbf file:
            Reader reader = new Reader();
            OsmosisReader osmosisReader = new OsmosisReader(inputStream);
            osmosisReader.setSink(reader);

            // initial parsing of the .pbf file:
            LOGGER.info("Start reading osm file.");
            osmosisReader.run();

            // secondary parsing of ways/creation of edges:
            Parser parser = new Parser();
            LOGGER.info("Start parsing Reader's data.");
            parser.parseMapWays(reader.getWays());

            // clean road map
            extractLargestCC();
//            GraphAlgo.removeNodesThatNotConnectedTo(RoadMap.INSTANCE.getNode(2432701015L));

        } catch (FileNotFoundException e) {
            LOGGER.severe("File not found!, "+e.getMessage());
            JOptionPane.showMessageDialog(new JFrame(), "File not found!", "ERROR",
                    JOptionPane.ERROR_MESSAGE);

            System.exit(0);
        }
    }
}
