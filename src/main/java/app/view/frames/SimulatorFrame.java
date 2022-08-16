package app.view.frames;

import app.controller.simulator.EventManager;
import app.controller.simulator.MatchMaker;
import app.controller.simulator.Simulator;
import app.controller.simulator.SimulatorThread;
import app.model.graph.RoadMap;
import app.model.users.Driver;
import app.model.users.User;
import app.view.MapView;
import app.view.utils.MapMouseManager;
import app.view.utils.MapShortcutManager;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;
import utils.DS.Latch;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        tabs.addChangeListener(e -> {
            if(e.getSource() instanceof JTabbedPane tabbedPane){
                int index = tabbedPane.getSelectedIndex();
                System.out.println(index);

                DefaultTableModel model = (DefaultTableModel) threadsTable.getModel();

                int rowCount = model.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    model.removeRow(i);
                }

                SimulatorThread.threads.forEach(t->{
                    if(t instanceof Simulator thread){
                        model.addRow(new Object[]{"Simulator" , FORMAT(thread.time())});
                    }else if(t instanceof EventManager thread){
                        model.addRow(new Object[]{"Event Manager" , FORMAT(thread.getTime())});
                    }else if(t instanceof MatchMaker thread){
                        model.addRow(new Object[]{"Match Maker" , FORMAT(thread.getTime())});
                    }else if(t instanceof Driver drive){
                        model.addRow(new Object[]{"Drive"+ drive.getId()+", current location: " + drive.getLocation().getId()+
                                ", passengers: "  + drive.getPassengers().stream().map(User::getId).toList()+ ".",
                                FORMAT(drive.getTime())});
                    }
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
        drawTable();
    }

    private void drawTable(){
        threadsTable = new JTable(new DefaultTableModel());

        DefaultTableModel model = (DefaultTableModel) threadsTable.getModel();
        model.addColumn("Thread");
        model.addColumn("Relative time");


        SimulatorThread.threads.forEach(t->{
            if(t instanceof Simulator thread){
                model.addRow(new Object[]{"Simulator" , FORMAT(thread.time())});
            }else if(t instanceof EventManager thread){
                model.addRow(new Object[]{"Event Manager" , FORMAT(thread.getTime())});
            }else if(t instanceof MatchMaker thread) {
                model.addRow(new Object[]{"thread", FORMAT(thread.getTime())});
            }
        });
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
