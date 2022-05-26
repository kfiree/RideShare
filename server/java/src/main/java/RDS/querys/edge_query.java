package RDS.querys;

import RDS.models.Edge;

public class edge_query {
    static public String addEdge(Edge e) {
        return "INSERT INTO \"public\".\"rs_edges\" (\"edge_Id\", \"startNodeId\", \"endNodeId\", \"weight\", \"distance\", \"name\", \"highwayType\") " +
                "VALUES " +
                "("+e.getEdge_Id()+", "+e.getStartNodeId()+", "+e.getEndNodeId()+", "+e.getWeight()+", "+e.getDistance()+", '"+e.getName()+"', '"+e.getHighwayType()+"');";
    }
    static public String updateEdge(Edge e) {
        return "UPDATE \"public\".\"rs_edges\" SET " +
                "\"startNodeId\" = "+e.getStartNodeId()+", " +
                "\"endNodeId\" = "+e.getEndNodeId()+"," +
                " \"weight\" = "+e.getWeight()+", " +
                "\"distance\" = '"+e.getDistance()+"', " +
                "\"name\" = '"+e.getName()+"', " +
                "\"highwayType\" = '"+e.getHighwayType()+"' " +
                "WHERE \"edge_Id\" = '"+e.getEdge_Id()+"';";
    }
    static public String deleteEdgeById(Edge e) {
        return "DELETE FROM \"public\".\"rs_edges\" WHERE \"edge_Id\" = '"+e.getEdge_Id()+"';";
    }
    static public String getEdgeById(Edge e) {
        return "SELECT * FROM \"public\".\"rs_edges\" WHERE \"edge_Id\" = '"+e.getEdge_Id()+"';";
    }
    static public String getAllEdges() {
        return "SELECT * FROM \"public\".\"rs_edges\";";
    }
}
