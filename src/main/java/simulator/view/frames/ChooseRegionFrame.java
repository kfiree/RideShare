package simulator.view.frames;

import road_map.model.graph.RoadMap;
import road_map.RoadMapHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ChooseRegionFrame extends JDialog {
    private JPanel contentPanel;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel bottonsPanel;
    private JLabel HeadLine;
    private JComboBox regionDropBox;
    private JPanel choosePanel;
    private JPanel LongPanel;
    private JTextField maxLong;
    private JTextField minLon;
    private JTextField maxLat;
    private JTextField minLat;
    private JPanel buttonsPanle;
    private JPanel latPanel;

    static public Region region = Region.TLV;

    private ChooseRegionFrame() {
        setContentPane(contentPanel);

        getRootPane().setDefaultButton(buttonOK);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(100, 100, (int) (dim.getWidth()/2), (int) ((dim.getHeight()-50)/2));

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPanel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setModal(true);
        regionDropBox.addActionListener(e -> {
            String selectedItem = (String) regionDropBox.getSelectedItem();
            assert selectedItem != null;
            if(selectedItem.equals(Region.CUSTOM.name)){
                LongPanel.setVisible(true);
                latPanel.setVisible(true);
            }
        });
    }

    private void onOK() {
        String selectedItem = (String) regionDropBox.getSelectedItem();

        if (Region.TLV.name.equals(selectedItem)) {
            region = Region.TLV;
        } else if (Region.BERLIN.name.equals(selectedItem)) {
            region = Region.BERLIN;
        } else if (Region.CUSTOM.name.equals(selectedItem)) {
            region = Region.CUSTOM;
            RoadMapHandler.updateBounds(
                    Double.parseDouble(maxLat.getText()), Double.parseDouble(minLat.getText()),
                    Double.parseDouble(maxLong.getText()), Double.parseDouble(minLon.getText())
            );
        }

        dispose();
    }

    private void onCancel() {
        dispose();

    }

    static public String choose(){
        ChooseRegionFrame dialog = new ChooseRegionFrame();
        dialog.pack();
        dialog.setVisible(true);

        switch (region){
            case TLV -> {
                RoadMap.INSTANCE.setName("TLV");
                return region.path;
            }
            case BERLIN -> {
                RoadMapHandler.setBounds(false);
                RoadMap.INSTANCE.setName("Berlin");
                return region.path;
            }
            case CUSTOM -> {
                return "Custom";
            }
            default -> throw new IllegalStateException("Unexpected value: " + region);
        }
    }

    public static String chooseFile() {
        // JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        JFileChooser jfc = new JFileChooser("data/maps/osm");
        jfc.setDialogTitle("Select .osm.pbf file to read");
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        return "Not A Valid Path";
    }

    private void createUIComponents() {
        String regions[] = {region.TLV.name, region.BERLIN.name, region.CUSTOM.name};
        regionDropBox = new JComboBox<>(regions);
    }

    public enum Region {
        TLV("Tel-Aviv", "tlv.json"),
        BERLIN("Berlin", "berlin.json"),
        CUSTOM("Custom");

        String name;
        String path;

        Region(String name, String path) {
            this.name = name;
            this.path = path;
        }

        Region(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        System.out.println("picked " + choose());
        System.exit(0);
    }
}
