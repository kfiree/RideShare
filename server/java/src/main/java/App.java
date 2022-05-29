import View.MapView;
import crosby.binary.osmosis.OsmosisReader;
import osmProcessing.*;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
//32.101267 35.2040791

public class App {
    private static Map<Long, Double[]> Riders = new HashMap<>();
    private static List<Object> pathNodesID = new ArrayList<>();


    public static void main(String[] args) {
//        String filepath = ExtractMap.chooseFile();
//        CreateGraph(filepath);
        CreateGraph("/Users/amitha/Desktop/RideShare/server/java/data/arielpbf.pbf");
    }

    /**
     * TODO get riders from database SQL - Motti
     */
    private static void addRiders(){
        Riders.put(-1L, new Double[]{32.10590091133335, 35.192002782079705});
        Riders.put(-2L, new Double[]{32.10569527212209, 35.20159203268953});
        Riders.put(-3L, new Double[]{32.100656966794055, 35.20341277647622});
        Riders.put(-4L, new Double[]{32.10551550801045, 35.1700032533693});
    }

    /**
     * TODO get driver's path from database SQL - Motti
     */
    private static List<Object> setDriversPath(){
        pathNodesID.add(432349397l);
        pathNodesID.add(5329510675l);
        pathNodesID.add(985633358l);
        pathNodesID.add(257392128l);
        pathNodesID.add(5224948678l);
        pathNodesID.add(5177072076l);
        return pathNodesID;
    }

    static Point2D.Double topRight = new Point2D.Double(32.10070229573369, 34.84550004660088), bottomLeft = new Point2D.Double(32.10070229573369, 34.84550004660088);

    public static void CreateGraph(String pathToPBF) {
        InputStream inputStream;

        try {
            inputStream = new FileInputStream(pathToPBF);

            // read from osm pbf file:
            Point2D.Double mapBounds = new Point2D.Double(31.99721786061472, 34.72968352639412);

            Reader custom = new Reader();
            custom.setMapBounds(topRight, bottomLeft);
            OsmosisReader reader = new OsmosisReader(inputStream);

//            custom.removeTertiary();

            // bottom left  31.999919131519913, 34.72920639507035
            // top right 32.10070229573369, 34.84550004660088



            reader.setSink(custom);

            // initial parsing of the .pbf file:
            reader.run();


            // secondary parsing of ways/creation of edges:
            Parser.parseMapWays(custom.ways, custom.MapObjects);

            // get riders
            addRiders();

            // get driver path
            setDriversPath();

            GraphUtils.getInstance().addPath(pathNodesID);
            GraphUtils.getInstance().addLabeledPath(pathNodesID, "Passenger");
            GraphUtils.getInstance().setRiders(Riders);
            OGraph graph = OGraph.getInstance();


            /**
             * flow with data base
             */


            /** full graph */

            // set
//            addGraphToDB addGraphToDB = new addGraphToDB(graph);
//            addGraphToDB.addToDB();

            //get


            // set & get user

            // set & get drive


            //  set & get sub graph

            // match rider and drivers

            // lock drive

//            //TODO add data to map
            List<ONode> path = GraphUtils.getInstance().AStar(graph.getNode(Long.parseLong("5341771424")), graph.getNode(Long.parseLong("5216093401")));
            List<Object> nodeIDs = new ArrayList<>();
            path.forEach(node -> nodeIDs.add(node.getOsm_Id()));
            GraphUtils.getInstance().addPath(nodeIDs);
            GraphUtils.getInstance().addLabeledPath(nodeIDs, "Driver");
            MapView.getInstance().run();
//
//            //TODO add algorithms here
            System.out.println(graph.toString());

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(new JFrame(), "File not found!", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            // exit after error //
            System.exit(0);
        }
    }
}
