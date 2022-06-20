package controller.rds;

import model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public enum jsonHandler {
    NodeType, EdgeType, MapType, PathType, RiderType, ListType, HashMapType; //, IntegerType, DoubleType, StringType;

    /** json to object */
    public <T> T jsonToObj(String json) throws ParseException {
        switch(this){
            case NodeType:
                return (T) jsonToNode(json);
            case EdgeType:
                return (T) jsonToEdge(json);
            case MapType:
                return (T) jsonToMap(json);
            case PathType:
                return (T) jsonToPath(json);
            case RiderType:
                return (T) jsonToRider(json);
            case ListType:
                return (T) jsonToList(json);
            case HashMapType:
                return (T) jsonToHashMap(json);
//            case DoubleType:
//                break;
//            case StringType:
//                break;
//            case IntegerType:
//                break;
            default:
                throw new IllegalStateException("value need to be one of the follow: " + Arrays.toString(values()));
        }
    }

    private static Edge jsonToMap(String jsonNode){return null;}

    private static Node jsonToNode(String jsonString) throws ParseException {

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
        Long osmId = (Long) jsonObject.get("osm_Id");
        String nodeId = (String) jsonObject.get("node_Id");
        double latitude = (double) jsonObject.get("latitude");
        double longitude = (double) jsonObject.get("longitude");
        List<Edge> edges = jsonToEdges((JSONArray) jsonObject.get("edges"));

        Node node = RegionMap.getInstance().addNode(nodeId, osmId, latitude, longitude, Node.userType.None);
        edges.forEach(e->node.addEdge(e));

        return node;

    }

    private static List<Edge> jsonToEdges(JSONArray jsonEdges){
        List<Edge> edges = new ArrayList<>();
        jsonEdges.forEach(jsonEdge -> edges.add(jsonToEdge( (JSONObject) jsonEdge)));
        return edges;
    }

    private static Edge jsonToEdge(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        return jsonToEdge((JSONObject) parser.parse(jsonString));
    }

    private static Edge jsonToEdge(JSONObject jsonObj){
        String edgeId = (String) jsonObj.get("edge_Id");
        Long startNodeId = (Long) jsonObj.get("startNodeId");
        Long endNodeId = (Long) jsonObj.get("endNodeId");
        Double weight = (Double) jsonObj.get("weight");
        String highwayType = (String) jsonObj.get("highwayType");

        return RegionMap.getInstance().addEdge(edgeId, startNodeId, endNodeId, weight, highwayType);
    }

    private static Edge jsonToPath(String jsonNode){return null;}

    private static Edge jsonToRider(String jsonNode){return null;}

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

    private static Map<Object, Object> jsonToHashMap(String json) throws ParseException {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(json);
            return (Map)obj;
        } catch (ParseException e) {
            throw e;
        }
    }


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
            case RiderType:
//                return (T) jsonToRider(obj);
            case ListType:
//                return (T) jsonToList(obj);
            case HashMapType:
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


    private static String MapToJson(RegionMap map){return null;}

    private static JSONObject nodeToJson(Node node) throws ParseException {
        JSONObject obj = new JSONObject();

        obj.put("node_Id", node.getId());
        obj.put("osm_Id", node.getOsmID());
        obj.put("latitude", node.getLatitude());
        obj.put("longitude", node.getLongitude());
        obj.put("edges", listToJson(node.getEdges()));
//        obj.put("tags", mapToJson(node.getTags()));

        return obj;
    }



    private static JSONObject edgeToJson(Edge edge){
        JSONObject obj = new JSONObject();

        obj.put("edge_Id", edge.getId());
        obj.put("startNodeId", edge.getStartNode().getId());
        obj.put("endNodeId", edge.getStartNode().getId());
        obj.put("highwayType", edge.getStartNode().getId());


        return obj;
//        private ONode startNode, endNode;
//
//        private Double distance, weight, length; //TODO clean redundant field
//        static Set<String> tagNames = new HashSet<>();
//        private String id = "", name, highwayType;
//        private boolean directed;
//
//        private final Hashtable<String, Integer> speedLimit =  new Hashtable<>()
//        {{
//            put("motorway", 110);
//            put("trunk", 100);
//            put("primary", 90);
//            put("secondary", 70);
//            put("tertiary", 50);
//        }};
    }

    private static String pathToJson(String jsonPath){return null;}

    private static String riderToJson(String jsonRider){return null;}

    private static JSONArray listToJson(List list) throws ParseException {
        JSONArray jsonArray = new JSONArray();

        for(Object obj: jsonArray)
            list.add(obj);

        return jsonArray;
    }

//    private static String mapToJson(Map<Object, Object> map) throws ParseException {
//        JSONObject jsonObject = new JSONObject();
//
//        for(Map.Entry<String,String> entry : map.entrySet())
//
//        try {
//            JSONParser parser = new JSONParser();
//            Object obj = parser.parse(json);
//            return (Map)obj;
//        } catch (ParseException e) {
//            throw e;
//        }
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