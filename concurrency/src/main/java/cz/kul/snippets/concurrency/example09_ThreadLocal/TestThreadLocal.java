package cz.kul.snippets.concurrency.example09_ThreadLocal;

import cz.kul.snippets.concurrency.commons.CommonRunnable;
import cz.kul.snippets.ThreadUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class TestThreadLocal {

    @Test
    public void onlyOneThreadLocalInstance() throws InterruptedException, ReflectiveOperationException {
        // There is only one ThreadLocal instance for N Threads. One ThreadLocal instance contains values for
        // N threads. So relation between ThredLocal and Thread is 1:N. 
        // So ThreadLocal is usually static field or field in singleton instance.

        ThreadLocal<Long> threadLocal = new ThreadLocal<>();
        CommonRunnable r1 = CommonRunnable.createInterruptable(-1, () -> {
            if (threadLocal.get() == null) {
                threadLocal.set(Thread.currentThread().getId());
            }
        });

        List<Thread> threads = Arrays.asList(new Thread(r1), new Thread(r1), new Thread(r1));
        threads.forEach(t -> t.start());
        
        ThreadUtils.sleepOneSecond();

        for (Thread thread : threads) {
            Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
            threadLocalsField.setAccessible(true);
            Object threadLocals = threadLocalsField.get(thread);

            Class threadLocalMapClass = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
            Field tableField = threadLocalMapClass.getDeclaredField("table");
            tableField.setAccessible(true);
            WeakReference<ThreadLocal<?>>[] table = (WeakReference<ThreadLocal<?>>[]) tableField.get(threadLocals);
            
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null) {
                    ThreadLocal<?> threadLocalInThread = table[i].get();
                    Assert.assertTrue(threadLocalInThread == threadLocal);
                }
            }
        }

        threads.forEach(t -> t.interrupt());
        ThreadUtils.join(threads);
    }
    
    @Test
    public void eachThreadHasItsOwnValue() throws InterruptedException {
        ThreadLocal<Long> threadLocal = new ThreadLocal<>();
        Runnable r1 = () -> {
            Long threadId = Thread.currentThread().getId();
            threadLocal.set(threadId);
            for (int i = 0; i < 100; i++) {
                ThreadUtils.sleep(10);
                Assert.assertEquals(threadId, threadLocal.get());
            }
        };

        List<Thread> threads = Arrays.asList(new Thread(r1), new Thread(r1), new Thread(r1));
        threads.forEach(t -> t.start());
        for (Thread thread : threads) {
            thread.join();
        }
    }
    
    @Test
    public void howToCleanThreadLocalValuesForAllThreads() throws ReflectiveOperationException {
        Thread thread = Thread.currentThread();
        
        // Set thread local value for current thread
        WeakReference<ThreadLocal> threadLocal = new WeakReference<>(new ThreadLocal<>());
        threadLocal.get().set("foo");
        
        // Get ThreadLocalMap
        Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
        threadLocalsField.setAccessible(true);
        WeakReference<Object> threadLocalMap = new WeakReference<>(threadLocalsField.get(thread));
        Assert.assertNotNull(threadLocalMap.get());
        Assert.assertNotNull(threadLocal.get());

        // Set ThreadLocalMap to null, GC do the rest
        threadLocalsField.set(Thread.currentThread(), null);
        System.gc();
        Assert.assertNull(threadLocalMap.get());
        Assert.assertNull(threadLocal.get());
    }
    
}
