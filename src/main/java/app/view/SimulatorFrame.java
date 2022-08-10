package app.view;

import app.controller.Simulator;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;
import utils.SimulatorLatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static utils.Utils.FORMAT;

public class SimulatorFrame extends JFrame{
    private JPanel mainPanel;
    private JSlider speedSlider;
    private JLabel speedLabel;
    private JButton okButton;
    private JPanel graphPanel;
    private JButton pauseButton;
    private JLabel clockLabel;
    private JPanel speedControlPanel;
    private JPanel clockPanel;
    private SimulatorLatch latch = SimulatorLatch.INSTANCE;

    protected Viewer viewer;
    private ViewerPipe pipeIn;

    public SimulatorFrame() {
        setContentPane(mainPanel);
        setTitle("TLV SharedRide Simulator");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(100, 100, (int) dim.getWidth(), (int) dim.getHeight());
        setLocationRelativeTo(null);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(speedSlider.getValue() == 0){
                    speedSlider.setValue(1);
                }
                System.out.println("new speed " + speedSlider.getValue());
                updateSpeed();
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
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        viewer = new Viewer(MapView.displayGraph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        graphPanel = viewer.addDefaultView(false);
        viewer.disableAutoLayout();
        pipeIn = viewer.newViewerPipe();

    }

    public void updateFrame(){
        pipeIn.pump();
        clockLabel.setText(FORMAT(Simulator.INSTANCE.time()));
//        super.repaint();
    }

//    public static void main(String[] args) {
//        SimulatorFrame f = new SimulatorFrame();
//        f.setContentPane(f.mainPanel);
//        f.setTitle("TLV SharedRide Simulator");
//        f.setSize(300,400);
//        f.setVisible(true);
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
}
