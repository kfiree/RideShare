package app.model;

import org.jetbrains.annotations.NotNull;
import java.util.Date;

import app.model.interfaces.ElementOnMap;
import static utils.Utils.FORMAT;



public class Rider implements ElementOnMap {
    private final Date askTime;
    private final Node src, dest;
    private final String id;
    private boolean taken;
    private boolean pickedUp;


    public Rider(String id, @NotNull Node currNode, @NotNull Node destination, Date askTime) {
        this.id = "R" + id;
        this.askTime = askTime;
        this.dest = destination;
        this.src = currNode;
    }

    public void markTaken() {
        /* remove this from waiting list */
        taken = true;
        UserMap.INSTANCE.pickPedestrian(this);
    }

    public boolean isTaken() {
        return taken;
    }

    @Override
    public Node getNextStop(){
        if( isPickedUp()){
            return getDest();
        }else{
            return getCurrentNode();
        }

    }

    @Override
    public Node getDest() { return dest; }

    @Override
    public Node getCurrentNode() { return src; }

    public void setPickUp() {
        this.pickedUp = true;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    @Override
    public Date getStartTime() { return askTime; }

    @Override
    public String getId() { return id; }

    @Override
    public GeoLocation getLocation() { return src.getLocation(); }

    @Override
    public String toString() {

        return "Rider{" +
                "id='" + id.substring(1) + '\'' +
                ", long id='" + id + '\'' +
                ", askTime=" + FORMAT(askTime) +
                ", currNode=" + src.getOsmID() +
                ", destination=" + dest.getOsmID() +
                '}';
    }
}
