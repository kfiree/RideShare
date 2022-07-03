package model;

import model.interfaces.ElementsOnMap;
import view.MapView;

import java.util.*;

/**
 * todo make iterate on edges thread safe
 *      add only from here
 */
public class UsersMap{
    private final Hashtable<String, Drive> drives;
    private final Hashtable<String, Pedestrian> waitingForRide;
    private final HashSet<ElementsOnMap> finished;

    /** CONSTRUCTORS */
    public UsersMap() {
        drives = new Hashtable<>();
        waitingForRide = new Hashtable<>();
        finished = new HashSet<>();
    }

    /**  Singleton specific properties */
    public static final UsersMap INSTANCE = new UsersMap();



    /** GETTERS */

    public Collection<Drive> getDrives() { return drives.values(); }

    public Drive getDrive(String key) { return drives.get(key); }

    public Collection<Pedestrian> getWaitingForRide() { return waitingForRide.values(); }

    public Pedestrian getPedestrian(String key) { return waitingForRide.get(key); }

    public HashSet<ElementsOnMap> getFinished() {
        return finished;
    }

    /**     SETTERS     */

    public void setDrives(Map<String, Drive> drives) { this.drives.putAll(drives); }

    public void setWaitingForRide(Map<String, Pedestrian> waitingForRide) { this.waitingForRide.putAll(waitingForRide); }

    public void addDrive(Drive drive) {
        Thread driveThread = new Thread(drive);
        driveThread.start();
        drives.put(drive.getId(), drive);
    }
    public Drive addDrive(Node src, Node dst, String type, String id, Date date) {
        return drives.put(id, new Drive(src, dst, type, id, date));
    }

    public void addPedestrian(Pedestrian pedestrian) { waitingForRide.put(pedestrian.getId(), pedestrian); }

    public Pedestrian addPedestrian(String id, Node src, Node dst, Date date) {
        return waitingForRide.put(id, new Pedestrian(id, src, dst, date));
    }



    /** REMOVE FROM GRAPH */

    public void finished(ElementsOnMap element) {
        if(element instanceof Drive){
            System.out.println("finish drive");
            drives.remove(element.getId());
        }else{
            waitingForRide.remove(element.getId());
        }
        finished.add(element);
    }//todo cancel when thread pool ready

    public void pickPedestrian(Pedestrian pedestrian){
        waitingForRide.remove(pedestrian.getId());
    }

//    public void remove(ElementsOnMap element) { finished.remove(element); }



    @Override
    public String toString() {
        return "UserMap{" +
                ", pedestrians=" + waitingForRide.size() +
                ", drives=" + drives.size() +
                '}';
    }
}
