package controller.RDS;

import controller.GraphUtils;
import model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class jsonHandler {
    static public ONode jsonToNode(String jsonNode){
        try {
            JSONParser parser = new JSONParser();
            ((JSONArray) parser.parse(jsonNode)).forEach(json -> {
                JSONObject jsonObj = (JSONObject) json;

                Long osmId = (Long) jsonObj.get("osm_Id");
                String nodeId = (String) jsonObj.get("node_Id");
                double latitude = (double) jsonObj.get("latitude");
                double longitude = (double) jsonObj.get("longitude");
                String edges = (String) jsonObj.get("edges");
                String tags = (String) jsonObj.get("tags");

                OGraph.getInstance().addNode(new ONode(osmId, nodeId, latitude, longitude, null, null));

            });
            return null;
        } catch (Exception e) {
            throw new IllegalStateException("Invalid JSON: " + jsonNode, e);
        }
    }

    static public OEdge jsonToEdge(String jsonNode){
        try {
            JSONParser parser = new JSONParser();
            ((JSONArray) parser.parse(jsonNode)).forEach(json -> {
                JSONObject jsonObj = (JSONObject) json;

                String edge_Id = (String) jsonObj.get("edge_Id");
                Long startNodeId = (Long) jsonObj.get("startNodeId");
                Long endNodeId = (Long) jsonObj.get("endNodeId");
                double distance = (double) jsonObj.get("distance");
                double weight = (double) jsonObj.get("weight");
                String name = (String) jsonObj.get("name");
                String highwayType = (String) jsonObj.get("highwayType");

                OGraph.getInstance().addEdge(new OEdge(edge_Id, startNodeId, endNodeId, weight, distance, name, highwayType));

            });
            return null;
        } catch (Exception e) {
            throw new IllegalStateException("Invalid JSON: " + jsonNode, e);
        }
    }

    public static void saveList(List<Long> list, String filePath){
        System.out.println("saving to " + filePath);
        //Add employees to list
        JSONArray jsonRemoved = new JSONArray();
        list.forEach(key->jsonRemoved.add(key));

        //Write JSON file
        try (FileWriter file = new FileWriter(filePath)) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(jsonRemoved.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Long> readList(String filePath){
        JSONParser jsonParser = new JSONParser();
        List<Long> list = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray jsonArray = (JSONArray) obj;

            //Iterate over employee array
            jsonArray.forEach( e -> {
                list.add((Long) e);
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void jsonDrivesToPath(String filePath) {
        //First set locations in GraphUtils
        jsonLocationsToNodes("server/node/locations.json");

        JSONParser jsonParser = new JSONParser();
        List<Drive> drives = new ArrayList<>();

        // Read JSON file
        try (FileReader reader = new FileReader(filePath)) {
            Object obj = jsonParser.parse(reader);

            JSONArray drivesJsonArray = (JSONArray) obj;

            //Iterate over drives array
            drivesJsonArray.forEach(drive -> {
                JSONObject jsonDrive = (JSONObject) drive;
                String driverType = (String) jsonDrive.get("type");
                String driveOwnerId = (String) jsonDrive.get("driver_id");
                Date leaveTime = null;

                try {
                    leaveTime = parseJsonDate(jsonDrive.get("leaveTime").toString());
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                String srcId = (String) jsonDrive.get("src");
                String dstId = (String) jsonDrive.get("dest");

                ONode src = OGraph.getInstance().findClosestNode(GraphUtils.getLocations().get(srcId));
                ONode dst = OGraph.getInstance().findClosestNode(GraphUtils.getLocations().get(dstId));
                List<Object> path = GraphUtils.AStar(src, dst);
                drives.add(new Drive(path, driverType, driveOwnerId, leaveTime));
            });
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void jsonLocationsToNodes(String filePath) {
        JSONParser jsonParser = new JSONParser();
        HashMap<String, ONode> locations = new HashMap<>();

        // Read JSON file
        try (FileReader reader = new FileReader(filePath)) {
            Object obj = jsonParser.parse(reader);

            JSONArray jsonLocationsArray = (JSONArray) obj;

            //Iterate over drives array
            jsonLocationsArray.forEach(location -> {
                JSONObject jsonObj = (JSONObject) location;
                String locationId = (String) jsonObj.get("geo_loaction_id");
                String latitude = (String) jsonObj.get("latitude");
                String longtitude = (String) jsonObj.get("longtitude");

                locations.put(locationId, new ONode(null, locationId, Double.parseDouble(latitude), Double.parseDouble(longtitude), null, null));
            });
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        GraphUtils.setLocations(locations);
    }

    // method to parse ISO String to Date object
    public static Date parseJsonDate(String date) throws java.text.ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.parse(date);
    }
}
