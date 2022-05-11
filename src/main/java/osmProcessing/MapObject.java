package osmProcessing;

import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class MapObject {

    /**
     * Properties:
     */

    private Double latitude;
    private Double longitude;
    private long id;
    private Map<String, String> tags = new HashMap<String, String>();
    private Map<Long, OMapWay> ways = new HashMap<Long, OMapWay>();

    // Objects with linkCounter greater then 1 will become crosses
    // or Entities of class OsmNode
    public int linkCounter = 0;

    public MapObject(long id) {
        this.id = id;
    }

    public MapObject(Double lat, Double longi, long id, Collection<Tag> tags) {
        this.setLatitude(lat);
        this.setLongitude(longi);
        this.id = id;
        this.addAllTags(tags);
    }

    /**
     * GETTERS
     * @return attributes
     */

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public long getID() {
        return this.id;
    }

    public String getTag(String key) throws NoSuchElementException {

        if (tags.get(key) == null) {
            throw new NoSuchElementException("Key "+key+" is not given for this object");
        }
        else {
            return this.tags.get(key);
        }

    }

    public Map<Long, OMapWay> getWays() {
        return this.ways;
    }

    public Map<String, String> getTags() {
        return this.tags;
    }

//    /**
//     * SETTERS
//     * @param attributes
//     */

    public void setLatitude(Double lat) {
        this.latitude = lat;
    }

    public void setLongitude(Double lon) {
        this.longitude = lon;
    }

    public void addAllTags(Collection<Tag> tags) {
        if (tags.isEmpty() == false) {
            for(Tag tag: tags) {
                this.tags.put(tag.getKey(), tag.getValue());
            }
        }
    }

    public void addWay(OMapWay way) {
        this.ways.put(way.getID(), way);
    }
}
