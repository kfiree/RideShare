package model;

import org.jetbrains.annotations.NotNull;
import controller.osmProcessing.MapObject;

public class ORider extends ONode {
    private ONode nearestNode;

    public ORider(@NotNull MapObject object) {
        super(object);
    }

    public ORider(long id, Double @NotNull [] coordinates, userType user) {
        super(id, coordinates, user);
    }
}
