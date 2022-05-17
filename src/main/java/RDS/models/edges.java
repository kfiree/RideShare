package RDS.models;

public class edges {
    public String addEdge(int edge_Id,  double startNodeId,double endNodeId, double weight, double distance, String name, String highwayType) {
        return "INSERT INTO \"public\".\"rs_edges\" (\"edge_Id\", \"startNodeId\", \"endNodeId\", \"weight\", \"distance\", \"name\", \"highwayType\") " +
                "VALUES " +
                "("+edge_Id+", "+startNodeId+", "+endNodeId+", "+weight+", "+distance+", '"+name+"', '"+highwayType+"');";
    }
    public String updateEdge(int edge_Id,  double startNodeId,double endNodeId, double weight, double distance, String name, String highwayType) {
        return "UPDATE \"public\".\"rs_edges\" SET " +
                "\"startNodeId\" = "+startNodeId+", " +
                "\"endNodeId\" = "+endNodeId+"," +
                " \"weight\" = "+weight+", " +
                "\"distance\" = '"+distance+"', " +
                "\"name\" = '"+name+"', " +
                "\"highwayType\" = '"+highwayType+"' " +
                "WHERE \"edge_Id\" = '"+edge_Id+"';";
    }
    public String deleteEdgeById(int edge_Id) {
        return "DELETE FROM \"public\".\"rs_edges\" WHERE \"edge_Id\" = '"+edge_Id+"';";
    }
    public String getEdgeById(int edge_Id) {
        return "SELECT * FROM \"public\".\"rs_edges\" WHERE \"edge_Id\" = '"+edge_Id+"';";
    }
    public String getAllEdges() {
        return "SELECT * FROM \"public\".\"rs_edges\";";
    }
}
