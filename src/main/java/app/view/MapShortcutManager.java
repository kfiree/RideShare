package app.view;

import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.util.DefaultShortcutManager;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import static utils.Utils.JSON_PATH;

public class MapShortcutManager extends DefaultShortcutManager {
    boolean shiftPressed,ctrlPressed;
    private enum KeyMap{
        SHIFT(16),
        CTRL(17),
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

    @Override
    public void keyPressed(KeyEvent event) {
        System.out.println(event.getKeyCode());
        if(event.getKeyCode() == KeyMap.CTRL.key){
            ctrlPressed = true;
        }else if(event.getKeyCode() == KeyMap.SHIFT.key){
            shiftPressed = true;
        }else if(ctrlPressed){
            if(event.getKeyCode() == KeyMap.D.key){
                System.out.println("pressed ctrl + d " + event.getKeyCode());
            }else if(event.getKeyCode() == KeyMap.R.key){
                System.out.println("pressed ctrl + r " + event.getKeyCode());
            }
        }

        super.keyPressed(event);
    }

    @Override
    public void keyReleased(KeyEvent event) {
        if(event.getKeyCode() == 17){
            ctrlPressed = false;
        }else if(event.getKeyCode() == 16){
            shiftPressed = false;
        }

        System.out.println("released " + event.getKeyCode());
        super.keyReleased(event);
    }

    @Override
    public void keyTyped(KeyEvent event) {
        System.out.println("typed " + event.getKeyCode());
        super.keyTyped(event);
    }
}
