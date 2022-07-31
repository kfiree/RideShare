package app.model;

import org.jetbrains.annotations.NotNull;
import java.util.Date;

import app.model.interfaces.ElementOnMap;
import static utils.Utils.FORMAT;



public class Rider implements ElementOnMap {
    private final Date askTime;
    private final Node src, dest;
    private final int id;
    private boolean taken;
    private boolean carNextTarget;


    public Rider(@NotNull Node currNode, @NotNull Node destination, Date askTime) {
        this.id = UserMap.keyGenerator.incrementAndGet();
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
        if( isCarNextTarget()){
            return getDest();
        }else{
            return getCurrentNode();
        }

    }

    @Override
    public Node getDest() { return dest; }

    @Override
    public Node getCurrentNode() { return src; }

    public void setCarNextTarget(boolean nextTarget) {
        this.carNextTarget = nextTarget;
    }

    public boolean isCarNextTarget() {
        return carNextTarget;
    }

    @Override
    public Date getStartTime() { return askTime; }

    @Override
    public int getId() { return id; }

    @Override
    public GeoLocation getLocation() { return src.getLocation(); }

    @Override
    public String toString() {

        return "Rider{" +
                "id=" + id +
                ", askTime=" + FORMAT(askTime) +
                ", currNode=" + src.getId() +
                ", destination=" + dest.getId() +
                '}';
    }
}
