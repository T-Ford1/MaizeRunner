package thread;

public abstract class WorkerThread extends Thread {

    protected int maxSleep;
    protected int defaultSleep;
    protected boolean updated;

    public WorkerThread(String name) {
        super(name);
        maxSleep = 15;
        defaultSleep = 5;
    }

    public final void run() {
        init();
        while (TimerMaster.running) {
            long time = System.currentTimeMillis();
            updateThread();
            time = System.currentTimeMillis() - time;
            try {
                if (!updated()) {
                    Thread.sleep(1);
                } else if (maxSleep - time > 0) {
                    Thread.sleep(maxSleep - time);
                }
            } catch (InterruptedException e) {
            }
        }
        endTask();
    }
    
    protected void increaseSpeed() {
        if(maxSleep > defaultSleep) maxSleep--;
        if(defaultSleep > 0) defaultSleep--;
    }

    protected abstract void init();

    protected abstract void updateThread();
    
    protected abstract void endTask();

    protected boolean updated() {
        return updated;
    }
}
