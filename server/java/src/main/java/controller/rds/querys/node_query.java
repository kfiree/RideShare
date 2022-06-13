package controller.rds.querys;

import model.ONode;
import org.json.simple.JSONObject;

public class node_query {
//    static public String addNode(Node n){
//       return "INSERT INTO \"public\".\"rs_nodes\" (\"osm_Id\", \"node_Id\", \"latitude\", \"longitude\", \"degree\", \"edges\", \"tags\") " +
//                "VALUES " +
//                "('"+n.getOsm_Id()+"','"+ n.getId() +"', "+n.getLatitude()+", "+n.getLongitude()+", "+n.getDegree()+", '"+n.getEdges()+"', '"+n.getTags()+"');";
//    }
    static public String addNode(ONode n){
        JSONObject edges = new JSONObject();
        n.getEdges().forEach(e -> {
            int index = 0;
            edges.put("" + (++index), "" + e.getId());
        });
        JSONObject tags = new JSONObject();
        n.getTags().forEach((keyTag,valTag) -> {
            keyTag =  keyTag.replace("\"", "");
            keyTag =  keyTag.replace("\'", "");
            valTag =  valTag.replace("\"", "");
            valTag =  valTag.replace("\'", "");
            tags.put(keyTag , valTag);
        });
        return "INSERT INTO \"public\".\"rs_nodes\" (\"osm_Id\", \"node_Id\", \"latitude\", \"longitude\", \"degree\", \"edges\", \"tags\") " +
                "VALUES " +
                "('"+n.getOsm_Id()+"','"+ n.getId() +"', "+n.getLatitude()+", "+n.getLongitude()+", "+n.getDegree()+", '"+edges+"', '"+tags+"');";
    }
    static public String updateNode(ONode n) {
        JSONObject edges = new JSONObject();
        n.getEdges().forEach(e -> {
            int index = 0;
            edges.put("" + (++index), "" + e.getId());
        });
        JSONObject tags = new JSONObject();
        n.getTags().forEach((keyTag,valTag) -> {
            keyTag =  keyTag.replace("\"", "");
            keyTag =  keyTag.replace("\'", "");
            valTag =  valTag.replace("\"", "");
            valTag =  valTag.replace("\'", "");
            tags.put(keyTag , valTag);
        });
        return "UPDATE \"public\".\"rs_nodes\" SET " +
                "\"latitude\" = "+n.getLatitude()+", " +
                "\"longitude\" = "+n.getLongitude()+"," +
                "\"degree\" = "+n.getDegree()+", " +
                "\"edges\" = '"+edges+"', " +
                "\"tags\" = '"+tags+"' " +
                "WHERE \"node_Id\" = '"+n.getId()+"';";
    }
    static public String deleteNodeById(ONode n) {
        return "DELETE FROM \"public\".\"rs_nodes\" WHERE \"node_Id\" = '"+n.getId()+"';";
    }
    static public String getNodeById(ONode n) {
        return "SELECT * FROM \"public\".\"rs_nodes\" WHERE \"node_Id\" = '"+n.getId()+"';";
    }
    static public String getAllNodes() {
        return "SELECT * FROM \"public\".\"rs_nodes\";";
    }
}
