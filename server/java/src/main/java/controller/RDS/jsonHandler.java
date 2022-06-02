package controller.RDS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import model.OEdge;
import model.OGraph;
import model.ONode;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
}
