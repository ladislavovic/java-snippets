package cz.kul.snippets.concurrency._03_sleep;

/**
 * You can sleep a thread for some time. The execution of the thread
 * is paused for some time.
 * 
 * The thread does not lose any monitor.
 * 
 * If the thread is interrupted during sleeping, an InterruptedException is
 * thrown.
 *
 * @author kulhalad
 * @since 7.4
 *
 */
public class MainSleep {

    public static void main(String[] args) {
        Thread t1 = new Thread(new SleepingThread(), "t1");
        t1.start();
        
        Thread t2 = new Thread(new SleepingThread(), "t2");
        t2.start();
        t2.interrupt();
    }
    
    public static class SleepingThread implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println(String.format("The thread \"%s\" has ended.", Thread.currentThread().getName()));
            } catch (InterruptedException e) {
                System.out.println(String.format("The thread \"%s\" was interrupted.", Thread.currentThread().getName()));
            } finally {
                clean();
            }
        }
        
        private void clean() {
            // this method could clean resources
        }
        
    }

}
