package cz.kul.snippets.concurrency.example11_interruptDuringIO;

import cz.kul.snippets.ThreadUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Somebody said the thread is stopped when it is in interrupted state
 * and an IO operation is started.
 *
 * It is NOT TRUE. There is nothing like that. The thread just must check
 * interrupted state and stop the work when needed.
 *
 * The following example run 50 threads. They open files, write data and must
 * change their sunning/sleeping state, because my computer does not have
 * so many cores. But there is no "automatic" interrupted detection.
 */
public class InterruptDuringIoMain {

	public static void main(String[] args) {

		final int THREADS = 50;
		Thread[] ioThreads = new Thread[THREADS];

		for (int i = 0; i < THREADS; i++) {
			ioThreads[i] = new Thread(new ByteFileWriter(), "my-io-thread-" + i);
			ioThreads[i].start();
		}

		ThreadUtils.sleep(1000);

		for (int i = 0; i < THREADS; i++) {
			ioThreads[i].interrupt(); // It has no effect on running IO threads. To stop them they just must check interrupted state themselves.
		}

	}

	public static class ByteFileWriter implements Runnable {

		@Override
		public void run() {

			try {
				Random random = new Random();
				File randomFile = File.createTempFile("interruption", "data");

				long ITERATIONS = 100;
				long BYTES = 1_000_000;

				for (long i = 0; i < ITERATIONS; i++) {

					try (FileOutputStream fos = new FileOutputStream(randomFile)) {
						for (long j = 0; j < BYTES; j++) {
							long byteNo = (i * BYTES) + j;
							if (byteNo % 100_000 == 0) {
								System.out.println("Writing the byte no " + byteNo + ", thread " + Thread.currentThread().getName());
							}
							byte randomByte = (byte) random.nextInt();
							fos.write(randomByte);
						}
					} catch (IOException e) {
						System.out.println("IOException: " + e.getMessage());
					}
				}

			} catch (Throwable e) {
				System.out.println("An unknown exception was thrown: " + e.getMessage());
			}

		}
	}

}
