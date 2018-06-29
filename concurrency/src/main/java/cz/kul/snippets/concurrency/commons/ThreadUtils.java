package cz.kul.snippets.concurrency.commons;

public class ThreadUtils {

    public static void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            // do nothing
        }
    }

}
