package view;

import model.Drive;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import static controller.utils.LogHandler.log;

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
        System.out.println("RealTimeEvents star running");
        log(Level.FINER, "RealTimeEvents star running");

        while(!events.isEmpty()){

            // add first event in list to startedEvents
            Drive newDrive = events.remove(0);
            newDrive.setTimeSpeed(timeSpeed);
            Thread driveThread = new Thread(newDrive);
            driveThread.start();

            startedEvents.add(newDrive);


            int sleepTime = currTime.compareTo(newDrive.getLeaveTime());
            System.out.println("RealTimeEvents sleep for "+  sleepTime/timeSpeed + " time speed = "+ timeSpeed);
            sleep(sleepTime);

            // jump in time to next event
            currTime = newDrive.getLeaveTime();
            log(Level.FINER, "RealTimeEvents add event.");
        }
    }

    private void sleep(long ms ) {
//        double sleepTime = ms / timeSpeed;
        double sleepTime = 3000;
        try { Thread.sleep((long) (3000)) ; }
        catch (InterruptedException e) { e.printStackTrace(); }
    }
}
