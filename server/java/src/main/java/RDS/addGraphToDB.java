package RDS;

import RDS.models.*;
import RDS.querys.*;
import org.json.simple.JSONObject;
import osmProcessing.OEdge;
import osmProcessing.OGraph;

import static RDS.checkQuerys.connection;

public class addGraphToDB {

//    nodes nodes = new nodes();
//    edges edges = new edges();
//    public addGraphToDB(OGraph graph) {
//        this.graph = graph;
//    }

//    public OGraph getGraph() {
//        return graph;
//    }
//
//    public void setGraph(OGraph graph) {
//        this.graph = graph;
//    }



    static public void addToDB() {
        OGraph graph = OGraph.getInstance();

        graph.getNodes().forEach((key,val) -> {
            //nodes
            JSONObject edges = new JSONObject();
            val.getEdges().forEach((edge) -> {
                int index = 0;
//                Edge e = new Edge(edge.getEdgeId().intValue(), edge.getStartNode().getID(), edge.getEndNode().getID(), edge.getWeight(), edge.getWeight(), edge.getName(), edge.getHighwayType());
                OEdge e = new OEdge(edge.getStartNode().getOsm_Id(), edge.getEndNode().getOsm_Id(), edge.getWeight(), edge.getWeight(), edge.getName(), edge.getHighwayType());
                edges.put("" + (++index), "" + e.getEdge_Id());
            });
            JSONObject tags = new JSONObject();
            val.getTags().forEach((keyTag,valTag) -> {
                keyTag =  keyTag.replace("\"", "");
                keyTag =  keyTag.replace("\'", "");
                valTag =  valTag.replace("\"", "");
                valTag =  valTag.replace("\'", "");
                tags.put(keyTag , valTag);
            });
            Node n = new Node(val.getOsm_Id(), val.getLatitude(), val.getLongitude(), val.getDegree(), edges, tags);
            System.out.println("addNode -> " + checkQuerys.addToDB.addToDB(connection, node_query.addNode(n)));
        });

        graph.getEdges().forEach((key,val) -> {
            //edges
            OEdge e = new OEdge(val.getStartNode().getOsm_Id(), val.getEndNode().getOsm_Id(), val.getWeight(), val.getWeight(), val.getName(), val.getHighwayType());
            System.out.println("addEdge -> " + checkQuerys.addToDB.addToDB(connection, edge_query.addEdge(e)));
        });
    }
    @Override
    public String toString() {
        return "addGraphToDB{" +
                "graph=" + OGraph.getInstance() +
                '}';
    }
}
