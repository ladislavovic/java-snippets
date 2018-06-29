package cz.kul.snippets.concurrency.example06_waitNotify;

import cz.kul.snippets.concurrency.commons.CommonRunnable;
import org.junit.Assert;
import org.junit.Test;

/**
 * wait() method release the monitor and cause another method to get it
 *
 * notifyAll() method "wake up" all threads, which waits on the monitor.
 *
 * Wait and notifyAll must be called on the monitory only! You can not
 * call them on whatever object.
 *
 * wait() must be in the loop! It is because "spurious wakeup". Simply the
 * thread can be awaken even though the thread is not notified, interrupted. It
 * is "feature" of scheduler api, it just sometimes happeden. I do not know
 * details. So the wait must be in the loop and check the condition,
 * if the thread should be really awakened.
 */
public class TestWaitNotify {

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
        Assert.assertEquals("1231231231", buffer.toString());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void waitCanBeCalledOnlyOnHoldedMonitor() throws InterruptedException {
        String s = "foo";
        s.wait();
    }

}
