package view;

import model.Drive;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RealTimeEvents implements Runnable{
    private List<Drive> events, startedEvents, eventsToSend;
    private final double timeSpeed;
    private Date currTime;

    public RealTimeEvents(List<Drive> events, double timeSpeed) {
        this.events = events;
        startedEvents = new ArrayList<>();
        eventsToSend = new ArrayList<>();
        currTime = events.get(0).getLeaveTime();
        this.timeSpeed = timeSpeed;
    }

    public List<Drive> getStartedEvents() {
        eventsToSend.clear();

        eventsToSend.addAll(startedEvents);

        startedEvents.clear();

        return eventsToSend;//TODO maybe make synchronized
    }

    @Override
    public void run() {
        while(!events.isEmpty()){
            // add first event in list to startedEvents
            Drive newEvent = events.remove(0);
            startedEvents.add(newEvent);

            //sleep till next events starts
//            sleep(currTime.getTime() - newEvent.getLeaveTime().getTime());
            sleep(currTime.compareTo(newEvent.getLeaveTime()));

            // jump in time to next event
            currTime = newEvent.getLeaveTime();
        }
    }

    private void sleep(long ms ) {
        try { Thread.sleep((long) (ms/timeSpeed)) ; }
        catch (InterruptedException e) { e.printStackTrace(); }
    }
}
