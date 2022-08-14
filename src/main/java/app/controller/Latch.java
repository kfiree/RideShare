package app.controller;

public class Latch {
    public static volatile boolean lock = true;

    private Latch() {}

    public static final Latch INSTANCE = new Latch();

    synchronized public void lock(){
        System.out.println("================== lock ==================");
        lock = true;
    }

    synchronized public void resume(){
        System.out.println("================== resume ==================");
        lock = false;

        notifyAll();
    }

    synchronized public void waitOnCondition(){
        if(lock){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
