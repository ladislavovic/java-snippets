package cz.kul.snippets.concurrency.example09_ThreadLocal;

import org.junit.Assert;
import org.junit.Test;

public class TestThreadLocal {

    @Test
    public void eachThreadHasItsOwnValue() throws InterruptedException {
        ThreadLocal<Long> threadLocal = new ThreadLocal<>();
        Runnable r1 = () -> {
            Long threadId = Thread.currentThread().getId();
            threadLocal.set(threadId);
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
                Assert.assertEquals(threadId, threadLocal.get());
            }
        };
        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(r1);
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }
    }




}
