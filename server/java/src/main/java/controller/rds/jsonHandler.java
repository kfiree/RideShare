package controller.rds;

import controller.utils.GraphAlgo;
import model.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.*;


public enum jsonHandler {
    RoadMapType, UserMapType, NodeType, EdgeType,  PathType, DriverType,  PedestrianType,
    ListType, MapType;

    /** json to object */
//    public <T> T jsonToObj(String json, RoadMap roadMap) throws ParseException {
//        return switch (this) {
//            case RoadMapType -> null;
//            case DriverType -> (T) jsonToDriver(json, roadMap);
//            case PedestrianType -> (T) jsonToPedestrian(json, roadMap);
//            case PathType -> (T) jsonToPath(json);
//            case NodeType -> (T) jsonToNode(json, roadMap);
//            case EdgeType -> (T) jsonToEdge(json, roadMap);
//
//            /* java types */
//            case ListType -> (T) jsonToList(json);
//            case MapType -> (T) jsonToMap(json);
////            case DoubleType:
////                break;
////            case StringType:
////                break;
////            case IntegerType:
////                break;
//            default -> throw new IllegalStateException("value need to be one of the follow: " + Arrays.toString(values()));
//        };
//    }
    private static JSONObject readFile(String filePath){
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(filePath)) {
            return (JSONObject) parser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    private static void loadRoadMap(String filePath){
        JSONObject jsonObject = readFile(filePath);
        RoadMap roadMap = RoadMap.INSTANCE;

        JSONArray nodesArray = (JSONArray) jsonObject.get("nodes");
        System.out.println(nodesArray);
        JSONArray edgesArray = (JSONArray) jsonObject.get("edges");
        System.out.println(edgesArray);

        nodesArray.forEach(node -> jsonToNode((JSONObject) node, roadMap));

        edgesArray.forEach(node -> jsonToEdge((JSONObject) node, roadMap));
    }

    private static Node jsonToNode(JSONObject jsonObj, RoadMap roadMap){

        JSONObject nodeObj = (JSONObject) jsonObj.get("node");


        String id = (String) nodeObj.get("id");
        Long osm_id = (Long) nodeObj.get("osm_Id");
        double lat = (double) nodeObj.get("latitude");
        double lon = (double) nodeObj.get("longitude");

        return roadMap.addNode(id,osm_id, lat, lon);

    }

    private static Edge jsonToEdge(JSONObject jsonObj, RoadMap roadMap){
        String edgeId = (String) jsonObj.get("edge_Id");
        Long startNodeId = (Long) jsonObj.get("startNodeId");
        Long endNodeId = (Long) jsonObj.get("endNodeId");
        Double weight = (Double) jsonObj.get("weight");
        String highwayType = (String) jsonObj.get("highwayType");

        return roadMap.addEdge(edgeId, startNodeId, endNodeId, weight, highwayType);
    }

    private static void loadUsersMap(String filePath){
        JSONObject jsonObject = readFile(filePath);
        UsersMap usersMap = UsersMap.INSTANCE;

        JSONArray pedestriansArray = (JSONArray) jsonObject.get("nodes");
        System.out.println(pedestriansArray);
        JSONArray drivesArray = (JSONArray) jsonObject.get("edges");
        System.out.println(drivesArray);

        pedestriansArray.forEach(pedestrian -> jsonToPedestrian((JSONObject) pedestrian, usersMap));

        drivesArray.forEach(drive -> jsonToDriver((JSONObject) drive, usersMap));
    }

    private static Pedestrian jsonToPedestrian(JSONObject jsonObj, UsersMap usersMap){
        JSONObject pedestrianObj = (JSONObject) jsonObj.get("pedestrian");


        String id = (String) pedestrianObj.get("id");
        Node src = RoadMap.INSTANCE.getNode((long)pedestrianObj.get("src_id"));
        Node dst = RoadMap.INSTANCE.getNode((long)pedestrianObj.get("dst_id"));
        Date date = new Date((long)pedestrianObj.get("date"));

        return usersMap.addPedestrian(id, src, dst, date);
    }

    private static Drive jsonToDriver(JSONObject jsonObj, UsersMap usersMap){
        JSONObject driverObj = (JSONObject) jsonObj.get("pedestrian");

//        Path path = jsonToPath((JSONObject) driverObj.get("path")); todo accept path

        String id = (String) driverObj.get("id");
        String type = (String) driverObj.get("type");
        Node src = RoadMap.INSTANCE.getNode((long)driverObj.get("src_id"));
        Node dst = RoadMap.INSTANCE.getNode((long)driverObj.get("dst_id"));
        Date date = new Date((long)driverObj.get("date"));

        return usersMap.addDrive(src, dst, type, id, date);
    }

    private static List<Object> jsonToList(String json) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        try {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(json);
            List<Object> list = new ArrayList<>();
            for(Object obj: jsonArray)
                list.add(obj);

            return list;
        } catch (ParseException e ) {
            throw e;
        }
    }

    private static Map<Object, Object> jsonToMap(String json) throws ParseException {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(json);
            return (Map<Object, Object>)obj;
        } catch (ParseException e) {
            throw e;
        }
    }





    //===================================================== NOT GOOD =====================================================

    /** object to json */
    public <T> String objToJson(Object obj){
        switch (this) {
            case NodeType:
//                return (T) jsonToNode(obj);
            case EdgeType:
//                return (T) jsonToEdge(obj);
            case MapType:
//                return (T) jsonToMap(obj);
            case PathType:
//                return (T) jsonToPath(obj);
            case DriverType:
//                return (T) jsonToDriver(obj);
            case ListType:
//                return (T) jsonToList(obj);
//                return (T) jsonToHashMap(obj);
//            case DoubleType:
//                break;
//            case StringType:
//                break;
//            case IntegerType:
//                break;
            default:
                throw new IllegalStateException("value need to be one of the follow: " + Arrays.toString(values()));
        }




//        if (obj instanceof ONode)
//            return "";
//        else  if (obj instanceof OEdge)
//            return "";
//        else if (obj instanceof OMap)
//            return "";
//        else if (obj instanceof OPath)
//            return "";
//        else if (obj instanceof ORider)
//            return "";

        // if obj is java type
        // Convert an object to JSON text. If this object is a Map or a List, and it's also a JSONAware, JSONAware will be considered firstly.
//        return JSONValue.toJSONString(obj);
    }

    private static JSONObject nodeToJson(Node node) throws ParseException {
        JSONObject obj = new JSONObject();

        obj.put("node_Id", node.getId());
        obj.put("osm_Id", node.getOsmID());
        obj.put("latitude", node.getLatitude());
        obj.put("longitude", node.getLongitude());
        obj.put("edges", node.getEdges());

        return obj;
    }

    private static JSONObject edgeToJson(Edge edge){
        JSONObject obj = new JSONObject();

        obj.put("edge_Id", edge.getId());
        obj.put("startNodeId", edge.getNode1().getId());
        obj.put("endNodeId", edge.getNode1().getId());
        obj.put("highwayType", edge.getNode1().getId());


        return obj;
    }

    private static String pathToJson(String jsonPath){return null;}

    private static String driverToJson(String jsonRider){return null;}

//    public static void jsonDrivesToPath(String filePath) {
//        //First set locations in GraphUtils
//        jsonLocationsToNodes("server/node/locations.json");
//
//        JSONParser jsonParser = new JSONParser();
//        List<Drive> drives = new ArrayList<>();
//
//        // Read JSON file
//        try (FileReader reader = new FileReader(filePath)) {
//            Object obj = jsonParser.parse(reader);
//
//            JSONArray drivesJsonArray = (JSONArray) obj;
//
//            //Iterate over drives array
//            drivesJsonArray.forEach(drive -> {
//                JSONObject jsonDrive = (JSONObject) drive;
//                String driverType = (String) jsonDrive.get("type");
//                String driveOwnerId = (String) jsonDrive.get("driver_id");
//                Date leaveTime = null;
//
//                try {
//                    leaveTime = JsonTODate(jsonDrive.get("leaveTime").toString());
//                } catch (java.text.ParseException e) {
//                    e.printStackTrace();
//                }
//
//                String srcId = (String) jsonDrive.get("src");
//                String dstId = (String) jsonDrive.get("dest");
//
//                Node src = GraphAlgo.findClosestNode(MapUtils.getLocations().get(srcId).getCoordinates(), RoadMap.INSTANCE.getNodes());
//                Node dst = GraphAlgo.findClosestNode(MapUtils.getLocations().get(dstId).getCoordinates(), RoadMap.INSTANCE.getNodes());
//                Path path = GraphAlgo.getShortestPath(src, dst);
//                drives.add(new Drive(path, driverType, driveOwnerId, leaveTime));
//            });
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void jsonLocationsToNodes(String filePath) {
//        JSONParser jsonParser = new JSONParser();
//        HashMap<String, Node> locations = new HashMap<>();
//
//        // Read JSON file
//        try (FileReader reader = new FileReader(filePath)) {
//            Object obj = jsonParser.parse(reader);
//
//            JSONArray jsonLocationsArray = (JSONArray) obj;
//
//            //Iterate over drives array
//            jsonLocationsArray.forEach(location -> {
//                JSONObject jsonObj = (JSONObject) location;
//                String locationId = (String) jsonObj.get("geo_loaction_id");
//                Double latitude = Double.parseDouble(( (String) jsonObj.get("latitude")));
//                Double longtitude = Double.parseDouble(( (String) jsonObj.get("longtitude")));
//
//                locations.put(locationId, new Node(locationId, null, new GeoLocation(latitude, longtitude)));
//            });
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }
//        MapUtils.setLocations(locations);
//    }

//    // method to parse ISO String to Date object
//    public static Date JsonTODate(JSONObject dateObj) throws java.text.ParseException {
//        dateObj
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        format.setTimeZone(TimeZone.getTimeZone("UTC"));
//        return format.parse(dateObj);
//    }





    /** write & read file */
    public static void jsonToFile(String jsonObj, String filePath){
        System.out.println("saving to " + filePath);

        //Write JSON file
        try (FileWriter file = new FileWriter(filePath)) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(jsonObj);
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static String fileToJson(String filePath){
//
//    }

//    public static List fileToList(String filePath){
//        JSONParser jsonParser = new JSONParser();
//        List<Long> list = new ArrayList<>();
//
//        try (FileReader reader = new FileReader(filePath))
//        {
//            //Read JSON file
//            Object obj = jsonParser.parse(reader);
//
//            JSONArray jsonArray = (JSONArray) obj;
//
//            //Iterate over employee array
//            jsonArray.forEach( e -> {
//                list.add((Long) e);
//            });
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }


}