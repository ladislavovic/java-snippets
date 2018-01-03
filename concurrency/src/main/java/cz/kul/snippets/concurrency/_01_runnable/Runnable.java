package cz.kul.snippets.concurrency._01_runnable;

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
public class Runnable implements java.lang.Runnable {

	public static void main(String[] args) {
		Runnable r = new Runnable();
		(new Thread(r)).start();
		System.out.println("Main thread.");
	}

	@Override
	public void run() {
		System.out.println("I'm running in my own thread.");
	}

}
