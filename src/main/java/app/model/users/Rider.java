package app.model.users;

import app.model.graph.Node;
import org.jetbrains.annotations.NotNull;
import java.util.Date;
import static utils.Utils.FORMAT;



public class Rider extends User {
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
            return getDestination();
        }else{
            return getLocation();
        }
    }

    @Override
    public Node getDestination() { return dest; }

    @Override
    public Node getLocation() { return src; }

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
    public String toString() {
        return "Rider " + id + ", choose time " + FORMAT(askTime) ;
    }
}
