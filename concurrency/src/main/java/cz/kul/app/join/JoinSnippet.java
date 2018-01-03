package cz.kul.app.join;

/**
 * Join causes that current thread is suspended until the thread,
 * which is current thread joined to, is stopped. It is possible
 * to specify the max wait time.
 * 
 * @author Ladislav Kulhanek
 */
public class JoinSnippet implements java.lang.Runnable {

	public static void main(String[] args) throws InterruptedException {
		JoinSnippet r = new JoinSnippet();
		Thread t = new Thread(r);
		
		// Execute thread
		t.start();
		
		// Tell to current thread to go sleap until t thread finish its work.
		// But max waiting time is 2s
		t.join(2000);
		System.out.println("Main thread.");
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < 10; i++) {
				System.out.println("I'm running in my own thread.");
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupted.");
		}
	}

}
