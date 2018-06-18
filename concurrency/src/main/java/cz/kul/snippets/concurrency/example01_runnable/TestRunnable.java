package cz.kul.snippets.concurrency.example01_runnable;

import cz.kul.snippets.SnippetsTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Create implementation of Runnable interface is one possible way, how to
 * create new thread.
 * 
 * Procedure (steps):
 * 
 * 1. Implement java.lang.Runnable
 * 2. Create instance of class
 * 3. Create instance of Thread, put runnable instance to constructor
 * 4. Call a start() method of thread
 * 
 * @author Ladislav Kulhanek
 */
public class TestRunnable extends SnippetsTest {

	@Test
	public void test() throws InterruptedException {
		MyRunnable myRunnable = new MyRunnable();
		(new Thread(myRunnable)).start();

		// TODO not so dummy
		Thread.sleep(1000);

		Assert.assertNotEquals(myRunnable.threadId, 0);
		Assert.assertNotEquals(Thread.currentThread().getId(), myRunnable.threadId);
	}

	public static class MyRunnable implements Runnable {

		long threadId;

		@Override
		public void run() {
			threadId = Thread.currentThread().getId();
		}
	}



}
