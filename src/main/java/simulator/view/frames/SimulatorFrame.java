package simulator.view.frames;

import simulator.controller.EventManager;
import simulator.controller.MatchMaker;
import simulator.controller.Simulator;
import simulator.controller.SimulatorThread;
import road_map.model.graph.RoadMap;
import simulator.model.users.Driver;
import simulator.model.users.Passenger;
import simulator.model.users.User;
import simulator.model.users.UserMap;
import simulator.view.MapView;
import simulator.view.utils.MapMouseManager;
import simulator.view.utils.MapShortcutManager;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;
import utils.DS.Latch;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static simulator.view.utils.Style.LIGHT_GREEN;
import static simulator.view.utils.Style.LIGHT_RED;
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
    private JPanel driversTabs;
    private JTable driversTable;
    private JLabel driversTitle;

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
        tabs.addChangeListener(e -> {
            if(e.getSource() instanceof JTabbedPane tabbedPane){
                int index = tabbedPane.getSelectedIndex();
                System.out.println(index);

                DefaultTableModel threadsModel = (DefaultTableModel) threadsTable.getModel();

                int rowCount = threadsModel.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    threadsModel.removeRow(i);
                }

                SimulatorThread.threads.forEach(t->{
                    if(t instanceof Simulator thread){
                        threadsModel.addRow(new Object[]{"Simulator" , FORMAT(thread.time())});
                    }else if(t instanceof EventManager thread){
                        threadsModel.addRow(new Object[]{"Event Manager" , FORMAT(thread.getTime())});
                    }else if(t instanceof MatchMaker thread){
                        threadsModel.addRow(new Object[]{"Match Maker" , FORMAT(thread.getTime())});
                    }else if(t instanceof Driver drive){
                        threadsModel.addRow(new Object[]{"Drive"+ drive.getId()+", current location: " + drive.getLocation().getId()+
                                ", passengers: "  + drive.getPassengers().stream().map(User::getId).toList()+ ".",
                                FORMAT(drive.getTime())});
                    }
                } );


                DefaultTableModel driversModel = (DefaultTableModel) driversTable.getModel();
                rowCount = driversModel.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    driversModel.removeRow(i);
                }

                UserMap.INSTANCE.getLiveDrives().forEach(d->{
                    List<Integer> passengers = d.getPassengers().stream().map(User::getId).toList();
                    List<Integer> onBoard = d.getPassengers().stream().filter(p -> p.state() == Passenger.State.Picked).map(User::getId).toList();
                    String nextStop = d.getPassengers().isEmpty() ?
                            "final stop.":
                            "passenger " + d.getNextPassenger().getId() + "'s " + (d.getNextPassenger().state() == Passenger.State.Picked? "drop off.": "pick up.");

                    driversModel.addRow(new Object[]{d.getId(), passengers, onBoard, nextStop});
                });

            }
        });
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
        simulatorTitle = new JLabel("--- "+ RoadMap.INSTANCE.getName() +" SharedRide Simulator ---");

        drawGraph();
        drawTables();
    }

    private void drawTables(){
        /** thread table */
        threadsTable = new JTable(new DefaultTableModel());

        DefaultTableModel threadsModel = (DefaultTableModel) threadsTable.getModel();
        threadsModel.addColumn("Thread");
        threadsModel.addColumn("Relative time");


        SimulatorThread.threads.forEach(t->{
            if(t instanceof Simulator thread){
                threadsModel.addRow(new Object[]{"Simulator" , FORMAT(thread.time())});
            }else if(t instanceof EventManager thread){
                threadsModel.addRow(new Object[]{"Event Manager" , FORMAT(thread.getTime())});
            }else if(t instanceof MatchMaker thread) {
                threadsModel.addRow(new Object[]{"thread", FORMAT(thread.getTime())});
            }
        });

        /** driver table */
        driversTable = new JTable(new DefaultTableModel());
        DefaultTableModel driversModel = (DefaultTableModel) driversTable.getModel();
        driversModel.addColumn("Driver Id");
        driversModel.addColumn("Passengers");
        driversModel.addColumn("On Board");
        driversModel.addColumn("Next stop");

        driversModel.addRow(new Object[]{"0000", "0", "0", "Nan"});
    }

    private void drawGraph(){
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        viewer = new Viewer(MapView.INSTANCE.getDisplayGraph(), Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        graphPanel = viewer.addDefaultView(false);

        viewer.disableAutoLayout();
        pipeIn = viewer.newViewerPipe();

        viewer.getDefaultView().setShortcutManager(new MapShortcutManager());
        viewer.getDefaultView().setMouseManager(new MapMouseManager());
    }

    public void updateFrame(){
        pipeIn.pump();
        clockLabel.setText(FORMAT(Simulator.INSTANCE.time()));
    }
}
