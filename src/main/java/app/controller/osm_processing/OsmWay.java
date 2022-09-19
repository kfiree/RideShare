package app.controller.osm_processing;

import app.model.utils.Coordinates;
import app.model.utils.Located;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import java.util.*;

/**
 *      |==================================|
 *      |===========|  OSM WAY  |==========|
 *      |==================================|
 *
 *
 * before they get parsed to Edges
 *
 * @author  Kfir Ettinger
 * @version 2.0
 * @since   2021-06-20
 */
public class OsmWay implements Located {

    private final long id;
    private final List<OsmObject> objectsOnWay = new ArrayList<>();
    private final Map<String, String> tags = new HashMap<>();

    public OsmWay(long id, Collection<Tag> tags) {
        this.id = id;
        addAllTags(tags);
    }

    public long getID() {
        return id;
    } //currently not in use

    public void addObject(OsmObject obj) {
        objectsOnWay.add(obj);
    }

    public List<OsmObject> getObjectsOnWay() {
        return objectsOnWay;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void addAllTags(Collection<Tag> tags) {
        if (!tags.isEmpty()) {
            for(Tag tag: tags) {
                this.tags.put(tag.getKey(), tag.getValue());
            }
        }
    }

    /**
     * @return coordinates of way's choose
     */
    @Override
    public Coordinates getCoordinates() {
        if(objectsOnWay.isEmpty()) {
            return null;
        }
        return objectsOnWay.get(0).getCoordinates();
    }

    @Override
    public boolean inBound() {
        for (OsmObject obj:objectsOnWay) {
            if(!obj.inBound()){
                return false;
            }
        }
        return true;
    }
}
