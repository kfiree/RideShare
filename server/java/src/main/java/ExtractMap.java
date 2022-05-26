import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/* Supporting extra stuff */
/* Not important for functionality */
public class ExtractMap {
    public static String chooseFile() {
//        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        JFileChooser jfc = new JFileChooser("server/java/data");
        jfc.setDialogTitle("Select .osm.pbf file to read");
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        return null;
    }

    public static String chooseDirectory() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Select target directory for osm file");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setAcceptAllFileFilterUsed(false);
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFile().getAbsolutePath()+jfc.getCurrentDirectory().separator;
        }
        return null;
    }

    public static String getCurrentDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }


}
