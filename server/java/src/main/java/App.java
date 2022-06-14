import controller.utils.GraphAlgo;
import view.MapView;
import crosby.binary.osmosis.OsmosisReader;
import model.OGraph;
import controller.osmProcessing.*;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

//osmconvert64.exe ariel2.osm > arielpbf.pbf --out-pbf

public class App implements Runnable{
    private static Map<Long, Double[]> Riders = new HashMap<>();
    private static List<Object> pathNodesID = new ArrayList<>();
    private static Thread client;

    public static void main(String[] args) {
//        String filepath = ExtractMap.chooseFile();
//        CreateGraph(filepath);
        CreateGraph("server/java/data/israel.pbf");

        System.out.println("graph is ready.\n" + OGraph.getInstance());

        MapView.getInstance().show();

//        startThread();

        System.exit(0);
    }

    public static void startThread(){
//        client = new Thread(new App());
//        client.start();
//
//        System.out.println("thread id is " + client.getId());
    }

    public static OGraph CreateGraph(String pathToPBF) {
        InputStream inputStream;

        try {
            inputStream = new FileInputStream(pathToPBF);

            // read from osm pbf file:
            Reader custom = new Reader();
            OsmosisReader reader = new OsmosisReader(inputStream);

            reader.setSink(custom);

            // initial parsing of the .pbf file:
            reader.run();

            // secondary parsing of ways/creation of edges:
            Parser.parseMapWays(custom.ways, custom.MapObjects);

            // get riders & drivers
            OGraph graph = OGraph.getInstance();

            GraphAlgo.removeNodesThatNotConnectedTo(graph.getNode(2432701015l));

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(new JFrame(), "File not found!", "ERROR",
                    JOptionPane.ERROR_MESSAGE);

            System.exit(0);
        }
        return OGraph.getInstance();
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
        return null;
    }

    @Override
    public void run() {
        System.out.println("start running");
        MapView map = MapView.getInstance();

        map.show();

        MapView.getInstance().show();
        long sleepTime = 100;

        int time = 10000;

        while(time > 0){//server.isRunning()) {
//          TODO  synchronized (Thread.currentThread())  ??
            time--;
            if(time % 100 == 0){
                System.out.println("time left - "+ time + " seconds.");
            }
//            server.getUpdates();

            try {
//                map.repaint();
                Thread.sleep(sleepTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
// TODO set & get graph user drive | sub graph | match rider and drivers | lock drive