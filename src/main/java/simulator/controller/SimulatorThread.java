package simulator.controller;

import utils.Utils;

import java.util.Date;
import java.util.HashSet;

import static utils.Utils.FORMAT;

public interface SimulatorThread {
    HashSet<SimulatorThread> threads = new HashSet<>(), sleeping = new HashSet<>();


    void setTime(Date date);

    default void addTime(long ms){
        setTime(new Date(getTime().getTime() + ms));
    }

    Date getTime();

    default void register(SimulatorThread t){
        synchronized (this){
            threads.add(t);
        }
    }

    default void unregister(SimulatorThread t) {
        synchronized (this){
            threads.remove(t);
        }
    }

    default void sleep(long ms){
        try {
            Thread.sleep((long) ( ms / Simulator.INSTANCE.speed()) );
            addTime(ms);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void showThreadsData(){
        for (SimulatorThread t: threads) {
            System.out.println(t + " : " + Utils.FORMAT(t.getTime()));

        }
    }
}
