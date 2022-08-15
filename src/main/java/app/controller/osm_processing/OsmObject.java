package app.controller.osm_processing;

import app.model.utils.RoadMapHandler;
import app.model.utils.Coordinates;
import app.model.utils.Located;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
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
public class OsmObject  implements Located {

    private Coordinates coordinates;
    private final long id;
    private final Map<String, String> tags = new HashMap<>();
    private int linkCounter;

    /* CONSTRUCTORS */
    public OsmObject(Node osmNode){
        coordinates = new Coordinates(osmNode.getLatitude(), osmNode.getLongitude());
        id = osmNode.getId();
        addAllTags(osmNode.getTags());
    }

    /* GETTERS */

    public long getID() {
        return id;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    @Override
    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean inBound() {
        return RoadMapHandler.inBound(coordinates);
    }

    public void setCoordinates(Double latitude, Double longitude) {
        coordinates = new Coordinates(latitude, longitude);
    }

    /* SETTERS */

    public void addAllTags(Collection<Tag> tags) {
        if (!tags.isEmpty()) {
            for(Tag tag: tags) {
                this.tags.put(tag.getKey(), tag.getValue());
            }
        }
    }

    public boolean isPartOfAnotherWay(){
        return linkCounter > 1;
    }

    public void incrementCounter() {
        linkCounter ++;
    }
}
