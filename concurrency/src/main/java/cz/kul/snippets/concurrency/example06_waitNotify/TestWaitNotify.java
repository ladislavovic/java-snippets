package cz.kul.snippets.concurrency.example06_waitNotify;

import cz.kul.snippets.concurrency.commons.CommonRunnable;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Generally, the wait() and notify() methods are designed to provide a mechanism to allow a thread to block until a specific condition is met.
 *
 * <h3>monitor</h3>
 * <ul>
 *     <li>A monitor is essentially a Java object that is “owned” by one thread at a time and is used to enforce mutual exclusion in blocks of code.</li>
 *     <li>It is a java object from synchronized clause: synchronized (monitor) {...}</li>
 *     <li>The thread owning the monitor is running, other threads are waiting</li>
 *     <li>It is used for thread synchronization</li>
 * </ul>
 *
 * <h3>wait()</h3>
 * <ul>
 *     <li>Cause the thread release the monitor and switch to the waiting state until it is notified</li>
 *     <li>Must be called in synchronized block</li>
 *     <li>wait() method release the monitor</li>
 *     <li>so the method calling it must own the monitor. So it is always called in the synchronized block</li>
 *     <li>The thread calling it is called "waiting" thread, because it waits unit another thread finish its work</li>
 *     <li>
 *         wait() must be in the loop! It is because "spurious wakeup". Simply the
 *         thread can be awaken even though the thread is not notified or interrupted. It
 *         is a "feature" of scheduler api, it just sometimes happens. I do not know
 *         details. So the wait must be in the loop and check the condition,
 *         if the thread should be really awakened.
 *     </li>
 * </ul>
 *
 * <h3>notify() / notifyAll()</h3>
 * <ul>
 *     <li>Must be called in the synchronized block</li>
 *     <li>It notifies one thread (or all threads if you call nofityAll()) waiting on the monitor.</li>
 *     <li>It does not release the monitor immediately. It completes the synchronized block.</li>
 * </ul>
 *
 */
public class TestWaitNotify {

    @Test
    public void waitRelinquishTheLockAndMakeTheCurrentThreadWaiting() throws InterruptedException
    {
        Object monitor = new Object();
        var t1 = new Thread(() -> {
            synchronized (monitor) {
                // here the thread owns the monitor
                try {
                    monitor.wait(); // here the thread relinquish the monitor and switch to the WAITING state
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting for monitor", e);
                }
            }
        });
        t1.setDaemon(true);

        t1.start();
        Thread.sleep(100);

        assertEquals(Thread.State.WAITING, t1.getState());
    }

    @Test
    public void notifyingTheWaitingThread() throws InterruptedException
    {
        var events = new ArrayList<String>();

        Object monitor = new Object();

        var waitingThread = new Thread(() -> {
            synchronized (monitor) {
                try {
                    events.add("before-wait");
                    monitor.wait(); // here the thread relinquish the monitor and switch to the WAITING state
                    events.add("after-wait");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting for monitor", e);
                }
            }
        });
        waitingThread.setDaemon(true);
        waitingThread.start();

        Thread.sleep(100);

        var workingThread = new Thread(() -> {
            synchronized (monitor) {
                monitor.notify();
                events.add("after-notify");
            }
        });
        workingThread.setDaemon(true);
        workingThread.start();

        Thread.sleep(100);

        assertEquals(
            List.of(
                "before-wait",
                "after-notify",
                "after-wait"
            ),
            events
        );
    }



    // todo: wait notify example - implement connection pool - 2 connections, 10 threads, test that max two threads communicate at the given time
    // todo: create deadlock by wait / notify


    @Test
    public void test() throws InterruptedException {
        Object monitor = new Object();
        StringBuilder buffer = new StringBuilder();

        Thread t1 = new Thread(CommonRunnable.createNotInterruptable(0, () -> {
            synchronized (monitor) {
                mainLoop:
                while (buffer.length() < 10) {
                    waitLoop:
                    while (buffer.length() != 0 && buffer.charAt(buffer.length() - 1) != '3') {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            break mainLoop;
                        }
                    }
                    if (buffer.length() < 10) {
                        buffer.append("1");
                    }
                    monitor.notifyAll();
                }
            }
        }));

        Thread t2 = new Thread(CommonRunnable.createNotInterruptable(0, () -> {
            synchronized (monitor) {
                mainLoop:
                while (buffer.length() < 10) {
                    waitLoop:
                    while (buffer.length() == 0 || buffer.charAt(buffer.length() - 1) != '1') {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            break mainLoop;
                        }
                    }
                    if (buffer.length() < 10) {
                        buffer.append("2");
                    }
                    monitor.notifyAll();
                }
            }
        }));

        Thread t3 = new Thread(CommonRunnable.createNotInterruptable(0, () -> {
            synchronized (monitor) {
                mainLoop:
                while (buffer.length() < 10) {
                    waitLoop:
                    while (buffer.length() == 0 || buffer.charAt(buffer.length() - 1) != '2') {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            break mainLoop;
                        }
                    }
                    if (buffer.length() < 10) {
                        buffer.append("3");
                    }
                    monitor.notifyAll();
                }
            }
        }));

        t1.start();
        t2.start();
        t3.start();
        Thread.sleep(2000);
        assertEquals("1231231231", buffer.toString());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void waitCanBeCalledOnlyOnHoldedMonitor() throws InterruptedException {
        String s = "foo";
        s.wait();
    }

}
