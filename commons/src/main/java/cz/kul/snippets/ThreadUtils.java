package cz.kul.snippets;

import java.util.Collection;

public class ThreadUtils {

    public static void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            // do nothing
        }
    }
    
    public static void sleepOneSecond() {
        ThreadUtils.sleep(1000);
    }
    
    public static void join(Collection<Thread> threads) {
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            // do nothing
        }
    }

}
