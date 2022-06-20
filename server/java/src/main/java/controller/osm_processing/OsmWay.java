package controller.osm_processing;

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
 * @author  Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 2.0
 * @since   2021-06-20
 */
public class OsmWay {

    private final long id;
    private OsmObject first, last;
    // Nodes on way, referenced by id:
    private final Map<Long, OsmObject> objects = new HashMap<>();
    // Tags referenced by tag-key:
    private final Map<String, String> tags = new HashMap<>();

    public OsmWay(long id, Collection<Tag> tags) {
        this.id = id;
        addAllTags(tags);
    }

    public long getID() {
        return id;
    }

    public void addObject(OsmObject mo) {
        if(first == null){
            first = mo;
        }
        last = mo;
        objects.put(mo.getID(), mo);
    }

    public Map<Long, OsmObject> getObjects() {
        return objects;
    }

    public LinkedList<OsmObject> getObjectsList() {
        return new LinkedList<>(objects.values());
    }

    public OsmObject getFirst() {
        return first;
    }

    public OsmObject getLast() {
        return last;
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
}
