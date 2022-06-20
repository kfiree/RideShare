package controller.osm_processing;

import model.GeoLocation;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *      |==================================|
 *      |==========| MAP OBJECT |==========|
 *      |==================================|
 *
 * create map object for each Node:
 *      - GeoLocation coordinates := (latitude, longitude)
 *      - Long id := osm id
 *      - Map tags := all objects tags
 *      - int linkCounter := count appearances of object in osm ways.
 *                          if linkCounter greater then 1 node will be a crosses or entities of class Node
 *
 * @author  Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 1.0
 * @since   2021-06-20
 */
public class OsmObject {

    private GeoLocation coordinates;
    private final long id;
    private final Map<String, String> tags = new HashMap<>();
    private int linkCounter;

    /** CONSTRUCTORS */
    public OsmObject(Double latitude, Double longitude, long id, Collection<Tag> tags) {
        coordinates = new GeoLocation(latitude, longitude);
        this.id = id;
        addAllTags(tags);//todo prettify
    }

    /** GETTERS */

    public long getID() {
        return id;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public GeoLocation getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Double latitude, Double longitude) {
        coordinates = new GeoLocation(latitude, longitude);
    }

    /** SETTERS */

    public void addAllTags(Collection<Tag> tags) {
        if (!tags.isEmpty()) {
            for(Tag tag: tags) {
                this.tags.put(tag.getKey(), tag.getValue());
            }
        }
    }

    public int getLinkCounter() {
        return linkCounter;
    }

    public void setLinkCounter(int linkCounter) {
        this.linkCounter = linkCounter;
    }
}
