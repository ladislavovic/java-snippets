package cz.kul.snippets.concurrency.example13_myTaskExecutor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

public class MyExecutor implements Executor
{

    private final static int NO_OF_THREADS = 10;

    private final List<WorkerThread> workers;

    public MyExecutor()
    {
        // create threads
        this.workers = new ArrayList<>(NO_OF_THREADS);
        for (int i = 0; i < NO_OF_THREADS; i++) {
            workers.add(new WorkerThread("my-executor-worker-%s".formatted(i)));
        }

        // start threads
        for (int i = 0; i < NO_OF_THREADS; i++) {
            workers.get(i).start();
        }
    }

    @Override
    public void execute(final Runnable task)
    {
        doExecute(task);
    }

    private synchronized void doExecute(final Runnable task)
    {
        findFreeWorker()
            .orElseThrow(() -> new IllegalStateException("No worker thread available"))
            .setTask(task);

    }

    private synchronized Optional<WorkerThread> findFreeWorker()
    {
        for (WorkerThread worker : workers) {
            if (worker.isFree()) {
                return Optional.of(worker);
            }
        }
        return Optional.empty();
    }

    public int getNoOfFreeWorkers() {
        return (int) workers.stream().filter(WorkerThread::isFree).count();
    }

    private static class WorkerThread extends Thread
    {

        private Runnable task;

        public WorkerThread(String threadName)
        {
            super(threadName);
        }

        @Override
        public void run()
        {
            while (true) {

                if (currentThread().isInterrupted()) {
                    return;
                }

                if (task == null) {
                    try {
                        sleep(Duration.ofMinutes(1));
                        continue;
                    } catch (InterruptedException e) {
                        currentThread().interrupt();
                        return;
                    }
                }

                task.run();
                task = null;
            }
        }

        public boolean isFree() {
            return task == null;
        }

        public void setTask(Runnable task)
        {
            if (isFree()) {
                this.task = task;
//                this. TODO continue here
            } else {
                throw new IllegalStateException("Can not accept a new task, the worker thread is not free.");
            }
        }

    }

}
