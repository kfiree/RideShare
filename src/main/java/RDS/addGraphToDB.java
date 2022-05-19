package RDS;

import RDS.models.*;
import RDS.querys.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import osmProcessing.OGraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import static RDS.checkQuerys.connection;

public class addGraphToDB {

    private OGraph graph;
    nodes nodes = new nodes();
    edges edges = new edges();
    public addGraphToDB(OGraph graph) {
        this.graph = graph;
    }

    public OGraph getGraph() {
        return graph;
    }

    public void setGraph(OGraph graph) {
        this.graph = graph;
    }



    public void addToDB() {
        this.graph.getNodes().forEach((key,val) -> {
            //nodes
            JSONObject edges = new JSONObject();
            val.getEdges().forEach((edge) -> {
                int index = 0;
                Edge e = new Edge(edge.getEdgeId().intValue(), edge.getStartNode().getID(), edge.getEndNode().getID(), edge.getWeight(), edge.getWeight(), edge.getName(), edge.getHighwayType());
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
            Node n = new Node(val.getID(), val.getLatitude(), val.getLongitude(), val.getDegree(), edges, tags);
            System.out.println("addNode -> " + checkQuerys.addToDB.addToDB(connection, nodes.addNode(n)));
        });

        this.graph.getEdges().forEach((key,val) -> {
            //edges
            Edge e = new Edge(val.getEdgeId().intValue(), val.getStartNode().getID(), val.getEndNode().getID(), val.getWeight(), val.getWeight(), val.getName(), val.getHighwayType());
            System.out.println("addEdge -> " + checkQuerys.addToDB.addToDB(connection, edges.addEdge(e)));
        });
    }
    @Override
    public String toString() {
        return "addGraphToDB{" +
                "graph=" + graph +
                '}';
    }
}
