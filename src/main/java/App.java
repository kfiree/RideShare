import crosby.binary.osmosis.OsmosisReader;
import osmProcessing.OGraph;
import osmProcessing.Reader;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
//32.101267 35.2040791

public class App {

    public static void main(String[] args) {
        String filepath = ExtractMap.chooseFile();
        CreateGraph(filepath);
//        CreateGraph("data/maps/ariel.pbf");
//
    }

    public static void CreateGraph(String pathToPBF) {
        InputStream inputStream;

        try {
            inputStream = new FileInputStream(pathToPBF);

            // read from osm pbf file:
            Reader custom = new Reader();
            OsmosisReader reader = new OsmosisReader(inputStream);
            reader.setSink(custom);

            // initial parsing of the .pbf file:
            reader.run();

            // create graph:
            OGraph OGraph = OGraph.getInstance();

            // secondary parsing of ways/creation of edges:
            OGraph.parseMapWays(custom.ways, custom.MapObjects);

            //TODO add data to map

            //TODO add algorithms here
            System.out.println(OGraph.toString());

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(new JFrame(), "File not found!", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            // exit after error //
            System.exit(0);
        }
    }
}
