package osmProcessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphUtils {
    private static GraphUtils instance = new GraphUtils();
    private List<OPath> paths;
    private Map<Long, ONode> riders;
    private OGraph graph;

    private GraphUtils() {
        paths = new ArrayList<>();
        riders = new HashMap<>();
        graph = OGraph.getInstance();
    }

    public static synchronized GraphUtils getInstance() {
        return instance;
    }

    /**
     * This function will be adressed by all distance calculations
     * between two nodes in space
     * Current calculation method: Haversine Method
     * @param lat1 start node latitude
     * @param lon1 start node longitude
     * @param lat2 end node latitude
     * @param lon2 end node longitude
     * @return calculated distance between two nodes
     */
    public Double getDistance(double lat1, double lon1, double lat2, double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }

    public boolean addPath(List<Object> pathNodes){
        return paths.add(new OPath(pathNodes));
    }

    public void setPaths(List<OPath> paths) {
        this.paths = paths;
    }

    public List<OPath> getPaths() {
        return paths;
    }

    public boolean setRiders(Map<Long, Double[]> Riders){
        Riders.entrySet().forEach(entry -> {
            ONode node = new ONode(entry.getKey(), entry.getValue(), ONode.userType.Rider);
            node.setUser(ONode.userType.Rider);
            riders.put(entry.getKey(), node);
        });

        return true;
    }

    public Map<Long, ONode> getRiders() {
        return riders;
    }
}
