package cz.kul.snippets.concurrency.example03_join;

import cz.kul.snippets.ThreadUtils;
import cz.kul.snippets.concurrency.commons.CommonRunnable;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Join causes that current thread is suspended until the thread,
 * which is current thread joined to, is stopped. It is possible
 * to specify the max wait time.
 * 
 * @author Ladislav Kulhanek
 */
public class TestJoin {

	@Test
	public void testJoin() throws InterruptedException {
		Date start = new Date();
		Thread t1 = new Thread(CommonRunnable.createNotInterruptable(3000));
		t1.start();

		// stop current thread until thread "t1" ends
		t1.join();

		Date end = new Date();
		Assert.assertEquals(3000, end.getTime() - start.getTime(), 300);
	}

	@Test
	public void testJoinWithTimeout() throws InterruptedException {
		Date start = new Date();
		Thread t1 = new Thread(CommonRunnable.createNotInterruptable(3000));
		t1.start();

		// stop current thread until thread "t1" ends or until timeout
		t1.join(1000);

		Date end = new Date();
		Assert.assertEquals(1000, end.getTime() - start.getTime(), 300);
	}

	@Test
	public void itIsPossibleToJoinToTerminatedThread_thenItJustDoesNotWait() throws InterruptedException {
		Thread t1 = new Thread(CommonRunnable.createNotInterruptable(1000));
		t1.start();
		ThreadUtils.sleep(2000);
		Assert.assertEquals(Thread.State.TERMINATED, t1.getState());
		t1.join();
	}

	@Test
	public void itIsPossibleToJoinToNewThread_thenItJustDoesNotWait() throws InterruptedException {
		Thread t1 = new Thread(CommonRunnable.createNotInterruptable(1000));
		ThreadUtils.sleep(2000);
		Assert.assertEquals(Thread.State.NEW, t1.getState());
		t1.join();
		Assert.assertEquals(Thread.State.NEW, t1.getState());
	}

}

