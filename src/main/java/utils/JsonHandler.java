package utils;

import app.model.graph.Edge;
import app.model.graph.Node;
import app.model.graph.Path;
import app.model.graph.RoadMap;
import app.model.users.Driver;
import app.model.users.Rider;
import app.model.users.UserMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

import static utils.Utils.FORMAT;
import static utils.Utils.JSON_PATH;


public enum JsonHandler {
    All, RoadMapType, UserMapType;


    /* === ROAD MAP ===  */

    public void save() {
        switch (this) {
            case All -> writeToFile(JSON_PATH + "state", simulatorStateToJSON());
            case RoadMapType -> writeToFile(JSON_PATH + "roadMap", roadMapToJSON());
            case UserMapType -> writeToFile(JSON_PATH + "userMap", usersMapToJSON());
        }
    }

    public void load() {
        switch (this) {
            case All -> JSONToSimulatorState(Objects.requireNonNull(readFile(JSON_PATH + "state.json")));
            case RoadMapType -> JSONToRoadMap(Objects.requireNonNull(readFile(JSON_PATH + "roadMap.json")));
            case UserMapType -> JSONToUserMap(Objects.requireNonNull(readFile(JSON_PATH + "userMap.json")));
        }
    }

    private static void JSONToSimulatorState(JSONObject jsonObject){
        System.out.println("Load state to project...");
        JSONToRoadMap((JSONObject) jsonObject.get("roadMap"));
        JSONToUserMap((JSONObject) jsonObject.get("userMap"));
    }

    @SuppressWarnings("unchecked")
    private static JSONObject simulatorStateToJSON(){
        JSONObject stateJSON = new JSONObject();

        stateJSON.put("roads", roadMapToJSON());
        stateJSON.put("users", usersMapToJSON());

        return stateJSON;
    }

    @SuppressWarnings("unchecked")
    private static JSONObject roadMapToJSON(){
        JSONArray nodesJSON = new JSONArray();
        RoadMap.INSTANCE.getNodes().forEach(node -> nodesJSON.add(nodeToJSON(node)));

        JSONArray edgesJSON = new JSONArray();
        RoadMap.INSTANCE.getEdges().forEach(edge -> edgesJSON.add(edgeToJSON(edge)));

        JSONObject metaDataJSON = new JSONObject();
        metaDataJSON.put("nodes_size", RoadMap.INSTANCE.nodesSize());
        metaDataJSON.put("edges_size", RoadMap.INSTANCE.edgesSize());
        metaDataJSON.put("Creation_date", FORMAT(new Date()));

        JSONObject roadMapJSON = new JSONObject();

        roadMapJSON.put("meta", metaDataJSON);
        roadMapJSON.put("nodes", nodesJSON);
        roadMapJSON.put("edges", edgesJSON);

        System.out.println(metaDataJSON);
        return roadMapJSON;
    }

    @SuppressWarnings("unchecked")
    private static void JSONToRoadMap(JSONObject jsonObject){
        System.out.println("Load RoadMap to project...");

        System.out.println("file meta data : "+jsonObject.get("meta"));
        RoadMap roadMap = RoadMap.INSTANCE;

        JSONArray nodesArray = (JSONArray) jsonObject.get("nodes");
        JSONArray edgesArray = (JSONArray) jsonObject.get("edges");

        nodesArray.forEach(node -> jsonToNode((JSONObject) node, roadMap));

        edgesArray.forEach(node -> jsonToEdge((JSONObject) node, roadMap));
    }

    @SuppressWarnings("unchecked")
    private static JSONObject nodeToJSON(Node node){
        JSONObject nodeJSON = new JSONObject();

        nodeJSON.put("id", node.getId());
        nodeJSON.put("latitude", node.getLatitude());
        nodeJSON.put("longitude", node.getLongitude());
        node.getEdges().forEach(edge -> {});

        return nodeJSON;
    }

    private static void jsonToNode(JSONObject nodeJSON, RoadMap roadMap){
        Long osmId = (Long) nodeJSON.get("id");
        double lat = (double) nodeJSON.get("latitude");
        double lon = (double) nodeJSON.get("longitude");

        roadMap.addNode(osmId, lat, lon);

    }

    @SuppressWarnings("unchecked")
    private static JSONObject edgeToJSON(Edge edge){
        JSONObject edgeJSON = new JSONObject();

        boolean node1IsSrc = edge.getNode1().getEdges().contains(edge);
        boolean node2IsSrc = edge.getNode2().getEdges().contains(edge);
        int srcNode = node1IsSrc && node2IsSrc ? 0 : node1IsSrc? 1:2;

        edgeJSON.put("id", edge.getId());
        edgeJSON.put("node1", edge.getNode1().getId());
        edgeJSON.put("node2", edge.getNode2().getId());
        edgeJSON.put("weight", edge.getWeight());
        edgeJSON.put("highwayType", edge.getHighwayType());
        edgeJSON.put("bidirectional", srcNode);

        return edgeJSON;
    }

    private static void jsonToEdge(JSONObject edgeJSON, RoadMap roadMap){//todo save as long
        Long edgeId = (long) edgeJSON.get("id");
        Long startNodeId = (long) edgeJSON.get("node1");
        Long endNodeId = (long)  edgeJSON.get("node2");
//        long weight = (long) edgeJSON.get("weight");
        long weight =(long)Math.round((Double)(edgeJSON.get("weight")));
        String highwayType = (String) edgeJSON.get("highwayType");
        long direction = (long) edgeJSON.get("bidirectional");

        if(direction == 1L){
            roadMap.addEdge(edgeId, startNodeId, endNodeId, weight, highwayType);
        }else if(direction == 2L){
            roadMap.addEdge(edgeId, endNodeId, startNodeId, weight, highwayType);
        }else{
            roadMap.addEdge(edgeId, startNodeId, endNodeId, weight, highwayType);
            roadMap.addEdge(edgeId, endNodeId, startNodeId, weight, highwayType);
        }
    }



    //todo check read and write usersMap

    /* === USER MAP ===  */

    @SuppressWarnings("unchecked")
    private static JSONObject usersMapToJSON(){
        JSONArray drivesJSON = new JSONArray();
        UserMap.INSTANCE.getDrives()
                .forEach(drive -> drivesJSON.add(driveToJSON(drive)));

        JSONArray pedestriansJSON = new JSONArray();
        UserMap.INSTANCE.getRequests()
                .forEach(pedestrian -> pedestriansJSON.add(pedestrianToJSON(pedestrian)));

        JSONObject userMapJSON = new JSONObject();

        userMapJSON.put("nodes", drivesJSON);
        userMapJSON.put("pedestrians", pedestriansJSON);

        return userMapJSON;
    }

    @SuppressWarnings("unchecked")
    private static void JSONToUserMap(JSONObject jsonObject){
        System.out.println("Load RoadMap to project...");
        UserMap userMap = UserMap.INSTANCE;

        JSONArray pedestriansArray = (JSONArray) jsonObject.get("nodes");
        JSONArray drivesArray = (JSONArray) jsonObject.get("edges");

        pedestriansArray.forEach(pedestrian -> JSONToPedestrian((JSONObject) pedestrian, userMap));

        drivesArray.forEach(drive -> JSONToDrive((JSONObject) drive, userMap));
    }

    @SuppressWarnings("unchecked")
    private static JSONObject driveToJSON(Driver drive){
        JSONObject driveJSON = new JSONObject();

        driveJSON.put("id", drive.getId());
        driveJSON.put("date", drive.getStartTime().getTime());
        driveJSON.put("path", pathToJSON(drive.getPath()));


        return driveJSON;
    }

    private static void JSONToDrive(JSONObject driveObj, UserMap userMap){
        //        Path path = jsonToPath((JSONObject) driverObj.get("path")); todo accept path

        String id = (String) driveObj.get("id");
        Node src = RoadMap.INSTANCE.getNode((long) driveObj.get("src_id"));
        Node dst = RoadMap.INSTANCE.getNode((long) driveObj.get("dst_id"));
        Long date = (long) driveObj.get("date");

        userMap.addDrive(src, dst, date);
    }

    @SuppressWarnings("unchecked")
    private static JSONObject pedestrianToJSON(Rider rider){
        JSONObject pedestrianJSON = new JSONObject();

        pedestrianJSON.put("id", rider.getId());
        pedestrianJSON.put("src", rider.getLocation().getId());
        pedestrianJSON.put("dst", rider.getDestination().getId());
        pedestrianJSON.put("date", rider.getStartTime().getTime());

        return pedestrianJSON;
    }

    private static void JSONToPedestrian(JSONObject pedestrianObj, UserMap userMap){
        String id = (String) pedestrianObj.get("id");
        Node src = RoadMap.INSTANCE.getNode((long) pedestrianObj.get("src_id"));
        Node dst = RoadMap.INSTANCE.getNode((long) pedestrianObj.get("dst_id"));
        Date date = new Date((long) pedestrianObj.get("date"));

        userMap.addRequest(src, dst, date);
    }

    @SuppressWarnings("unchecked")
    private static JSONObject pathToJSON(Path path){
        JSONObject pathJSON = new JSONObject();

        JSONArray nodes = new JSONArray();

        for(Node node: path.getNodes()){
            nodes.add(node.getId());
        }

        pathJSON.put("nodes", nodes);
        return pathJSON;
    }

    @SuppressWarnings("unchecked")
    private static Path JSONToPath(JSONObject pathObj){

        JSONArray nodesJSON = (JSONArray) pathObj.get("nodes");
        List<Node> nodes = new ArrayList<>();
        nodesJSON.forEach(nodeID->nodes.add(RoadMap.INSTANCE.getNode((long)  nodeID)));

        return new Path(nodes, 0.0);
    }



    /* write & read file */

    private static void writeToFile(String filePath, JSONObject jsonObj){
        System.out.println("saving to " + filePath);

        try (FileWriter file = new FileWriter(filePath+ ".json")) {
            file.write(jsonObj.toJSONString());
            file.flush(); //todo check if need to flush
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject readFile(String filePath){
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(filePath)) {
            return (JSONObject) parser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}