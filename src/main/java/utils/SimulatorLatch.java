//package utils;
//
//import app.controller.Simulator;
//
//public class SimulatorLatch {
//    private boolean pause = false;
//
//    private SimulatorLatch(){}
//
//    public static SimulatorLatch INSTANCE = new SimulatorLatch();
//
//    public void waitIfPause(){
//        try {
//            if(pause){
//                this.wait();
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void pause(){
//        pause = true;
//    }
//
//    public void unpause(){
//        pause = false;
//        this.notifyAll();
//    }
//
//    public Boolean isPause() {
//        return pause;
//    }
//
//
//}
