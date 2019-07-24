package cz.kul.snippets.concurrency.example08_Executor;

import cz.kul.snippets.ThreadUtils;
import cz.kul.snippets.concurrency.commons.CommonRunnable;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestExecutor {

    @Test
    public void threadPoolExecutor() {
        // Create the executer by this static factory method. It is prefered way over
        // use constructor directly.
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        assertEquals(2, executor.getCorePoolSize());
        assertEquals(0, executor.getActiveCount());

        // now you can execute tasks
        CommonRunnable task1 = CommonRunnable.create(100);
        CommonRunnable task2 = CommonRunnable.create(100);
        CommonRunnable task3 = CommonRunnable.create(100);
        executor.execute(task1);
        executor.execute(task2);
        executor.execute(task3);
        assertEquals(3, executor.getTaskCount());

        // the pool has two threads so two tasks are done simultaneously
        ThreadUtils.sleep(50);
        assertEquals(2, executor.getActiveCount());
        assertEquals(0, executor.getCompletedTaskCount());
        assertEquals(3, executor.getTaskCount());

        ThreadUtils.sleep(300);
        assertEquals(0, executor.getActiveCount());
        assertEquals(3, executor.getCompletedTaskCount());
        assertEquals(3, executor.getTaskCount());
    }
    
    @Test
    public void future() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        Future<Long> f1 = executor.submit(new TaskSquare(1));
        Future<Long> f2 = executor.submit(new TaskSquare(2));
        Future<Long> f3 = executor.submit(new TaskSquare(3));
        
        ThreadUtils.sleep(100);
        assertFalse(f3.isDone());
        
        ThreadUtils.sleep(300);
        assertTrue(f1.isDone() && f2.isDone() && f3.isDone());
        assertEquals(14, f1.get() + f2.get() + f3.get());
    }
    
    @Test
    public void testShutdownVsShutdownNow_ifShutdownThanAlreadyRunningTasksContinueInWork() throws InterruptedException {
        CommonRunnable task = CommonRunnable.create(1000);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);
        Thread.sleep(100);

        executor.shutdown();
        Thread.sleep(2000);

        assertTrue(task.isFinished());
        assertFalse(task.getStat().isInterrupted());
    }

    @Test
    @Ignore
    public void testShutdownVsShutdownNow_ifShutdownNowThanRunningTasksAreInterrupted() throws InterruptedException {
        CommonRunnable task = CommonRunnable.create(3000);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);
        Thread.sleep(100);

        System.out.println("Shutdown now!");
        executor.shutdownNow();
        Thread.sleep(5000);

        System.out.println(task.getStat().getLivetime());
        System.out.println(task.getStat().isInterrupted());

        assertTrue(task.isFinished());
        assertTrue(task.getStat().isInterrupted());
    }
    
}
