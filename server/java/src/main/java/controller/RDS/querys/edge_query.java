package controller.RDS.querys;

import model.OEdge;

public class edge_query {
    static public String addEdge(OEdge e) {
        return "INSERT INTO \"public\".\"rs_edges\" (\"edge_Id\", \"startNodeId\", \"endNodeId\", \"weight\", \"distance\", \"name\", \"highwayType\") " +
                "VALUES " +
                "("+e.getEdge_Id()+", "+e.getStartNode().getOsm_Id()+", "+e.getEndNode().getOsm_Id()+", "+e.getWeight()+", "+e.getDistance()+", '"+e.getName()+"', '"+e.getHighwayType()+"');";
    }
    static public String updateEdge(OEdge e) {
        return "UPDATE \"public\".\"rs_edges\" SET " +
                "\"startNodeId\" = "+e.getStartNode().getOsm_Id()+", " +
                "\"endNodeId\" = "+e.getEndNode().getOsm_Id()+"," +
                " \"weight\" = "+e.getWeight()+", " +
                "\"distance\" = '"+e.getDistance()+"', " +
                "\"name\" = '"+e.getName()+"', " +
                "\"highwayType\" = '"+e.getHighwayType()+"' " +
                "WHERE \"edge_Id\" = '"+e.getEdge_Id()+"';";
    }
    static public String deleteEdgeById(String id) {
        return "DELETE FROM \"public\".\"rs_edges\" WHERE \"edge_Id\" = '"+id+"';";
    }
    static public String deleteEdgeById(OEdge e) {
        return deleteEdgeById(e.getEdge_Id());
    }
    static public String getEdgeById(OEdge e) {
        return "SELECT * FROM \"public\".\"rs_edges\" WHERE \"edge_Id\" = '"+e.getEdge_Id()+"';";
    }
    static public String getAllEdges() {
        return "SELECT * FROM \"public\".\"rs_edges\";";
    }
}
