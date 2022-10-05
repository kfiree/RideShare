package simulator.view.utils;

import simulator.view.MapView;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.util.DefaultShortcutManager;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class MapShortcutManager extends DefaultShortcutManager {
    private boolean shiftPressed,ctrlPressed, driveInput, requestInput;
    private char[] digits = new char[3];
    private int id = 0;
    private int index = 0;

    private enum KeyMap{
        SHIFT(16),
        CTRL(17),
        LEFT(37),
        UP(38),
        RIGHT(39),
        DOWN(40),
        ENTER(10),
        MINUS(45),
        PLUS(61),
        D(68),
        R(82);

        public final int key;
        KeyMap(int key) {
            this.key = key;
        }
    }

    public MapShortcutManager() {
        super();
    }

    @Override
    public void init(GraphicGraph graph, View view) {
        super.init(graph, view);
    }

    @Override
    public void release() {
        super.release();
    }

    private void addDigit(KeyEvent event){
        id*=10;
        id += event.getKeyCode() - 48;
    }

    private boolean isNumber(KeyEvent event){
        int keyCode = event.getKeyCode();
        return keyCode >= 48 && keyCode <= 57;
    }

    @Override
    public void keyPressed(KeyEvent event) {

        if(driveInput){
            if(isNumber(event)){
                addDigit(event);
                index ++;
            }
        }else if(event.getKeyCode() == KeyMap.CTRL.key){
            ctrlPressed = true;
        }else if(event.getKeyCode() == KeyMap.SHIFT.key){
            shiftPressed = true;
        }else if(ctrlPressed){
            if(event.getKeyCode() == KeyMap.D.key){
                dropdownList();
            }else if(event.getKeyCode() == KeyMap.R.key){
                dropdownList();
            }
        }

        super.keyPressed(event);
    }

    private static void dropdownList(){
        String[] ids = MapView.INSTANCE.getElementsOnMapNodes().values().stream().map(Node::getId).toArray(String[]::new);


        if(ids.length>0) {
            int userId =
                    Integer.parseInt((String) JOptionPane.showInputDialog(
                    null,
                    "Choose User",
                    "Show user information",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    ids,
                    ids[0]
            ));

            System.out.println("Choose user : " + userId);

//            stylePath(UserMap.INSTANCE.getDrive(userId));
        }

    }

    @Override
    public void keyReleased(KeyEvent event) {
        if(event.getKeyCode() == 17){
            ctrlPressed = false;
        }else if(event.getKeyCode() == 16){
            shiftPressed = false;
        }

        super.keyReleased(event);
    }

    @Override
    public void keyTyped(KeyEvent event) {
        System.out.println("typed " + event.getKeyCode());
        super.keyTyped(event);
    }
}
