package controller.osm_processing;

import controller.rds.jsonHandler;
import static controller.utils.MapUtils.inBound;

import model.GeoLocation;
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
        // you can filter ways/nodes //
        if (isAppropriate(way)) {//TODO add check if empty to isAppropriate()
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
                        osmObject.incrementCounter();// todo make a list of un-init nodes and empty when init and check if empty
                        osmWay.addObject(osmObject);
                    }
                }
            }

            if(!way.getWayNodes().isEmpty()) {
                ways.add(osmWay);
            }
        }
    }

    //TODO add alias like to org.openstreetmap.osmosis.core.domain.v0_6.Node so wont be confused with model.Node                sources =  https://stackoverflow.com/questions/2447880/change-name-of-import-in-java-or-import-two-classes-with-the-same-name      https://itecnote.com/tecnote/java-change-name-of-import-in-java-or-import-two-classes-with-the-same-name/
    private void processNode(Node node){
        OsmObject temp;

        // create node or add details if object was already created through ways (usually not the case, if .pbf file formatted correctly)
        if (mapObjects.get(node.getId()) != null) {
            temp = mapObjects.get(node.getId());//todo make one method in node
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

    // following methods are a requirement of the Sink interface //
    /** Sink interface methods */
    @Override
    public void initialize(Map<String, Object> map) {}
    @Override
    public void complete() {}
    @Override
    public void release() {}
}