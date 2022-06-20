import controller.utils.MapUtils;
import view.MapView;
import crosby.binary.osmosis.OsmosisReader;
import model.RegionMap;
import controller.osm_processing.*;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

//osmconvert64.exe ariel2.osm > arielpbf.pbf --out-pbf
// add "server/java/data/israel.pbf"; as input in configuration
public final class App{

    private App() {}

    public static void main(String[] args) {
        String pbfFilePath = args.length == 0 ? chooseFile() : args[0];

        System.out.println("start parsing map : '" + pbfFilePath+"'");

        MapUtils.setBounds(true);

        CreateMap(pbfFilePath);

        System.out.println("map is ready.\n" + RegionMap.getInstance());

        MapView.getInstance().show();

        System.exit(0);
    }

    public static RegionMap CreateMap(String pathToPBF) {
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
            Parser.parseMapWays(reader.getWays());//reader.getWays(), reader.getMapObjects());

            // get riders & drivers
//            GraphAlgo.removeNodesThatNotConnectedTo(map.getNode(2432701015l));

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(new JFrame(), "File not found!", "ERROR",
                    JOptionPane.ERROR_MESSAGE);

            System.exit(0);
        }
        return RegionMap.getInstance();
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
}


