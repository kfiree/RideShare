package app.controller.osm_processing;

import static app.model.utils.RoadMapHandler.inBound;
import static utils.logs.LogHandler.LOGGER;

import org.openstreetmap.osmosis.core.container.v0_6.*;
import org.openstreetmap.osmosis.core.domain.v0_6.*;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

import java.util.*;

/**
 *      |==================================|
 *      |==========| OSM READER  |=========|
 *      |==================================|
 *
 *
 *  Read '.pbf' file and initialize a collection of OsmObject and collection of OsmWay (for the Parser class to parse).
 *
 * @author  Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 1.0
 * @since   2021-06-20
 */
public class Reader implements Sink {
    private final ArrayList<OsmWay> ways = new ArrayList<>();
    private final Map<Long, OsmObject> mapObjects = new HashMap<>();
    private final HashMap<Long, Long> JUNCTIONS = new HashMap<>();
    private static int nodeNum = 0, wayNum = 0;

    /* GETTERS */
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
            nodeNum++;
            processNode(((NodeContainer) entityContainer).getEntity());
            if(nodeNum % 1000000 == 0){
                LOGGER.info("Reader has processed " + nodeNum + " nodes.");
            }
        } else if (entityContainer instanceof WayContainer){
            wayNum++;
            if(wayNum % 100000 == 0){
                LOGGER.info("Reader has processed " + wayNum + " ways.");
            }
            processWay(((WayContainer) entityContainer).getEntity());
        }



    }

    private void processWay(Way way){
        // you can filter ways/nodes //
        if (isAppropriate(way)) {
            OsmWay osmWay = new OsmWay(way.getId(), way.getTags());

            // process all nodes contained in the way
            boolean isJunction = way.getTags().contains("junction");

            if(isJunction){ // replace all osmObject with Junction-OsmObject

                long junctionId = way.getWayNodes().get(0).getNodeId();
                for(WayNode wn: way.getWayNodes()){
                    JUNCTIONS.put(wn.getNodeId(), junctionId);
                }
            } else {
                for (WayNode wn : way.getWayNodes()) {
                    OsmObject osmObject = getOsmObj(wn.getNodeId());
                    if(osmObject != null){ //TODO remove after non-existing node fixed
                        osmObject.incrementCounter();
                        osmWay.addObject(osmObject);
                    }
                }
            }

            if(!way.getWayNodes().isEmpty()) {
                ways.add(osmWay);
            }
        }
    }

    private void processNode(Node node){
        OsmObject temp;

        // create node or add details if object was already created through ways
        // (usually not the case, if .pbf file formatted correctly)
        if (mapObjects.get(node.getId()) != null) {
            temp = mapObjects.get(node.getId());
            temp.setCoordinates(node.getLatitude(), node.getLongitude());
            temp.addAllTags(node.getTags());
        } else if(inBound(node.getLongitude(), node.getLatitude())) {
            temp = new OsmObject(node);
            mapObjects.put(temp.getID(), temp);
        }
    }

    /**
     * if object is part of junction return junction-object else return object.
     *
     * @param id osm id
     * @return OsmObject corresponding to given id
     */
    private OsmObject getOsmObj(long id) {

        Long junctionID = JUNCTIONS.get(id);
        if(junctionID != null){
            id = junctionID;
        }

        return mapObjects.get(id);
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
                carAllowed =  !fillterTags.contains(tag.getValue());
            }
        }
        return carAllowed;
    }

    /**
     * Values of tag 'highway' of ways in OSM that are not important for creating a road map.
     *  src: https://wiki.openstreetmap.org/wiki/Key:highway
     *
     * I pooled out "trunk".
     */
    private final HashSet<String> fillterTags = new HashSet<>(Arrays.asList( "motorway", "pedestrian", "footway", "bridleway", "steps", "path", "cycleway",
            "construction", "proposed", "bus_stop", "elevator", "street_lamp", "stop", "traffic_signals", "service", "track", "platform", "raceway",
            "abandoned", "road" , "escape" , "proposed" , "construction", "corridor", "bridleway" , "bus_guideway", "none", "motorway_link",
            "unclassified"
            , "construction", "service"
//            , "tertiary_link", "secondary_link"
//            , "residential"
//            , "living_street"
    ));



    @Override
    public void initialize(Map<String, Object> map) {}
    @Override
    public void complete() {}
    @Override
    public void release() {}
}