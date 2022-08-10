//package app.controller;
//
//import app.model.Node;
//
//import java.util.Iterator;
//import java.util.TimerTask;
//
//import static utils.Utils.lock;
//
//public class MoveCar extends TimerTask {
//    Node currNode, nextNode;
//
//    public MoveCar(Node currNode, Drive drive) {
//        this.currNode = currNode;
//    }
//
//    public void init(Node nextNode){
//        this.nextNode = nextNode;
//    }
//
//    @Override
//    public void run() {
//        Iterator<Node> nodeIter = getPath().iterator();
//        nextNode = nodeIter.next();
//
//        while(nodeIter.hasNext()){
//            currNode = nextNode;
//            nextNode = nodeIter.next();
//
//            Double timeToNextNode = currNode.getEdgeTo(nextNode).getWeight();
//
//            sleep(timeToNextNode);
//
//            originalTime -= timeToNextNode;
//
//            lock(false);
//
//            currNode = nextNode;
//
//        }
//    }
//}
