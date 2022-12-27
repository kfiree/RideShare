package simulator.model.users;

import simulator.model.graph.Node;
import simulator.model.graph.utils.Coordinates;
import simulator.model.graph.utils.Located;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class User implements Comparable<User>, Located {
    protected static AtomicInteger keyGenerator = new AtomicInteger(-1);
    private Node currNode, destination;
    private Date startTime;
    final int id = keyGenerator.incrementAndGet();;

    /* GETTERS*/

    @Override
    public Coordinates getCoordinates() {
        return getLocation().getCoordinates();
    }

    public Node getLocation() {
        return currNode;
    }

    public int getId() {
        return id;
    }

    public Node getFinalDestination() {
        return destination;
    }

    public Date getStartTime() {
        return startTime;
    }

    public abstract  Node getNextStop();

    @Override
    public int compareTo(@NotNull User other) {
        return (int) (getStartTime().getTime() - other.getStartTime().getTime());
    }
}
