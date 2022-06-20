package controller.utils;

import model.GeoLocation;
import model.Node;
import model.Path;
import org.jetbrains.annotations.NotNull;

import java.util.*;



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
public class MapUtils {
    private static List<Path> _paths =  new ArrayList<>();
    private static Hashtable<Path, String> labeledPaths = new Hashtable<>();
    private static Map<Long, Node> _riders = new HashMap<>();
    private static Double _topLongitude, _bottomLongitude, _topLatitude, _bottomLatitude;
    private static boolean bound;

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
        if(!MapUtils.bound){
            try {
                updateBounds(32.13073917015928, 32.0449580796914, 34.852006, 0.0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        MapUtils.bound = bound;
    }

    public static void updateBounds(@NotNull Double topLatitude, @NotNull Double bottomLatitude, @NotNull Double topLongitude, @NotNull Double bottomLongitude) throws Exception {

        _topLatitude = topLatitude;
        _bottomLatitude = bottomLatitude;
        _topLongitude = topLongitude;
        _bottomLongitude = bottomLongitude;

        if(topLatitude <bottomLatitude || topLongitude < bottomLongitude) {

            boolean longitudeException = topLongitude < bottomLongitude;

            String coordinateType = longitudeException? "Longitude" : "Latitude";
            Double top = longitudeException? topLongitude : topLatitude,
                    bottom = longitudeException? bottomLongitude: bottomLatitude ;

            throw new Exception("Illegal Map Bounds. Top " + coordinateType + " bound can not be smaller then bottom " + coordinateType + ". " + top + " < " + bottom + ".");
        }

    }

    public static void updateBounds(@NotNull GeoLocation topRightBound, @NotNull GeoLocation bottomLeftBound) throws Exception {
        updateBounds(topRightBound.getLatitude(), bottomLeftBound.getLatitude(), topRightBound.getLongitude(), bottomLeftBound.getLongitude());
    }

    public static boolean inBound(double longitude, double latitude ){
        if(!bound) {
            return true;
        }
//        return node.getLatitude() <= 32.13073917015928 && node.getLatitude() >= 32.0449580796914 && node.getLongitude() <=34.852006;
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


    /** GETTERS */

    public static boolean addPath(List<Object> pathNodes){
        return _paths.add(new Path(pathNodes));
    }

    public static String addLabeledPath(List<Object> pathNodes, String label){
        return labeledPaths.put(new Path(pathNodes), label);
    }

    public static void setPaths(List<Path> paths) {
        _paths.addAll(paths);
    }

    public static boolean setRiders(Map<Long, Double[]> Riders){

//        Riders.entrySet().forEach(entry -> {
//            Node node = new Node(null, entry.getKey(), entry.getValue()[0], entry.getValue()[1], Node.userType.Rider);
//            node.setUser(Node.userType.Rider);
//            _riders.put(entry.getKey(), node);
//        });

        return true;
    }

    /** SETTERS */

    public static Hashtable<Path, String> getLabeledPaths(){
        return labeledPaths;
    }

    public static List<Path> getPaths() {
        return _paths;
    }

    public static Map<Long, Node> getRiders() {
        return _riders;
    }

    /** create uuid */
    public static String generateId(Object object) {
        return UUID.randomUUID().toString();
    }
}

//    public static Map<Long, Map<Long, OEdge>> getNodeEdges() {
//        return nodeEdges;
//    }

//    public static void addEdgeToMap(ONode n1, ONode n2, OEdge e){
//        addEdgeHelper(n2, n1, e);
//        addEdgeHelper(n1, n2, e);
//    }
//
//    private static void addEdgeHelper(ONode n1, ONode n2, OEdge e) {
//        if(!nodeEdges.containsKey(n2.getOsmID())){
//            Map<Long, OEdge> n2Map = new HashMap<>();
//            n2Map.put(n1.getOsmID(),e);
//            nodeEdges.put(n2.getOsmID(), n2Map);
//        }else{
//            nodeEdges.get(n2.getOsmID()).put(n1.getOsmID(), e);
//        }
//    }