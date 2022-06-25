import controller.utils.GraphAlgo;
import controller.utils.MapUtils;
import controller.utils.LogHandler;
import model.Node;
import view.MapView;
import crosby.binary.osmosis.OsmosisReader;
import model.RoadMap;
import controller.osm_processing.*;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;

//osmconvert64.exe ariel2.osm > arielpbf.pbf --out-pbf
// add "server/java/data/israel.pbf"; as input in configuration

/**
 * to run with intellij add '.pbf' path file to configuration:
 *      edit configuration -> Name: APP -> Build and run -> Program arguments -> insert: server/java/data/israel.pbf
 */
public final class App{
    private static final long NODE_IN_MAIN_COMPONENT = 2432701015L;
    public static LogHandler logger = new LogHandler();
    private App() {}

    public static void main(String[] args) {
        String pbfFilePath = args.length == 0 ? chooseFile() : args[0];

        boolean defaultBounds = true;

        MapUtils.setBounds(defaultBounds);

        logger.log(Level.INFO, "start parsing map : '" + pbfFilePath+"'");

        CreateMap(pbfFilePath);

        logger.log(Level.INFO,"map is ready. map = " + RoadMap.getInstance());

        MapView.getInstance().show();

        logger.closeHandler();

        System.out.println("final thoughts?!");
        System.exit(0);
    }

    public static void CreateMap(String pathToPBF) {
        InputStream inputStream;

        try {
            inputStream = new FileInputStream(pathToPBF);

            // read from osm pbf file:
//            Reader reader = new Reader(new GeoLocation(32.18276629498098, 35.05028293864246), new GeoLocation(31.943290520866952, 34.70846861419407));
            Reader reader = new Reader();
            OsmosisReader osmosisReader = new OsmosisReader(inputStream);
            osmosisReader.setSink(reader);

            // initial parsing of the .pbf file:
            osmosisReader.run();

            // secondary parsing of ways/creation of edges:
            Parser parser = new Parser();
            parser.parseMapWays(reader.getWays());//reader.getWays(), reader.getMapObjects());

            // get riders & drivers
            GraphAlgo.removeNodesThatNotConnectedTo(RoadMap.getInstance().getNode(NODE_IN_MAIN_COMPONENT));

        } catch (FileNotFoundException e) {
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


