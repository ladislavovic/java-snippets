package cz.kul.app.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TesterLock {

	public static class Counter {

		private int i = 0;
		private Lock lock = new ReentrantLock();

		public int increment() {
			try {
				lock.lock(); // if lock is locked, thread waits here
				int newValue = i++;
				return newValue;
			} finally {
				lock.unlock();
			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
