package RDS.models;

import org.json.simple.JSONObject;

public class nodes {
    public String addNode(int node_Id, double latitude, double longitude, double degree, JSONObject edges, JSONObject tags) {
        return "INSERT INTO \"public\".\"rs_nodes\" (\"node_Id\", \"latitude\", \"longitude\", \"degree\", \"edges\", \"tags\") " +
                "VALUES " +
                "('"+node_Id+"', "+latitude+", "+longitude+", "+degree+", '"+edges+"', '"+tags+"');";
    }
    public String updateNode(int node_Id,  double latitude, double longitude, double degree, JSONObject edges, JSONObject tags) {
        return "UPDATE \"public\".\"rs_nodes\" SET " +
                "\"latitude\" = "+latitude+", " +
                "\"longitude\" = "+longitude+"," +
                "\"degree\" = "+degree+", " +
                "\"edges\" = '"+edges+"', " +
                "\"tags\" = '"+tags+"' " +
                "WHERE \"node_Id\" = '"+node_Id+"';";
    }
    public String deleteNodeById(int node_Id) {
        return "DELETE FROM \"public\".\"rs_nodes\" WHERE \"node_Id\" = '"+node_Id+"';";
    }
    public String getNodeById(int node_Id) {
        return "SELECT * FROM \"public\".\"rs_nodes\" WHERE \"node_Id\" = '"+node_Id+"';";
    }
    public String getAllNodes() {
        return "SELECT * FROM \"public\".\"rs_nodes\";";
    }
}
