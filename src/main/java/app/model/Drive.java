package app.model;

/* third party */
import java.util.*;

import app.model.interfaces.ElementsOnMap;
import app.model.interfaces.Located;
import app.view.MapView;
import org.jetbrains.annotations.NotNull;

/* local */
import app.controller.GraphAlgo;

/* static imports */
import static app.controller.RoadMapUtils.getShortestDist;
import static utils.LogHandler.LOGGER;
import static utils.Utils.*;


/*
 * todo make long way with stop-point instead of paths
 *      make drives thread-pool
 */
public class Drive implements Runnable, ElementsOnMap, Located {
    private static final int MAX_PASSENGERS_NUM = 2; //todo set to 3
    private final String type, id;  //todo is type needed?
    private final PriorityQueue<Rider> passengers;
    private final HashMap<Node, ElementsOnMap> stops = new HashMap<>();
    private final Date leaveTime;
    private Path path;
    private Node currNode, destination;
    private double totalTime, detoursTime;
    private boolean pathChange;

    /** CONSTRUCTORS */

    private Drive(String type, String id, Date leaveTime) {
        this.passengers = new PriorityQueue<>(Comparator.comparingDouble(rider -> getNextPassengerStop(rider).distanceTo(currNode)));
        this.type = type;
        this.id = "D" + id;
        this.leaveTime = leaveTime;
    }

    public Drive(String id, @NotNull Path path, String type, Date leaveTime) {
        this(type, id, leaveTime);
        initPathVariables(path);
    }

    public Drive(@NotNull Node src, @NotNull Node dst, String type, String id, Date leaveTime) {
        this(type, id, leaveTime);
        initPathVariables(Objects.requireNonNull(GraphAlgo.getShortestPath(src, dst)));
    }

    public void initPathVariables(@NotNull Path path) {
        setPath(path);
        currNode = path.getSrc();
        destination = path.getDest();
        totalTime = path.getWeight();

        addStop(destination, this);
    }



    /** RUN */

    @Override
    public void run() {
        LOGGER.fine("Drive "+ id +" started.");
        validate(getPath() != null, "can't start a drive if path is null. Drive owner id - " + id);

        while(currNode!= getDest()){
            driveToNextStop();
            setNextStop();
        }

        UserMap.INSTANCE.finished(this);

        LOGGER.finest("Drive "+ id +" finished!.");
    }

    private void driveToNextStop(){
        pathChange = false;
        int nodeIndex = 0;

        Iterator<Node> nodeIter = getPath().iterator();
        Node nextNode = nodeIter.next();

        while(nodeIter.hasNext()){
            nodeIndex++;
            currNode = nextNode;
            nextNode = nodeIter.next();

            if(nodeIndex % 5 ==0) {
                LOGGER.info("driver (" + getId() + ") " + FORMAT( nodeIndex / (double) getPath().getSize() * 100) + "% complete.");
            }

            Double timeToNextNode = currNode.getEdgeTo(nextNode).getWeight();

            sleep(timeToNextNode);

            totalTime -= timeToNextNode;

//            System.out.println("curr " + currNode.getOsmID() + " ,  nextNode " + nextNode.getOsmID());
            /* pick passenger on the way */
            if(currNode.getOsmID() == 1154683176l){
                System.out.println("close");
            }
            if(currNode.getOsmID() == 563938249l){
                System.out.println("at stop");
            }
            if(pathChange || currNode == this.destination){
                return;
            }

            if(AtStop()){
                nextStop(stops.remove(currNode));
                ElementsOnMap remove = stops.remove(currNode);
                if(remove instanceof Rider passenger){
                    if(passenger.isPickedUp()){
                        passengers.remove(passenger);
                        System.out.println("just droped "+ passenger.getId());
                        setNextStop();
                    }else{
                        addStop(passenger.getDest(), passenger);
                        UserMap.INSTANCE.finished(passenger);
                    }
                    System.out.println("reached " + passenger.getId());
                    setNextStop();
                }
            }

            lock(false);

            currNode = nextNode;

        }
    }
    private void nextStop(ElementsOnMap element){
        //todo continue here
    }

    private void nextStop(Node node){
        ElementsOnMap element = stops.remove(node);
        if(element instanceof Rider passenger) {
            stops.remove(passenger.getNextStop());
            if (!passenger.isPickedUp()) {
                addStop(element.getDest(), element);
                UserMap.INSTANCE.finished(element);
            } else {
                passengers.remove(element);
            }
        }else{

        }
        setNextStop();
    }



    private boolean AtStop(){
        return stops.containsKey(currNode);
    }

    /**
     * todo improve detour time heuristic
     *
     *
     * @param rider
     */
    public void addDetour(Rider rider){
        /* extend path to pick up pedestrian */
        passengers.add(rider);

        setNextStop();

        detourHeuristic(rider);

        addStop(rider.getCurrentNode(), rider);
        addStop(rider.getDest(), rider);

        if(passengers.peek() == rider){
            setPath(getCurrentNode(), rider.getCurrentNode());
            changePath(rider);
        }
    }

    private void changePath(Rider rider){

    }
    // 2 סוגים של הוצאה אחד כשמגיעים לנקודה ואחד כשמוסיפים עיקוף למסלול
    private void setNextStop(){
        if(!passengers.isEmpty()) {
            Rider nextStop = passengers.poll();
            stops.remove(nextStop.getNextStop());
            for(Map.Entry<Node, ElementsOnMap> stopEntry : stops.entrySet()){
                if(stopEntry.getValue() == nextStop){
                    setPath(currNode, nextStop.getNextStop());
                    this.pathChange = true;
                    stops.remove(stopEntry.getKey());
                    break;
                }
            }
//            if()
            // todo loop it
            if (!nextStop.isPickedUp()) {
                addStop(nextStop.getDest(), nextStop);
            }

            setPath(currNode, nextStop.getNextStop());
            this.pathChange = true;
        }
    }

    private void addStop(Node stop, ElementsOnMap type){
        stops.put(stop, type);
    }



    private void detourHeuristic(Rider rider){

        double passengerDestToPathDist =  getShortestDist(rider.getDest(), stops.keySet(), currNode),
         passengerSrcToPathDist = getShortestDist(rider.getCurrentNode(), stops.keySet(), currNode);

        detoursTime += passengerSrcToPathDist + passengerDestToPathDist;
    }

    /* TODO add multiple middle paths (TSP):
        public void addDetour(Path currPath) {
            this.currPath = currPath;
        }
    */
    private void sleep(double sleepTime ) {
        if(sleepTime <1 ){
            LOGGER.warning("drive " + getId()+" sleep time "+ FORMAT(sleepTime) +" seconds is too small.");
        }

        try {
            Thread.sleep( (long) (sleepTime * 1000 / MapView.simulatorSpeed));
        } catch (InterruptedException e) {
            LOGGER.severe(e.getMessage() +"\n"+ Arrays.toString(e.getStackTrace()));
        }
    }



    /** GETTERS */

    public double getDetoursTime() {
        return detoursTime;
    }

    private Node getNextPassengerStop(Rider rider){
        return rider.isPickedUp()? rider.getDest() : rider.getCurrentNode();
    }

    public boolean canSqueezeOneMore(){
        return passengers.size() < MAX_PASSENGERS_NUM;
    }

    public double getTotalTime() {
        return totalTime;
    }

    @Override
    public GeoLocation getLocation(){
        return currNode.getLocation();
    }

    @Override
    public Node getDest() {
        return this.destination;
//        return paths.get(paths.size() -1).getDest(); // TODO check if last
    }

    @Override
    public Node getCurrentNode() {
        return currNode;
    }

    public String getType() {
        return type;
    }

    @Override
    public String getId() {
        return id;
    }

    public Queue<Rider> getPassengers() {
        return passengers;
    }

    @Override
    public Date getStartTime() {
        return leaveTime;
    }

    public Path getPath(){
        return path;
    }

    public void setPath(Path path) {
        System.out.println(path + " path changed " + this.path);
        this.path = path;
    }

    public void setPath(Node src, Node dst) {
        setPath(GraphAlgo.getShortestPath(src, dst));
    }

    public List<Node> getNodes(){
        return getPath().getNodes();
    }


    @Override
    public String toString() {
        return "Drive{" +
                "id='" + id.substring(1) + '\'' +
                ", long id='" + id + '\'' +
                ", passengers=" + passengers.size() +
                ", leaveTime=" + FORMAT(leaveTime) +
                ", pathSize=" + getPath().getSize() +
                ", currNode=" + currNode.getId() +
                '}';
    }
}