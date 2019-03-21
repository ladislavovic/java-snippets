package cz.kul.snippets.quartz.example02_dataMap;

import org.junit.Assert;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class TestJobDataMap {

    public static class Job1 implements Job {
        
        private static String log = "";

        private int counter;

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            log = log + map.getInt("counter");
            log = log + counter;
            map.put("counter", ++counter);
        }

        public void setCounter(int counter) {
            this.counter = counter;
        }
        
        public static String getLog() {
            return log;
        }
    }

    @PersistJobDataAfterExecution
    public static class Job2 extends Job1 {
    }
    
    @Test
    public void testDataMap() throws SchedulerException, InterruptedException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();

        JobDataMap map = new JobDataMap();
        map.put("counter", 1);

        JobDetail jobDetail = newJob(Job1.class).withIdentity("nonPersistant").usingJobData(map).build();

        Trigger trigger = newTrigger()
                .withIdentity("myTrigger")
                .startNow()
                .withSchedule(simpleSchedule().withIntervalInSeconds(1).repeatForever())
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        Thread.sleep(4000);
        scheduler.shutdown();
        Assert.assertTrue(Job1.getLog().startsWith("1122"));
    }
    
    @Test
    public void testDataMapPersisting()throws SchedulerException, InterruptedException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();

        JobDataMap map = new JobDataMap();
        map.put("counter", 1);

        JobDetail jobDetail = newJob(Job2.class).withIdentity("nonPersistant").usingJobData(map).build();

        Trigger trigger = newTrigger()
                .withIdentity("myTrigger")
                .startNow()
                .withSchedule(simpleSchedule().withIntervalInSeconds(1).repeatForever())
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        Thread.sleep(4000);
        scheduler.shutdown();
        Assert.assertTrue(Job2.getLog().startsWith("1122"));
    }
    
}
