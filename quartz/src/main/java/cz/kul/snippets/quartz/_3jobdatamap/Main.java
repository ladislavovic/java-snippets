package cz.kul.snippets.quartz._3jobdatamap;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Job classes can not hold data, because the new
 * instance is created with every execution.
 * JobDataMap is used for that.
 * 
 * (1) JobDataMap is added to JobDetail. Job then can acces
 * that map duging execution. Trigger can have the map too.
 * If the job has the setter method with the same name,
 * as the property in the map, the setter is callet
 * with the value before execution.
 * 
 * (2) Two jobs are executing. One of them has the
 * @PersistJobDataAfterExecution annotaion. It cause,
 * than values of the map are persisted at the end of
 * execution. So counter is rising in the one job and
 * is not rising in the second job. 
 * 
 * Another important things:
 *  - data must be serializable
 *  - If the both maps contain the values with the same key,
 *    the value from the trigger map has the bigger priority
 *    (job ca not see the overrided value from JobDetail).
 *  - @DisallowConcurrentExecution annotation cause only
 *    the one instance of Job can be executed in the
 *    particular time (Job, not JobDetail).
 * 
 * @author Ladislav Kulhanek
 *
 */
public class Main {

    public static void main(String[] args) {

        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            JobDataMap map = new JobDataMap();
            map.put("counter", 1);

            JobDetail jobDetail = newJob(FooJob.class).withIdentity("nonPersistant", "group1").usingJobData(map).build();
            JobDetail jobDetail2 = newJob(JobWithPersistJobData.class).withIdentity("persistant", "group1").usingJobData(map)
                    .build();

            Trigger trigger = newTrigger().withIdentity("myTrigger", "group1").startNow()
                    .withSchedule(simpleSchedule().withIntervalInSeconds(1).repeatForever()).build();
            Trigger trigger2 = newTrigger().withIdentity("myTrigger2", "group1").startNow()
                    .withSchedule(simpleSchedule().withIntervalInSeconds(1).repeatForever()).build();

            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.scheduleJob(jobDetail2, trigger2);
            Thread.sleep(4000);
            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class FooJob implements Job {

        private int counter;

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println(context.getJobDetail().getKey() + " counter:" + counter);
            JobDataMap map = context.getJobDetail().getJobDataMap();
            map.put("counter", ++counter);
        }

        public void setCounter(int counter) {
            this.counter = counter;
        }

    }

    @PersistJobDataAfterExecution
    public static class JobWithPersistJobData extends FooJob {
    }

}
