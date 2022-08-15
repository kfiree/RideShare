package utils.DS;

public class Latch {
    public static volatile boolean lock = true;

    public Latch() {}

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
