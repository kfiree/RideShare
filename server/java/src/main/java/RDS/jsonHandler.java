package RDS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import osmProcessing.OEdge;
import osmProcessing.OGraph;
import osmProcessing.ONode;

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

                OGraph.getInstance().addEdge(null, new OEdge(edge_Id, startNodeId, endNodeId, weight, distance, name, highwayType));

            });
            return null;
        } catch (Exception e) {
            throw new IllegalStateException("Invalid JSON: " + jsonNode, e);
        }
    }
}
