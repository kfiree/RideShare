package app.model;

import app.model.interfaces.ElementsOnMap;

import java.util.*;

/**
 * todo make iterate on edges thread safe
 *      add only from here
 */
public class UserMap {
    private final Hashtable<String, Drive> drives;
    private final Hashtable<String, Pedestrian> requests;
    private final HashSet<ElementsOnMap> finished;

    /** CONSTRUCTORS */
    public UserMap() {
        drives = new Hashtable<>();
        requests = new Hashtable<>();
        finished = new HashSet<>();
    }

    /**  Singleton specific properties */
    public static final UserMap INSTANCE = new UserMap();



    /** GETTERS */

    public Collection<Drive> getDrives() { return drives.values(); }

    public Drive getDrive(String key) { return drives.get(key); }

    public Collection<Pedestrian> getRequests() { return requests.values(); }

    public Pedestrian getPedestrian(String key) { return requests.get(key); }

    public HashSet<ElementsOnMap> getFinished() {
        return finished;
    }

    /**     SETTERS     */

    public void setDrives(Map<String, Drive> drives) { this.drives.putAll(drives); }

    public void setRequests(Map<String, Pedestrian> requests) { this.requests.putAll(requests); }

    public void addDrive(Drive drive) {
        Thread driveThread = new Thread(drive);
        driveThread.start();
        drives.put(drive.getId(), drive);
    }
    public Drive addDrive(Node src, Node dst, String type, String id, Date date) {
        return drives.put(id, new Drive(src, dst, type, id, date));
    }

    public void addRequest(Pedestrian pedestrian) { requests.put(pedestrian.getId(), pedestrian); }

    public Pedestrian addRequest(String id, Node src, Node dst, Date date) {
        return requests.put(id, new Pedestrian(id, src, dst, date));
    }



    /** REMOVE FROM GRAPH */

    public void finished(ElementsOnMap element) {
        if(element instanceof Drive){
            drives.remove(element.getId());
        }else{
            requests.remove(element.getId());
        }
        finished.add(element);
    }//todo cancel when thread pool ready

    public void pickPedestrian(Pedestrian pedestrian){
        requests.remove(pedestrian.getId());
    }

//    public void remove(ElementsOnMap element) { finished.remove(element); }



    @Override
    public String toString() {
        return "UserMap{" +
                ", pedestrians=" + requests.size() +
                ", drives=" + drives.size() +
                '}';
    }
}
