package app.view.frames;

import app.controller.simulator.EventManager;
import app.controller.simulator.MatchMaker;
import app.controller.simulator.Simulator;
import app.controller.simulator.SimulatorThread;
import app.model.graph.RoadMap;
import app.model.users.Driver;
import app.view.MapView;
import app.view.utils.MapMouseManager;
import app.view.utils.MapShortcutManager;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;
import utils.DS.Latch;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;

import static app.view.utils.Style.LIGHT_GREEN;
import static app.view.utils.Style.LIGHT_RED;
import static utils.Utils.FORMAT;

public class SimulatorFrame extends JFrame{
    private Latch latch = Simulator.INSTANCE.getLatch();
    protected JPanel graphPanel;
    private JPanel mainTab;
    private JSlider speedSlider;
    private JLabel speedLabel;
    private JButton speedButton;
    private JButton pauseButton;
    private JLabel clockLabel;
    private JPanel speedControlPanel;
    private JPanel clockPanel;
    private JPanel threadsTab;
    private JTable threadsTable;
    private JLabel threadsTitle;
    private JLabel simulatorTitle;
    private JTabbedPane tabs;
    private JPanel pausePanel;

    protected Viewer viewer;
    private ViewerPipe pipeIn;

    public SimulatorFrame() {
        setContentPane(tabs);
        setTitle("TLV SharedRide Simulator");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(100, 100, (int) dim.getWidth(), (int) dim.getHeight()-50);
        setLocationRelativeTo(null);

        setListeners();

    }

    private void setListeners(){
        speedButton.addActionListener(e -> {
            SimulatorThread.showThreadsData();
            if(speedSlider.getValue() == 0){
                speedSlider.setValue(1);
            }
            System.out.println("new speed " + speedSlider.getValue());
            updateSpeed();
        });
        pauseButton.addActionListener(e -> {
            if(Latch.lock){
                latch.resume();
                pauseButton.setText("Pause");
                pauseButton.setBackground(LIGHT_RED);
            }else{
                latch.lock();
                pauseButton.setText("Continue");
                pauseButton.setBackground(LIGHT_GREEN);
            }
        });
    }

    private void updateSpeed(){
        Simulator.INSTANCE.setSpeed(speedSlider.getValue());
        speedLabel.setText("Speed : " + speedSlider.getValue());
    }

    private void createUIComponents() {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        viewer = new Viewer(MapView.INSTANCE.getDisplayGraph(), Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        graphPanel = viewer.addDefaultView(false);
//        graphPanel = viewer.addView("RoadMapView", new J2DGraphRenderer());

        viewer.disableAutoLayout();
        pipeIn = viewer.newViewerPipe();

//        graphPanel.addMouseListener(new MapMouseManager());
        viewer.getDefaultView().setShortcutManager(new MapShortcutManager());
        viewer.getDefaultView().setMouseManager(new MapMouseManager());

        simulatorTitle = new JLabel("--- "+ RoadMap.INSTANCE.getName() +" SharedRide Simulator ---");
//        simulatorTitle.setText("--- "+ RoadMap.INSTANCE.getName() +" SharedRide Simulator ---");

        threadsTable = new JTable(new DefaultTableModel());

        DefaultTableModel model = (DefaultTableModel) threadsTable.getModel();
        model.addColumn("Thread");
        model.addColumn("Relative time");
        model.addColumn("Start Time");

        for (int i = 0; i < 4; i++) {
            model.addRow(new Object[]{"thread" , FORMAT(new Date()), "choose time"});
        }

        SimulatorThread.threads.forEach(t->{
            model.addRow(TimeSyncType(t));
        });
    }

    private Object[] TimeSyncType(SimulatorThread t){
        if(t instanceof Simulator){
            return new Object[]{"Simulator" , FORMAT(t.getTime()), "choose time"};
        }else if(t instanceof MatchMaker){
            return new Object[]{"MatchMaker" , FORMAT(t.getTime()), "choose time"};
        }else if(t instanceof EventManager){
            return new Object[]{"EventManager" , FORMAT(t.getTime()), "choose time"};
        }else if(t instanceof Driver){
            return new Object[]{"EventManager" , FORMAT(t.getTime()), "choose time"};
        }
        return new Object[0];
    }

    public void updateFrame(){
        pipeIn.pump();
        clockLabel.setText(FORMAT(Simulator.INSTANCE.time()));
    }
}
