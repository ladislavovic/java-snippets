package cz.kul.snippets.concurrency.example10_volatile;


import org.junit.Assert;
import org.junit.Test;

/**
 * <ul>
 * <li>Volatile variables are always read and write to main memmory, not CPU</li>
 *
 * <li>If Thread A writes to a volatile variable and Thread B subsequently reads the same volatile
 * variable, then all variables visible to Thread A before writing the volatile variable,
 * will also be visible to Thread B after it has read the volatile variable.
 * </li>
 *
 * <li>If Thread A reads a volatile variable, then all all variables visible to Thread A when reading
 * the volatile variable will also be re-read from main memory.
 * </li>
 * </ul>
 */
public class TestVolatile {

    public static class Incrementor extends Thread {
        
        boolean keepRunning = true;
        volatile boolean keepRunningVolatile = true;
        boolean useVolatile;
        
        public Incrementor(boolean useVolatile) {
            this.useVolatile = useVolatile;
        }

        @Override
        public void run() {
            if (useVolatile) {
                volatileRun();
            } else {
                nonVolatileRun();
            }
        }

        public void volatileRun() {
            long counter = 0;
            while (keepRunningVolatile) {
                counter++;
            }
        }
        
        public void nonVolatileRun() {
            long counter = 0;
            while (keepRunning) {
                counter++;
            }
        }
        
        public void stopIt() {
            if (useVolatile) {
                keepRunningVolatile = false;
            } else {
                keepRunning = false;
            }
        }
        
    }
    
    @Test
    public void test() throws InterruptedException {
        Incrementor t1 = new Incrementor(false);
        t1.start();
        Thread.sleep(1000);
        t1.stopIt();
        Thread.sleep(1000);
        Assert.assertEquals(Thread.State.RUNNABLE, t1.getState()); 
        // Here the keepRunning is not volatile, so this thread set it in its CPU memmory,
        // but the t1 thread has still its own copy in its own CPU memmory and it still has
        // the false value.
        
        Incrementor t2 = new Incrementor(true);
        t2.start();
        Thread.sleep(1000);
        t2.stopIt();
        Thread.sleep(1000);
        Assert.assertEquals(Thread.State.TERMINATED, t2.getState());
        // Here the variable is volatile so it is always read and write from/to main memmory
        // so the change is visible to all threads so the thread is stopped
    }

}
