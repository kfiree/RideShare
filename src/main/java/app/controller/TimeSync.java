package app.controller;

import java.util.Date;
import java.util.HashSet;

import static utils.Utils.FORMAT;

public interface TimeSync {
    HashSet<TimeSync> threads = new HashSet<>();


    void setTime(Date date);

    default void addTime(long ms){
        setTime(new Date(getTime().getTime() + ms));
    }

    Date getTime();

    default void register(TimeSync t){//, Date date) {
//        t.setTime(date);
        synchronized (this){
            threads.add(t);
        }
    }

    default void unregister(TimeSync t) {
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
        for (TimeSync t: threads) {
            System.out.println(t + " : " + FORMAT(t.getTime()));

        }
    }
}
