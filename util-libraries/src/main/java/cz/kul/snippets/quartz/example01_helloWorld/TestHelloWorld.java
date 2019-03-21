package cz.kul.snippets.quartz.example01_helloWorld;

import cz.kul.snippets.SnippetsTest;
import cz.kul.snippets.agent.AgentManager;
import cz.kul.snippets.quartz.common.MyJob1;
import org.junit.Assert;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class TestHelloWorld extends SnippetsTest {
    
    @Test
    public void test() throws SchedulerException, InterruptedException {
        JobDetail jobDetail = JobBuilder.newJob(MyJob1.class).withIdentity("myJob").build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("myTrigger")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever())
                .build();

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.scheduleJob(jobDetail, trigger); // it can be befor or after start. Both are possible.
        scheduler.start();
        Thread.sleep(5000);
        scheduler.shutdown();
        Assert.assertTrue(MyJob1.getExecutionCount() >= 4);
    }
    
}
