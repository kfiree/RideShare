package controller.rds.querys;

import model.Edge;

public class edge_query {
    static public String addEdge(Edge e) {
        return "INSERT INTO \"public\".\"rs_edges\" (\"edge_Id\", \"startNodeId\", \"endNodeId\", \"weight\", \"distance\", \"highwayType\") " +
                "VALUES " +
                "('"+e.getId()+"', '"+e.getNode1().getId()+"', '"+e.getNode2().getId()+"', "+e.getWeight()+", "+e.getDistance()+", '"+e.getHighwayType()+"');";
    }
    static public String updateEdge(Edge e) {
        return "UPDATE \"public\".\"rs_edges\" SET " +
                "\"startNodeId\" = "+e.getNode1().getOsmID()+", " +
                "\"endNodeId\" = "+e.getNode2().getOsmID()+"," +
                " \"weight\" = "+e.getWeight()+", " +
                "\"distance\" = '"+e.getDistance()+"', " +
//                "\"name\" = '"+e.getName()+"', " +
                "\"highwayType\" = '"+e.getHighwayType()+"' " +
                "WHERE \"edge_Id\" = '"+e.getId()+"';";
    }
    static public String deleteEdgeById(String id) {
        return "DELETE FROM \"public\".\"rs_edges\" WHERE \"edge_Id\" = '"+id+"';";
    }
    static public String deleteEdgeById(Edge e) {
        return deleteEdgeById(e.getId());
    }
    static public String getEdgeById(Edge e) {
        return "SELECT * FROM \"public\".\"rs_edges\" WHERE \"edge_Id\" = '"+e.getId()+"';";
    }
    static public String getAllEdges() {
        return "SELECT * FROM \"public\".\"rs_edges\";";
    }
}
