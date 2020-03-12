package cz.kul.snippets.concurrency.example08_Executor;

import cz.kul.snippets.ThreadUtils;

import java.util.concurrent.Callable;

public class TaskSquare implements Callable<Long> {
    
    long num;

    public TaskSquare(long num) {
        this.num = num;
    }

    @Override
    public Long call() {
        ThreadUtils.sleep(100);
        return num * num;
    }
    
}
