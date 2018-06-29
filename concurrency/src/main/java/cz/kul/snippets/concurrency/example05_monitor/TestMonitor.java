package cz.kul.snippets.concurrency.example05_monitor;

import cz.kul.snippets.concurrency.commons.CommonRunnable;
import org.junit.Assert;
import org.junit.Test;

/**
 * Basic way how to synchronize concurrent threads in Java are "monitors".
 *
 * Only the thread which holds the monitor can run. Only one thread at time can hold the monitor.
 *
 * Synchronized method is a shortcut for synchronized (this) { ...
 *
 * Synchronized static method is a shortcut for synchronized (Foo.class) { ...
 * 
 */
public class TestMonitor {

    @Test
    public void testNotSynchronized() throws InterruptedException {
        ThreadCounter threadCounter = new ThreadCounter();
        for (int i = 0; i < 10; i++) {
            CommonRunnable r = CommonRunnable.createNotInterruptable(3000, () -> {
                threadCounter.method();
            });
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.start();
        }
        Thread.sleep(3000);
        Assert.assertTrue(threadCounter.getMax() > 1);
    }

    @Test
    public void testSynchronized() throws InterruptedException {
        ThreadCounter threadCounter = new ThreadCounter();
        for (int i = 0; i < 10; i++) {
            CommonRunnable r = CommonRunnable.createNotInterruptable(3000, () -> {
                threadCounter.synchronizedMethod();
            });
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.start();
        }
        Thread.sleep(3000);
        Assert.assertTrue(threadCounter.getMax() == 1);
    }

    static class ThreadCounter {
        private volatile int current;
        private volatile int max;

        void method() {
            current++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            if (current > max) {
                max = current;
            }
            current--;
        }

        synchronized void synchronizedMethod() {
            method();
        }

        int getMax() {
            return max;
        }
    }

}
