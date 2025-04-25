package cz.kul.snippets.concurrency.example12_asyncStackTrace;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <h3>Debug asynchronous code</h3>
 *
 * <p>
 * It is an IntelliJ feature which makes debugging of async code easier.
 * To debug an asynchronous code is hard because the async task is scheduled in one thread and executed in another. If you write out the
 * task thread you do not see stacktrace/values of thread which created this thread.
 * </p>
 *
 * <p>
 * IntelliJ IDEA makes it easier by establishing a connection between frames in different threads. This allows you to look back from a worker
 * thread to the place where the task was scheduled and debug the program as if the execution was all in the same thread.
 * </p>
 *
 * <p>
 * It runs out of the box for java.concurrent and you can enable it also for your code by annotations.
 * </p>
 *
 * More info: https://www.jetbrains.com/help/idea/debug-asynchronous-code.html
 */
public class AsyncStackTrace
{
    public static void main(String[] args) throws ExecutionException, InterruptedException
    {
        int length = 7;
        boolean onlyLetters = true;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> futureRandomString = executor.submit(() -> {
            System.out.println("This task is running in separated thread");
            return createRandomPassword(length, onlyLetters);
        });
        System.out.println(futureRandomString.get());
    }

    public static String createRandomPassword(int length, boolean onlyLetters) {
        System.out.println("Creating random password...");

        // Put a breakpoint here, IntelliJ debugger should show you a thread which created this task.
        return onlyLetters ? RandomStringUtils.randomAlphabetic(length) : RandomStringUtils.randomAlphanumeric(length);
    }

}
