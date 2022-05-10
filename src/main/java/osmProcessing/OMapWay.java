package osmProcessing;

import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import java.util.*;

public class OMapWay {

    private long id;
    // Nodes on way, referenced by id:
    private Map<Long, MapObject> objects = new HashMap<Long, MapObject>();
    // Tags referenced by tag-key:
    private Map<String, String> tags = new HashMap<String, String>();
    //TODO get name from nodes
    // Name of the street if provided:
    private String name = "";

    public OMapWay(long id, Collection<Tag> tags) {
        this.id = id;
        this.addAllTags(tags);
    }

    public long getID() {
        return this.id;
    }

    public void addObject(MapObject mo) {
        this.objects.put(mo.getID(), mo);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Long, MapObject> getObjects() {
        return this.objects;
    }

    public LinkedList<MapObject> getObjectsList() {
        return new LinkedList<MapObject>(this.objects.values());
    }

    public MapObject pollFirstObject() {
        long id = this.getObjectsList().getFirst().getID();
        MapObject result = this.objects.remove(id);
        return result;
    }

    public MapObject pollLastObject() {
        long id = this.getObjectsList().getLast().getID();
        MapObject result = this.objects.remove(id);
        return result;
    }

    public Map<String, String> getTags() {
        return this.tags;
    }

    public void addAllTags(Collection<Tag> tags) {
        if (tags.isEmpty() == false) {
            for(Tag tag: tags) {
                this.tags.put(tag.getKey(), tag.getValue());
            }
        }
    }

    /**
     *
     * @param key
     * @return value of provided key. E.g.: getTag("highway") -> "primary"
     * @throws NoSuchElementException because tags do not
     * always exist. Person who will use this method later,
     * will notice, that exception can be thrown and work with the case
     * that the key does not exist
     */

    public String getTag(String key) throws NoSuchElementException {

        if (tags.get(key) == null) {
            throw new NoSuchElementException("Key "+key+" is not given for this edge");
        }
        else {
            return this.tags.get(key);
        }

    }

}
