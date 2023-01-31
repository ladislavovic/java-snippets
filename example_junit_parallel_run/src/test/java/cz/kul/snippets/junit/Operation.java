package cz.kul.snippets.junit;

public class Operation {

    public static volatile int counter;

    public static void createAndRun(int lastInSeconds) {
        Operation operation = new Operation();
        operation.run(lastInSeconds);
    }

    public void run(int lastInSeconds) {
        try {
            System.out.println("Running, count=" + counter++ + " thread: " + Thread.currentThread().getName());
            Thread.sleep(lastInSeconds * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted...", e);
        }
    }

}
