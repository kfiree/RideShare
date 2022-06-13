package model;

import org.jetbrains.annotations.NotNull;
import controller.osmProcessing.MapObject;

public class ORider extends ONode {
    private ONode nearestNode;

    public ORider(@NotNull MapObject object) {
        super(object);
    }

    public ORider(long id, Double latitude, Double longitude, userType user) {
        super(null, id, latitude, longitude, user);
    }
}
