package cz.kul.snippets.concurrency.example_02_sleep;

import cz.kul.snippets.SnippetsTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * You can sleep a thread for some time. The execution of the thread
 * is paused for some time.
 * 
 * The thread does not lose any monitor.
 * 
 * If the thread is interrupted during sleeping, an InterruptedException is
 * thrown.
 *
 * @author kulhalad
 * @since 7.4
 *
 */
public class TestSleep extends SnippetsTest {

    private long ONE_SECOND_IN_MILLS = 1_000;

    @Test
    public void testSleep() throws InterruptedException {
        long start = System.nanoTime() / 1000;
        Thread.currentThread().sleep(ONE_SECOND_IN_MILLS);
        long end = System.nanoTime() / 1000;
        Assert.assertTrue(end - start >= ONE_SECOND_IN_MILLS);
    }

    @Test
    public void testInterruptedException() throws InterruptedException {
        Sleeping sleeping = new Sleeping();
        Thread thread = new Thread(sleeping);
        thread.start();

        // TODO do the synchronization better
        Thread.sleep(100);
        thread.interrupt();
        Thread.sleep(100);

        Assert.assertNotNull(sleeping.exception);
    }

    public static class Sleeping implements Runnable {

        InterruptedException exception;

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                exception = e;
            }
        }

    }

}
