package cz.kul.snippets.concurrency._04_interruption;

/**
 * You can interrupt a thread. But it only set status 'interrupted' to true.
 * The treat must check this status and must behave accordint to it itself.
 * 
 * I means the thread must ends itself, you can not stop it this way.
 *
 * @author kulhalad
 * @since 7.4
 *
 */
public class MainInterruption {

    public static void main(String[] args) {
        Thread t1 = new Thread(new TestingThread(false), "t1");
        t1.start();
        t1.interrupt();
        
        Thread t2 = new Thread(new TestingThread(true), "t2");
        t2.start();
        t2.interrupt();
    }
    
    public static class TestingThread implements Runnable {
        
        boolean checkInterruption;
        
        public TestingThread(boolean checkInterruption) {
            this.checkInterruption = checkInterruption;
        }

        @Override
        public void run() {
            String tName = Thread.currentThread().getName();
            for (long i = 0; true; i++) {
                if (checkInterruption && Thread.interrupted()) {
                    System.out.println(tName + " interrupted");
                    break;
                }
                if (i == 1000000000L) break;
            }
            System.out.println(tName + " end");
        }
        
    }

}
