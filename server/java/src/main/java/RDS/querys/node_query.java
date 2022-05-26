package RDS.querys;

import RDS.models.Node;

public class node_query {
    static public String addNode(Node n){
       return "INSERT INTO \"public\".\"rs_nodes\" (\"osm_Id\", \"node_Id\", \"latitude\", \"longitude\", \"degree\", \"edges\", \"tags\") " +
                "VALUES " +
                "('"+n.getOsm_Id()+"','"+ n.getNode_id() +"', "+n.getLatitude()+", "+n.getLongitude()+", "+n.getDegree()+", '"+n.getEdges()+"', '"+n.getTags()+"');";
    }
    static public String updateNode(Node n) {
        return "UPDATE \"public\".\"rs_nodes\" SET " +
                "\"latitude\" = "+n.getLatitude()+", " +
                "\"longitude\" = "+n.getLongitude()+"," +
                "\"degree\" = "+n.getDegree()+", " +
                "\"edges\" = '"+n.getEdges()+"', " +
                "\"tags\" = '"+n.getTags()+"' " +
                "WHERE \"node_Id\" = '"+n.getOsm_Id()+"';";
    }
    static public String deleteNodeById(Node n) {
        return "DELETE FROM \"public\".\"rs_nodes\" WHERE \"node_Id\" = '"+n.getOsm_Id()+"';";
    }
    static public String getNodeById(Node n) {
        return "SELECT * FROM \"public\".\"rs_nodes\" WHERE \"node_Id\" = '"+n.getOsm_Id()+"';";
    }
    static public String getAllNodes() {
        return "SELECT * FROM \"public\".\"rs_nodes\";";
    }
}
