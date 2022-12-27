package simulator.model.graph.utils;

//import simulator.model.users.utils.GraphAlgo;

public interface Located {

    Coordinates getCoordinates();

    default boolean inBound() {
        return Bounds.inBound(getCoordinates());
    }

    default double distanceTo(Located other){
        return GraphUtils.distance(getCoordinates(), other.getCoordinates());
    }

    @Override
    boolean equals(Object o);

    @Override
    String toString();
}