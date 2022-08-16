package app.model.users;

import app.model.graph.Node;
import org.jetbrains.annotations.NotNull;
import java.util.Date;
import static utils.Utils.FORMAT;



public class Passenger extends User {
    private final Date askTime;
    private final Node src, dest;
    private boolean matched;
    private boolean carNextTarget;


    /* CONSTRUCTORS  */
    public Passenger(@NotNull Node currNode, @NotNull Node destination, Date askTime) {
        this.askTime = askTime;
        this.dest = destination;
        this.src = currNode;
    }


    /* LOGIC */

    public void markMatched() {
        /* remove this from waiting list */
        matched = true;
        UserMap.INSTANCE.matchPassenger(this);
    }

    public boolean isMatched() {
        return matched;
    }

//    public boolean distTo(Node node) {
//        return Math.min(getDestination().distanceTo(node), getLocation().distanceTo(node));
//    }



    /* GETTERS */

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

    @Override
    public Date getStartTime() { return askTime; }



    /* SETTERS */

    public void setCarNextTarget(boolean nextTarget) {
        this.carNextTarget = nextTarget;
    }

    public boolean isCarNextTarget() {
        return carNextTarget;
    }




    @Override
    public String toString() {
        return "Rider " + id + ", choose time " + FORMAT(askTime) ;
    }
}
