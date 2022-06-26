package view;

import model.Drive;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static controller.utils.LogHandler.LOGGER;


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
        LOGGER.fine("RealTimeEvents star running");

        while(!events.isEmpty()){

            // add first event in list to startedEvents
            Drive newDrive = events.remove(0);
            newDrive.setTimeSpeed(timeSpeed);
            Thread driveThread = new Thread(newDrive);
            driveThread.start();

            startedEvents.add(newDrive);


            int sleepTime = currTime.compareTo(newDrive.getLeaveTime());

            sleep(sleepTime);

            // jump in time to next event
            currTime = newDrive.getLeaveTime();
            LOGGER.info("RealTimeEvents add event. sleep time = " +sleepTime);
        }
    }

    private void sleep(long ms ) {
//        double sleepTime = ms / timeSpeed;
        double sleepTime = 3000;
        try { Thread.sleep((long) (3000)) ; }
        catch (InterruptedException e) { e.printStackTrace(); }
    }
}
