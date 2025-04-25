package cz.kul.snippets.concurrency.example13_myTaskExecutor;

import org.junit.Test;

public class MyExecutorTest
{

    @Test
    public void test() throws InterruptedException
    {
        MyExecutor myExecutor = new MyExecutor();

        System.out.println(myExecutor.getNoOfFreeWorkers());

        myExecutor.execute(() -> {
            System.out.println("Hello World!");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {}
            System.out.println("Hello World!");
        });

        System.out.println(myExecutor.getNoOfFreeWorkers());

        Thread.sleep(4000);

        System.out.println(myExecutor.getNoOfFreeWorkers());


    }


}
