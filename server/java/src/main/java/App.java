import controller.utils.GraphAlgo;
import controller.utils.MapUtils;
import view.MapView;
import crosby.binary.osmosis.OsmosisReader;
import model.RoadMap;
import controller.osm_processing.*;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import static controller.utils.LogHandler.closeLogHandlers;
import static controller.utils.LogHandler.LOGGER;

/**
 * TODO - make methods and datastructures thread safe.
 *      - use only int id of classes
 *      - add assertion @
 *
 *      to run with intellij add '.pbf' path file to configuration:
 *          edit configuration -> Name: APP -> Build and run -> Program arguments -> insert: server/java/data/israel.pbf
 *
 *
 *      to convert files:
 *          osmconvert64.exe ariel2.osm > arielpbf.pbf --out-pbf
 *
 *
 */
public final class App{
    private static final long NODE_IN_MAIN_COMPONENT = 2432701015L;
    private static final double SIMULATOR_SPEED = 10;
    private App() {}

    public static void main(String[] args) {
        String pbfFilePath = args.length == 0 ? chooseFile() : args[0];
        LOGGER.finest("Let's GO!!");

        boolean defaultBounds = true;

        MapUtils.setBounds(defaultBounds);

        LOGGER.info( "Start parsing main map.");// : '" + pbfFilePath+"'");

        CreateMap(pbfFilePath);

        LOGGER.info("Map is ready. Map = " + RoadMap.getInstance());

        MapView.getInstance().show(SIMULATOR_SPEED);

        closeLogHandlers();

        System.exit(0);
    }

    public static void CreateMap(String pathToPBF) {
        InputStream inputStream;

        try {
            inputStream = new FileInputStream(pathToPBF);

            // read from osm pbf file:
            Reader reader = new Reader();
            OsmosisReader osmosisReader = new OsmosisReader(inputStream);
            osmosisReader.setSink(reader);

            // initial parsing of the .pbf file:
            osmosisReader.run();

            // secondary parsing of ways/creation of edges:
            Parser parser = new Parser();
            parser.parseMapWays(reader.getWays());

            // get riders & drivers
            GraphAlgo.removeNodesThatNotConnectedTo(RoadMap.getInstance().getNode(NODE_IN_MAIN_COMPONENT));

        } catch (FileNotFoundException e) {
            LOGGER.severe("File not found!, "+e.getMessage());
            JOptionPane.showMessageDialog(new JFrame(), "File not found!", "ERROR",
                    JOptionPane.ERROR_MESSAGE);

            System.exit(0);
        }
    }

    private static String chooseFile() {
        // JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        JFileChooser jfc = new JFileChooser("server/java/data");
        jfc.setDialogTitle("Select .osm.pbf file to read");
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        return "Not A Valid Path";
    }


    // TODO set & get map user drive | sub map | match rider and drivers | lock drive
    // TODO add loader
}


