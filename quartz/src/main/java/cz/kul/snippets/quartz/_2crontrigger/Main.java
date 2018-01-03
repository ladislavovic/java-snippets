package cz.kul.snippets.quartz._2crontrigger;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Schedule Job by CronTrigger. Cron trigger 
 * is very useful trigger.
 * 
 * Format of cron string:
 * S M H DoM M DoW Y
 * 
 * ranges - fields can contains ranges, eg.: 0-10 
 *          or MON-FRI
 * lists - fields can contains lists, eg.: 0,10,20
 * increments - eg. 3/20 in minutes field. It means
 *              3,23,43
 * DoM - Dao of Month. Can contain any value between
 *       1-31
 * M - Month. Can contains values 0-11 or strings
 *     JAN, FEB, ...
 * DoW - Day of week. Can contain values from 1 to 7
 *       or strings SUN, MON. 1=SUN!
 * ? - It is used for DoM and DoW when you do not want
 *     to specify these fields.
 * L - In DoM it means the last day in the month. You can
 *     use offset: L-3 This means the third-to-last day.
 *     In DoW means the last day of week (SAT) or it can
 *     be used in the form FRIL. That means the last
 *     friday in the month.
 * year - it is optional
 * 
 * @author Ladislav Kulhanek
 *
 */
public class Main {

    public static void main(String[] args) {

        try {
            JobDetail jobDetail = newJob(FooJob.class).withIdentity("foo", "group1").build();
            Trigger cronTrigger = newTrigger().withIdentity("cron", "group1").withSchedule(cronSchedule("0/3 * * * * ?")).build();
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.scheduleJob(jobDetail, cronTrigger);
            scheduler.start();
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class FooJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("I am executed.");
        }

    }

}
