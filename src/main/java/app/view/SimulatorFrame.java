package app.view;

import app.controller.EventManager;
import app.controller.MatchMaker;
import app.controller.Simulator;
import app.controller.TimeSync;
import app.model.users.Driver;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;
import utils.SimulatorLatch;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import static utils.Utils.FORMAT;

public class SimulatorFrame extends JFrame{
    private JPanel mainTab;
    private JSlider speedSlider;
    private JLabel speedLabel;
    private JButton speedButton;
    private JPanel graphPanel;
    private JButton pauseButton;
    private JLabel clockLabel;
    private JPanel speedControlPanel;
    private JPanel clockPanel;
    private JPanel threadsTab;
    private JTable threadsTable;
    private JLabel threadsTitle;
    private JLabel simulatorTitle;
    private JTabbedPane tabs;
    private SimulatorLatch latch = SimulatorLatch.INSTANCE;

    protected Viewer viewer;
    private ViewerPipe pipeIn;

    public SimulatorFrame() {
        setContentPane(mainTab);
        setTitle("TLV SharedRide Simulator");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(100, 100, (int) dim.getWidth(), (int) dim.getHeight()-50);
        setLocationRelativeTo(null);

        speedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TimeSync.showThreadsData();
                if(speedSlider.getValue() == 0){
                    speedSlider.setValue(1);
                }
                System.out.println("new speed " + speedSlider.getValue());
                updateSpeed();
                Simulator.INSTANCE.setSpeed(speedSlider.getValue());
            }
        });
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if(latch.isPause()){
                    latch.unpause();
                    pauseButton.setBackground(Color.green);
                }else{
                    latch.pause();
                    pauseButton.setBackground(Color.red);
                }
            }
        });
    }

    private void updateSpeed(){
        speedLabel.setText("Speed : " + speedSlider.getValue());
    }

    private void createUIComponents() {
//        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        viewer = new Viewer(MapView.displayGraph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        graphPanel = viewer.addDefaultView(false);
        viewer.disableAutoLayout();
        pipeIn = viewer.newViewerPipe();

        threadsTable = new JTable(new DefaultTableModel());

        DefaultTableModel model = (DefaultTableModel) threadsTable.getModel();
        model.addColumn("Thread");
        model.addColumn("Relative time");
        model.addColumn("Start Time");

        for (int i = 0; i < 4; i++) {
            model.addRow(new Object[]{"thread" , FORMAT(new Date()), "start time"});
        }

        TimeSync.threads.forEach(t->{
            model.addRow(TimeSyncType(t));
        });

    }

    private Object[] TimeSyncType(TimeSync t){
        if(t instanceof Simulator){
            return new Object[]{"Simulator" , FORMAT(t.getTime()), "start time"};
        }else if(t instanceof MatchMaker){
            return new Object[]{"MatchMaker" , FORMAT(t.getTime()), "start time"};
        }else if(t instanceof EventManager){
            return new Object[]{"EventManager" , FORMAT(t.getTime()), "start time"};
        }else if(t instanceof Driver){
            return new Object[]{"EventManager" , FORMAT(t.getTime()), "start time"};
        }
        return new Object[0];
    }

    public void updateFrame(){
        pipeIn.pump();
        clockLabel.setText(FORMAT(Simulator.INSTANCE.time()));
//        super.repaint();
    }

//    List rowColours = (List) Arrays.asList(
//            Color.RED,
//            Color.GREEN,
//            Color.CYAN
//    );
//
//    public void setRowColour(int row, Color c) {
//        rowColours.set(row, c);
//        fireTableRowsUpdated(row, row);
//    }


//    public static void main(String[] args) {
//        SimulatorFrame f = new SimulatorFrame();
//        f.setContentPane(f.mainPanel);
//        f.setTitle("TLV SharedRide Simulator");
//        f.setSize(300,400);
//        f.setVisible(true);
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
}
