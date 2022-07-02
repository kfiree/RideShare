package controller.rds;

import controller.rds.querys.*;
import org.json.simple.JSONObject;
import model.RoadMap;

import static controller.rds.checkQuerys.connection;

public class addMapToDB {

//    nodes nodes = new nodes();
//    edges edges = new edges();
//    public addMapToDB(OMap map) {
//        this.map = map;
//    }

//    public OMap getMap() {
//        return map;
//    }
//
//    public void setMap(OMap map) {
//        this.map = map;
//    }


    static public void addToDB(RoadMap map) {
        uploadNodes(map);
        uploadEdges(map);
    }
    public static void uploadNodes(RoadMap map){
        map.getNodes().forEach(node -> {
            //nodes
            JSONObject edges = new JSONObject();
            node.getEdges().forEach(e -> {
                int index = 0;
                edges.put("" + (++index), "" + e.getId());
            });
//            JSONObject tags = new JSONObject();
//            node.getTags().forEach((keyTag,valTag) -> {
//                keyTag =  keyTag.replace("\"", "");
//                keyTag =  keyTag.replace("\'", "");
//                valTag =  valTag.replace("\"", "");
//                valTag =  valTag.replace("\'", "");
//                tags.put(keyTag , valTag);
//            });

            System.out.println("addNode -> " + checkQuerys.addToDB.addToDB(connection, node_query.addNode(node)));
        });
    }

    public static void uploadEdges(RoadMap map){
        map.getEdges().forEach(edge -> {
            //edges
//            OEdge e = new OEdge(edge.getStartNode().getOsmID(), edge.getEndNode().getOsmID(), edge.getWeight(), edge.getWeight(), edge.getName(), edge.getHighwayType());
            System.out.println(edge_query.addEdge(edge));
            System.out.println("addEdge -> " + checkQuerys.addToDB.addToDB(connection, edge_query.addEdge(edge)));
        });
    }


    @Override
    public String toString() {
        return "addMapToDB{" +
                "map=" +
                '}';
    }
}
