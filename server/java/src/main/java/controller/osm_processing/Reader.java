package controller.osm_processing;

import controller.utils.MapUtils;

import org.openstreetmap.osmosis.core.container.v0_6.*;
import org.openstreetmap.osmosis.core.domain.v0_6.*;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *      |==================================|
 *      |==========| OSM READER  |=========|
 *      |==================================|
 *
 *
 *  static class that read '.pbf' file *
 *  and return a collection of OsmObject and collection of OsmWay (for the Parser class to parse).
 *
 *
 *
 * @author  Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 1.0
 * @since   2021-06-20
 */
public class Reader implements Sink {
    private final ArrayList<OsmWay> ways = new ArrayList<>();
    private final Map<Long, OsmObject> mapObjects = new HashMap<>();
    private static final HashMap<Long, Long> JUNCTIONS = new HashMap<>();

    /** GETTERS */
    public ArrayList<OsmWay> getWays() {
        return ways;
    }



    /**
     * Method called on each entity from original XML file
     * @param entityContainer incoming entity for procession
     * Method separates entities in different collections
     *
     * EntityContainer Types:
     *      - NodeContainer
     *      - WayContainer
     *      - BoundContainer
     *      - RelationContainer
     *      - Unknown Entity
     *
     */
    @Override
    public void process(EntityContainer entityContainer) {
        if (entityContainer instanceof NodeContainer){
            processNode(((NodeContainer) entityContainer).getEntity());

        } else if (entityContainer instanceof WayContainer){
            processWay(((WayContainer) entityContainer).getEntity());
        }
    }

    private void processWay(Way way){
//        Way way = ((WayContainer) entityContainer).getEntity();

        // you can filter ways/nodes //
        if (isAppropriate(way)) {
            OsmWay mWay = new OsmWay(way.getId(), way.getTags());
            // process all nodes contained in the way //
            for(Tag t: way.getTags()){
                if(t.getKey().equals("junction")){
                    WayNode first = way.getWayNodes().get(0);

                    for(WayNode wn: way.getWayNodes()){
                        JUNCTIONS.put(wn.getNodeId(), first.getNodeId());
                    }
                    break;
                }
            }
            for(WayNode wn: way.getWayNodes()) {
                OsmObject temp;

                // if object was already created through nodes:
                if (mapObjects.get(wn.getNodeId()) != null) {
                    temp = mapObjects.get(wn.getNodeId());

                    // outside of if
                    mWay.addObject(temp);
                    temp.setLinkCounter(temp.getLinkCounter() + 1);
                }

            }
            ways.add(mWay);
        }
    }

    //TODO add alias like to org.openstreetmap.osmosis.core.domain.v0_6.Node so wont be confused with model.Node                sources =  https://stackoverflow.com/questions/2447880/change-name-of-import-in-java-or-import-two-classes-with-the-same-name      https://itecnote.com/tecnote/java-change-name-of-import-in-java-or-import-two-classes-with-the-same-name/
    private void processNode(Node node){
        OsmObject temp;

        if(MapUtils.inBound(node.getLongitude(), node.getLatitude())){
            temp = new OsmObject(node.getLatitude(), node.getLongitude(), node.getId(), node.getTags());
            mapObjects.put(temp.getID(), temp);

            // create node or add details if object was already created through ways (usually not the case, if .pbf file formatted correctly)
            if (mapObjects.get(node.getId()) != null) {
                temp = mapObjects.get(node.getId());
                temp.setCoordinates(node.getLatitude(), node.getLongitude());
                temp.addAllTags(node.getTags());
            }
            else {
                temp = new OsmObject(node.getLatitude(), node.getLongitude(), node.getId(), node.getTags());

                mapObjects.put(temp.getID(), temp);
            }
        }

    }

    /**
     * Checks, whether a way is appropriate for our map
     * @param way from pbf file to check
     * @return true if
     * (1) way contains highway tag
     * (2) way not for only pedestrians, not a footway
     * (3) way should have a name (street name)
     */
    private boolean isAppropriate(Way way) {
        boolean carAllowed = false;

        for (Tag tag : way.getTags()) {
            if (tag.getKey().equals("highway") ) {
                carAllowed =  !noVehicleValues.contains(tag.getValue());
            }
        }
        return carAllowed;
    }

    /**
     * Values of tag 'highway' of ways in OSM where no cars are allowed
     * Feel free to fill the list: https://wiki.openstreetmap.org/wiki/Key:highway
     * TODO use more efficient collection
     * TODO check if links are useful
     */
    private final List<String> noVehicleValues = Arrays.asList( "motorway", "pedestrian", "footway", "bridleway", "steps", "path", "cycleway",
            "construction", "proposed", "bus_stop", "elevator", "street_lamp", "stop", "traffic_signals", "service", "track", "platform", "raceway",
            "abandoned", "road" , "escape" , "proposed" , "construction", "corridor", "bridleway" , "bus_guideway", "none", "motorway_link",
            "unclassified"
            , "construction", "service"
//            , "tertiary_link", "secondary_link"
//            , "residential"
//            , "living_street"
    );
    // tags i pooled out "trunk",

    public static HashMap<Long, Long> getJunctions() {
        return JUNCTIONS;
    }

    // following methods are a requirement of the Sink interface //
    /** Sink interface methods */
    @Override
    public void initialize(Map<String, Object> map) {}
    @Override
    public void complete() {}
    @Override
    public void release() {}
}