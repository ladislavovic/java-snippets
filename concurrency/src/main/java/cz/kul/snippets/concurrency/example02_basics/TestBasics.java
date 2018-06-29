package cz.kul.snippets.concurrency.example02_basics;

import cz.kul.snippets.SnippetsTest;
import cz.kul.snippets.concurrency.commons.CommonRunnable;
import cz.kul.snippets.concurrency.commons.ThreadUtils;
import org.junit.Assert;
import org.junit.Test;

public class TestBasics extends SnippetsTest {

    private long ONE_SECOND_IN_MILLS = 1_000;

    /**
     * You can sleep a thread for some time. The execution of the thread
     * is paused for some time.
     * <p>
     * The thread does not lose any monitor.
     * <p>
     * If the thread is interrupted during sleeping, an InterruptedException is
     * thrown.
     */
    @Test
    public void testSleep_threadShouldPause() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread.currentThread().sleep(ONE_SECOND_IN_MILLS);
        long end = System.currentTimeMillis();
        Assert.assertTrue(end - start >= ONE_SECOND_IN_MILLS);
    }

    @Test
    public void testThreadStates() throws InterruptedException {
        {
            // state NEW
            Thread t = new Thread(() -> {
            });
            Assert.assertEquals(Thread.State.NEW, t.getState());
        }

        {
            // state RUNNABLE
            // it is a thread executing in VM. Even though it is sleeping.
            Thread t = new Thread(() -> {
                ThreadUtils.sleep(1000);
            });
            t.start();
            Assert.assertEquals(Thread.State.RUNNABLE, t.getState());
        }

        {
            // state BLOCKED
            // thread which is waiting for a monitor
            Object monitor = new Object();
            Runnable r = () -> {
                synchronized (monitor) {
                    ThreadUtils.sleep(1000);
                }
            };
            Thread t1 = new Thread(r);
            t1.start();

            Thread t2 = new Thread(r);
            t2.start();

            ThreadUtils.sleep(100);

            Assert.assertTrue(t1.getState() == Thread.State.BLOCKED || t2.getState() == Thread.State.BLOCKED);
        }

        {
            // state WAITING
            // if the thread release a monitor and wait
            Object monitor = new Object();
            Thread t = new Thread(() -> {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.setDaemon(true);
            t.start();
            ThreadUtils.sleep(100);
            Assert.assertEquals(Thread.State.WAITING, t.getState());
        }

        {
            // state TERMINATED
            Thread t = new Thread(() -> {
            });
            t.start();
            ThreadUtils.sleep(100);
            Assert.assertEquals(Thread.State.TERMINATED, t.getState());
        }
    }

    @Test
    public void testAlive() {
        // A thread is alive if it is not NEW or TERMINATED
        Thread t1 = new Thread(() -> {
            ThreadUtils.sleep(1000);
        });
        Assert.assertFalse(t1.isAlive());

        t1.start();
        ThreadUtils.sleep(100);
        Assert.assertTrue(t1.isAlive());

        ThreadUtils.sleep(1500);
        Assert.assertFalse(t1.isAlive());
    }

    @Test
    public void testDaemon() {
        Thread t1 = new Thread(() -> {
            ThreadUtils.sleep(20000);
        });
        t1.setDaemon(false);
        t1.start();
    }

    @Test
    public void testPriority_prioritiesAreFromOneToTen() {
        Assert.assertEquals(1, Thread.MIN_PRIORITY);
        Assert.assertEquals(5, Thread.NORM_PRIORITY);
        Assert.assertEquals(10, Thread.MAX_PRIORITY);
    }

    @Test
    public void testPriority_defaultThreadPriorityIsNORM_PRIORITY() {
        Assert.assertEquals(Thread.NORM_PRIORITY, Thread.currentThread().getPriority());
    }

    @Test
    public void testPriority_defaultGroupMaxPriorityIsMAX_PRIORITY() {
        Assert.assertEquals(Thread.MAX_PRIORITY, Thread.currentThread().getThreadGroup().getMaxPriority());
    }

    @Test
    public void testPriority() throws InterruptedException {
        // TODO this test does not work
        StringBuilder agent1 = new StringBuilder();
        StringBuilder agent2 = new StringBuilder();
        StringBuilder agent3 = new StringBuilder();

        Object monitor = new Object();

        Thread t1 = new Thread(createAppendingRunnable(agent1, monitor));
        Thread t2 = new Thread(createAppendingRunnable(agent2, monitor));
        Thread t3 = new Thread(createAppendingRunnable(agent3, monitor));
        t3.setPriority(Thread.MAX_PRIORITY);

        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();

        testOut("agent1 length: %d", agent1.length());
        testOut("agent2 length: %d", agent2.length());
        testOut("agent3 length: %d", agent3.length());

        Assert.assertTrue(agent1.length() < agent2.length());
    }

    private CommonRunnable createAppendingRunnable(StringBuilder agent, Object monitor) {
        CommonRunnable r = CommonRunnable.create(10000, () -> {
            synchronized (monitor) {
                agent.append("a");
                monitor.notifyAll();
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return r;
    }

}
