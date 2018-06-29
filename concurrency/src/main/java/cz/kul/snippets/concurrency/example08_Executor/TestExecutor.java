package cz.kul.snippets.concurrency.example08_Executor;

import cz.kul.snippets.concurrency.commons.CommonRunnable;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestExecutor {

    @Test
    public void testShutdown_alreadyRunningTasksContinueInWork() throws InterruptedException {
        CommonRunnable task = CommonRunnable.create(1000);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);
        Thread.sleep(100);

        executor.shutdown();
        Thread.sleep(2000);

        Assert.assertTrue(task.isFinished());
        Assert.assertFalse(task.getStat().isInterrupted());
    }

    @Test
    public void testShutdownNow_runningTasksAreInterrupted() throws InterruptedException {
        CommonRunnable task = CommonRunnable.create(3000);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);
        Thread.sleep(100);

        System.out.println("Shutdown now!");
        executor.shutdownNow();
        Thread.sleep(5000);

        System.out.println(task.getStat().getLivetime());
        System.out.println(task.getStat().isInterrupted());

        Assert.assertTrue(task.isFinished());
        Assert.assertTrue(task.getStat().isInterrupted());
    }


}
