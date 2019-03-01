package cz.kul.snippets.concurrency.commons;

public class CommonRunnable implements Runnable {

    private static int SLEEP_INTERVAL = 10;

    private long livetime;
    private boolean interruptable;
    private Runnable task;

    volatile private Stat stat;

    public static CommonRunnable create(long livetime) {
        return new CommonRunnable(livetime, true, null);
    }

    public static CommonRunnable create(long livetime, Runnable task) {
        return new CommonRunnable(livetime, true, task);
    }

    public static CommonRunnable createInfinite() {
        return new CommonRunnable(0, true, null);
    }

    public static CommonRunnable createNotInterruptable(long livetime) {
        return new CommonRunnable(livetime, false, null);
    }

    public static CommonRunnable createNotInterruptable(long livetime, Runnable task) {
        return new CommonRunnable(livetime, false, task);
    }

    public static CommonRunnable createInterruptable(long livetime, Runnable task) {
        return new CommonRunnable(livetime, true, task);
    }

    public static CommonRunnable createInfiniteNotInterruptable() {
        return new CommonRunnable(0, false, null);
    }

    private CommonRunnable(long livetime, boolean interruptable, Runnable task) {
        this.livetime = livetime;
        this.interruptable = interruptable;
        this.task = task;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        long endTime = livetime > 0 ? startTime + livetime : 0;
        while (endTime == 0 || endTime > System.currentTimeMillis()) {
            if (interruptable && Thread.currentThread().interrupted()) {
                break;
            }
            if (task != null) {
                task.run();
            }
            try {
                Thread.sleep(SLEEP_INTERVAL);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        stat = new Stat();
        stat.interrupted = Thread.interrupted();
        stat.livetime = System.currentTimeMillis() - startTime;
        stat.daemon = Thread.currentThread().isDaemon();
    }

    public Stat getStat() {
        return stat;
    }

    public boolean isFinished() {
        return stat != null;
    }

    public static class Stat {
        volatile private boolean interrupted;
        volatile private long livetime;
        volatile private boolean daemon;

        public boolean isInterrupted() {
            return interrupted;
        }

        public long getLivetime() {
            return livetime;
        }

        public boolean isDaemon() {
            return daemon;
        }
    }
}
