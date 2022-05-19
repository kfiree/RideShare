package RDS.querys;

import RDS.models.Node;
import org.json.simple.JSONObject;

public class nodes {
    public String addNode(Node n){
       return "INSERT INTO \"public\".\"rs_nodes\" (\"node_Id\", \"latitude\", \"longitude\", \"degree\", \"edges\", \"tags\") " +
                "VALUES " +
                "('"+n.getNode_Id()+"', "+n.getLatitude()+", "+n.getLongitude()+", "+n.getDegree()+", '"+n.getEdges()+"', '"+n.getTags()+"');";
    }
    public String updateNode(Node n) {
        return "UPDATE \"public\".\"rs_nodes\" SET " +
                "\"latitude\" = "+n.getLatitude()+", " +
                "\"longitude\" = "+n.getLongitude()+"," +
                "\"degree\" = "+n.getDegree()+", " +
                "\"edges\" = '"+n.getEdges()+"', " +
                "\"tags\" = '"+n.getTags()+"' " +
                "WHERE \"node_Id\" = '"+n.getNode_Id()+"';";
    }
    public String deleteNodeById(Node n) {
        return "DELETE FROM \"public\".\"rs_nodes\" WHERE \"node_Id\" = '"+n.getNode_Id()+"';";
    }
    public String getNodeById(Node n) {
        return "SELECT * FROM \"public\".\"rs_nodes\" WHERE \"node_Id\" = '"+n.getNode_Id()+"';";
    }
    public String getAllNodes() {
        return "SELECT * FROM \"public\".\"rs_nodes\";";
    }
}
