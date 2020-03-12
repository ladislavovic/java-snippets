package cz.kul.snippets.concurrency.example10_volatile;

import org.junit.Assert;
import org.junit.Test;

public class TestVolatilePerformance {

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
            long endTime = System.currentTimeMillis() + 1000;
            long counter = 0;
            while (keepRunningVolatile) {
                counter++;
                if (endTime < System.currentTimeMillis()) {
                    break;
                }
            }
            System.out.println("Counter end value (volatile ON) : " + counter);
        }

        public void nonVolatileRun() {
            long endTime = System.currentTimeMillis() + 1000;
            long counter = 0;
            while (keepRunning) {
                counter++;
                if (endTime < System.currentTimeMillis()) {
                    break;
                }
            }
            System.out.println("Counter end value (volatile OFF): " + counter);
        }

    }

    /**
     * This test does not prove volatile has performance impact. But it has to have :)
     */
    @Test
    public void performance() throws InterruptedException {
        Incrementor t1 = new Incrementor(true);
        t1.start();
        Thread.sleep(2000);

        Incrementor t2 = new Incrementor(false);
        t2.start();
        Thread.sleep(2000);
    }
    
}
