package controller.RDS;

import controller.RDS.models.*;
import controller.RDS.querys.*;
import org.json.simple.JSONObject;
import model.OEdge;
import model.OGraph;

import static controller.RDS.checkQuerys.connection;

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


    static public void addToDB(OGraph graph) {
//        uploadNodes(graph);
        uploadEdges(graph);
    }
    public static void uploadNodes(OGraph graph){
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


    }

    public static void uploadEdges(OGraph graph){
        graph.getEdges().forEach(edge -> {
            //edges
//            OEdge e = new OEdge(edge.getStartNode().getOsm_Id(), edge.getEndNode().getOsm_Id(), edge.getWeight(), edge.getWeight(), edge.getName(), edge.getHighwayType());
            System.out.println(edge_query.addEdge(edge));
            System.out.println("addEdge -> " + checkQuerys.addToDB.addToDB(connection, edge_query.addEdge(edge)));
        });
    }


    @Override
    public String toString() {
        return "addGraphToDB{" +
                "graph=" + OGraph.getInstance() +
                '}';
    }
}