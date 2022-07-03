package controller.osm_processing;

import controller.utils.MapUtils;
import model.RoadMap;
import model.Node;

import java.util.*;

/**
 *      |==================================|
 *      |==========| OSM PARSER  |=========|
 *      |==================================|
 *
 *  parse 'OsmWay' and 'OsmObject' to 'RoadMap.java'
 *
 * @author  Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 1.0
 * @since   2021-06-20
 */
public final class Parser {
    private final Map<OsmObject, Map<OsmObject, OsmWay>> AdjacentMap = new HashMap<>();

    public void parseMapWays(ArrayList<OsmWay> ways){
        initAdjacentMap(ways);

        buildRoadMap();
    }

    private void buildRoadMap(){
        RoadMap roadMap = RoadMap.INSTANCE;

        AdjacentMap.forEach((srcObj, adjacentMap)-> adjacentMap.forEach((dstObj, way)->{
            Node src = roadMap.addNode(srcObj);
            Node dst = roadMap.addNode(dstObj);
            roadMap.addEdge(src, dst, way);
        }));

    }

    private void initAdjacentMap(ArrayList<OsmWay> ways){
        for (OsmWay way: ways) {

            List<OsmObject> objectsOnWay = way.getObjectsOnWay();
            OsmObject srcObj , dstObj;

            if (objectsOnWay.size() >= 2) {

                boolean directed = isDirected(way);

                srcObj = objectsOnWay.get(0);
                for (int i = 1; i < objectsOnWay.size()-1; i++) {

                    dstObj = objectsOnWay.get(i);

                    if(dstObj.isPartOfAnotherWay()){
                        addWay(srcObj, dstObj, way, directed);
                        srcObj = dstObj;
                    }
                }
                addWay(srcObj,  objectsOnWay.get(objectsOnWay.size() -1), way, directed); //todo check its last
            }
        }
    }

    private boolean isDirected(OsmWay way){
        String oneWay = way.getTags().get("oneWay");
        return oneWay != null && oneWay.equals("yes");
    }

    private void addWay(OsmObject first, OsmObject last, OsmWay way, boolean directed){
        MapUtils.validate(first != null && last != null, "can't create edge because edge's node is null. node1 - "+ first + ", node2 - "+ last+".");
        addDirectedWay(first, last, way);
        if(!directed){
            addDirectedWay(last, first, way);
        }
    }

    private void addDirectedWay(OsmObject src, OsmObject dst, OsmWay way){
        Map<OsmObject, OsmWay> srcMap = AdjacentMap.get(src);

        if(srcMap == null){
            srcMap = new HashMap<>();
            srcMap.put(dst, way);
            AdjacentMap.put(src,srcMap);
        }else if(!srcMap.containsKey(dst)){
            srcMap.put(dst, way);
        }
    }
}
