package cz.kul.app.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This sample run a few Runnable instances
 * in thread pool.
 * 
 * @author Ladislav Kulhanek
 */
public class ThreadPool {
	
	public static class Task implements Runnable {
		
		private String name;
		
		public Task(String name) {
			this.name = name;
		}
		
		@Override
		public void run() {
			StringBuilder threadIdentification = new StringBuilder();
			threadIdentification.append(name).append(" ");
			threadIdentification.append(Thread.currentThread().getName()).append(" ");
			threadIdentification.append(Thread.currentThread().getId()).append(" ");
			
			System.out.println("START: " + threadIdentification);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("END:   " + threadIdentification);
		}
		
	}

	public static void main(String[] args) {
		int pool_size = 2;
		int tasks_count = 10;
		
		
		// Create pool. The pool has fixed count of threads.
		// When one of the pool thread finish a task, it remains
		// in the pool. It is NOT terminated. Then it can process
		// another task. These threads are called workers.
		ExecutorService es = Executors.newFixedThreadPool(pool_size);
		
		// Creates tasks and let them execute in the pool.
		for (int i = 0; i < tasks_count; i++) {
			Task foo = new Task("Job " + i);
			es.submit(foo);
		}
		
		// Pool must be shutdowned, because its workers are still existing.
		es.shutdown();

	}

}
