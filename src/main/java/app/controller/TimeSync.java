package app.controller;

import java.util.Date;
import java.util.HashSet;

public interface TimeSync {
    HashSet<TimeSync> threads = new HashSet<>();

    void setTime(Date date);

    Date getTime();

    default void register(TimeSync t) {
        synchronized (this){
            threads.add(t);
        }
    }

    default void unregister(TimeSync t) {
        synchronized (this){
            threads.remove(t);
        }
    }
}
