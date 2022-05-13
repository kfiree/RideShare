package osmProcessing;

import org.openstreetmap.osmosis.core.container.v0_6.*;
import org.openstreetmap.osmosis.core.domain.v0_6.*;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

import java.awt.geom.Point2D;
import java.util.*;

//osmconvert64.exe ariel2.osm > arielpbf.pbf --out-pbf
public class Reader implements Sink {
    // Variables for algorithms :)
    public ArrayList<OMapWay> ways = new ArrayList<>();
    // Reference by id:
    public Map<Long, MapObject> MapObjects = new HashMap<>();
    static private HashMap<Long, Long> junctions = new HashMap<>();
    static int entityNum = 0;
    static HashSet<String> nodeTags = new HashSet<>(),
    wayTags = new HashSet<>(),
    boundTags = new HashSet<>();
    private Point2D mapBounds;

    public static HashSet<String> getNodeTags() {
        return nodeTags;
    }

    public static HashSet<String> getWayTags() {
        return wayTags;
    }

    public static HashSet<String> getBoundTags() {
        return boundTags;
    }

    public Point2D getMapBounds() {
        return mapBounds;
    }

    public void setMapBounds(Point2D mapBounds) {
        this.mapBounds = mapBounds;
    }

    /**
     * Method called on each entity from original XML file
     * @param entityContainer incoming entity for procession
     * Method separates entities in different collections
     */
    public void process(EntityContainer entityContainer) {
        entityNum++;
        if (entityContainer instanceof BoundContainer) {
            MapObject temp;

            Bound bound = ((BoundContainer) entityContainer).getEntity();
            for(Tag t: bound.getTags()){
                boundTags.add(t.getKey() + ", " + t.getValue());
            }
        }else if (entityContainer instanceof NodeContainer) {
            MapObject temp;

            Node node = ((NodeContainer) entityContainer).getEntity();
            temp = new MapObject(node.getLatitude(), node.getLongitude(), node.getId(), node.getTags());
            MapObjects.put(temp.getID(), temp);
            OGraph.getInstance().nodesQuantity.put(temp.getID(), 0);

            // create node or add details if object was already created through ways (usually not the case, if .pbf file formatted correctly)
            if (MapObjects.get(node.getId()) != null) {
                temp = MapObjects.get(node.getId());
                temp.setLatitude(node.getLatitude());
                temp.setLongitude(node.getLongitude());
                temp.addAllTags(node.getTags());
            }
            else {
                temp = new MapObject(node.getLatitude(), node.getLongitude(), node.getId(), node.getTags());

                MapObjects.put(temp.getID(), temp);
            }

            for(Tag t: node.getTags()){

                nodeTags.add(t.getKey() + ", " + t.getValue());
            }

        // process your node //
        } else if (entityContainer instanceof WayContainer){
            Way way = ((WayContainer) entityContainer).getEntity();
            if(way.getId() == 551927065l ){
                boolean stop = true;
            }
            // you can filter ways/nodes //
            if (this.isAppropriate(way)) {
                OMapWay mway = new OMapWay(way.getId(), way.getTags());
                // process all nodes contained in the way //
                for(Tag t: way.getTags()){
                    if(t.getKey().equals("junction")){
                        WayNode first = way.getWayNodes().get(0);

                        for(WayNode wn: way.getWayNodes()){
                            junctions.put(wn.getNodeId(), first.getNodeId());
                        }
                        break;
                    }
                }
                for(WayNode wn: way.getWayNodes()) {
                    MapObject temp;
                    // if object was already created through nodes:
                    if (MapObjects.get(wn.getNodeId()) != null) {
                        temp = MapObjects.get(wn.getNodeId());
                    }
                    else {
                        temp = new MapObject(wn.getNodeId());
                        MapObjects.put(temp.getID(), temp);
                    }
                    mway.addObject(temp);

//                    Integer nodeNum = OGraph.getInstance().nodesQuantity.get(wn.getNodeId());
//                    OGraph.getInstance().nodesQuantity.put(wn.getNodeId(), nodeNum == null? 0 : nodeNum++);

                    // linkCounter of node counts on how many ways the node appears //
//                    if(nodesQuantity.containsKey())
                    temp.linkCounter++;
                    temp.addWay(mway);
                }
                this.ways.add(mway);
            }
            for(Tag t: way.getTags()){

                if(t.getKey().equals("junction")){
                    boolean stop = true;
                }
                wayTags.add(t.getKey() + ", " + t.getValue());
            }

        // process your way //
        } else if (entityContainer instanceof RelationContainer){
//            RelationContainer relationContainer = ((RelationContainer) entityContainer);
//            System.out.println(relationContainer.getEntity().toString());
        } else {
//            System.out.println("Unknown Entity!");
        }

    }

//    private boolean inBound(Double longitude, double latitude){
//        return longitude
//    }
    // following methods are a requirement of the Sink interface //
    @Override
    public void initialize(Map<String, Object> map) {
    }

    @Override
    public void complete() {

    }

    @Override
    public void release() {

    }

    /**
     * Checks, whether a way is appropriate for our graph
     * @param way from pbf file to check
     * @return true if
     * (1) way contains highway tag
     * (2) way not for only pedestrians, not a footway
     * (3) way should have a name (street name)
     */
    private boolean isAppropriate(Way way) {
        Boolean carAllowed = false;
        if(way.getId() == 787284934l){
            int a = 1;
        }
        for (Tag tag : way.getTags()) {
            if(this.irrelevantTags.contains(tag.getKey()) ){
                return false;
            }
            if (tag.getKey().equals("highway") ) {
                carAllowed =  !this.noVehicleValues.contains(tag.getValue());
            }
            // ... no need to iterate over all tags if both values already changed
            if (carAllowed != null) {
                break;
            }
        }
        return carAllowed;
    }

//    /**
//     * Checks, whether a way is appropriate for our graph
//     * @param way from pbf file to check
//     * @return true if
//     * (1) way contains highway tag
//     * (2) way not for only pedestrians, not a footway
//     * (3) way should have a name (street name)
//     */
//    private boolean isAppropriate(Way way) {
//        // use Object-Boolean variable to track, if value changed
//        Boolean carAllowed = null;
//        Boolean namedStreet = null;
//
//        for (Tag tag : way.getTags()) {
////            String a = tag.getKey();
////            System.out.println(a);
//            if (tag.getKey().equals("name")) {
//                namedStreet = true;
//            }
//            if (tag.getKey().equals("highway")) {
//                carAllowed = this.noVehicleValues.contains(tag.getValue()) ? false : true;
//            }
//            // ... no need to iterate over all tags if both values already changed
//            if (namedStreet != null && carAllowed != null) {
//                break;
//            }
//        }
//        // final decision:
//        carAllowed = carAllowed == null ? false : carAllowed;
//        namedStreet = namedStreet == null ? false : namedStreet;
//        return (carAllowed && namedStreet);
//    }

    /**
     * Values of tag 'highway' of ways in OSM where no cars are allowed
     * Feel free to fill the list: https://wiki.openstreetmap.org/wiki/Key:highway
     * TODO use more efficient collection
     * TODO check if links are useful
     */
    private final List<String> noVehicleValues = Arrays.asList( "motorway", "pedestrian", "footway", "bridleway", "steps", "path", "cycleway",
            "construction", "proposed", "bus_stop", "elevator", "street_lamp", "stop", "traffic_signals", "service", "track", "platform", "raceway",
            "abandoned", "road" , "escape" , "proposed" , "construction", "corridor", "bridleway" , "bus_guideway", "none", "motorway_link", "unclassified", "tertiary_link",
            "secondary_link", "construction", "service", "residential"
            ,"living_street", "tertiary"
    );
    // tags i pooled out "trunk",

    /**
     * other tags that irrelevant for us no matter what the value is
     *
     * Feel free to fill the list: https://wiki.openstreetmap.org/wiki/Key:highway
     * TODO use more efficient collection
     */
    public final List<String> irrelevantTags = Arrays.asList("barrier");



    /**
     * Check if a node from pbf file is appropriate for your purposes
     * TODO you can add required properties
     * @param node
     * @return
     */
    private boolean isAppropriate(Node node) {
        return false;
    }

    public static HashMap<Long, Long> getJunctions() {
        return junctions;
    }
}