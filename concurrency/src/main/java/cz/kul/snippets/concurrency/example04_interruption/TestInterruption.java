package cz.kul.snippets.concurrency.example04_interruption;

import cz.kul.snippets.ThreadUtils;
import cz.kul.snippets.concurrency.commons.Agent;
import cz.kul.snippets.concurrency.commons.CommonRunnable;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * You can interrupt a thread. But it only set status 'interrupted' to true.
 * The treat must check this status and must behave accordint to it itself.
 * <p>
 * I means the thread must ends itself, you can not stop it this way.
 *
 * @author kulhalad
 * @since 7.4
 */
public class TestInterruption {

	@Test
	public void test_interruptMethodSetThreadFlag() throws InterruptedException {
		Thread t = new Thread(() -> {
			while (true) {
				if (Thread.currentThread().isInterrupted()) break;
			}
		});
		t.start();
		Thread.sleep(100);

		t.interrupt();
		t.join(1000);
		Assert.assertTrue(!t.isAlive());
	}

	@Test
	public void test_whenInterruptedExceptionThrowedTheFlagIsNOTset() throws InterruptedException {
		Agent agent = new Agent();
		Thread t = new Thread(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// if you want set interrupted status you have to write
				// Thread.currentThread().interrupt();
			}
			agent.setValue(Thread.currentThread().isInterrupted());
		});
		t.start();
		Thread.sleep(100);

		t.interrupt();
		t.join(1000);
		Assert.assertFalse(agent.isValue());
	}

	@Test
	public void test_thisThreadCanBeInterrupted() throws InterruptedException {
		Thread t1 = new Thread(CommonRunnable.createInfinite());
		t1.start();
		t1.interrupt();
		t1.join(1000);
		Assert.assertFalse(t1.isAlive());
	}

	@Test
	public void test_thisThreadCanNotBeInterrupted() throws InterruptedException {
		Thread t1 = new Thread(CommonRunnable.createInfiniteNotInterruptable());
		t1.setDaemon(true);
		t1.start();
		t1.interrupt();
		t1.join(1000);
		Assert.assertTrue(t1.isAlive());
	}

	@Test
	public void test_interruptionDuringSleepCauseAnException() throws InterruptedException {
		StringBuilder agent = new StringBuilder();
		Thread thread = new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				agent.append("true");
			}
		});
		thread.start();
		thread.interrupt();
		thread.join();
		Assert.assertTrue(Boolean.valueOf(agent.toString()));
	}

}
