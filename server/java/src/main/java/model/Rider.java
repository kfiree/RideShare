package model;

import org.jetbrains.annotations.NotNull;
import controller.osm_processing.OsmObject;

/**
 *      |==================================|
 *      |=============| RIDER  |============|
 *      |==================================|
 *
 *
 * @author  Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 1.0
 * @since   2021-06-20
 */
public class Rider extends Node {
    private Node nearestNode;

    public Rider(@NotNull OsmObject object) {
        super(object);
    }

    public Rider(long id, Double latitude, Double longitude, userType user) {
        super(null, id, new GeoLocation(latitude, longitude), user);
    }
}
