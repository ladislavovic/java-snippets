package cz.kul.snippets.quartz._1helloworld;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
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
 * (1) Job - job is a class implementing Job interface. It is the "core". It is
 * what the job really do.
 * 
 * (2) JobDetail - it is class which connect together Job (implementation) and
 * other properties (name, group, ...)
 * 
 * (3) Trigger -  can execute jobs. It knows when to execute them. There
 * is 1:N association between JobDetail and Trigger
 * 
 * (4) Scheduler - main component of the library. It binds all together. You
 * can register JobDetails and triggers there. Cheduler is responsible for
 * job execution (trigger only send message to scheduler "start it", but scheduler
 * is responsible)
 * There is more implementations of Scheduler.
 * 
 * Lifecycle:
 *   - Scheduler must be started at the beginning
 *   - always when the Job is executed, new instance of the Job
 *     is created
 *   - at the end we have te shut it down (Scheduler is implemented by Threads,
 *     at the end all threads must be properly ended)
 * 
 * @author Ladislav Kulhanek
 *
 */
public class Main {

    // (1)
    public static class HelloJob implements Job {
        
        @Override
        public void execute(JobExecutionContext arg0) throws JobExecutionException {
            System.out.println("I am executed. My hash: " + hashCode());
        }
    }

    public static void main(String[] args) {

        try {
            // (2)
            JobDetail jobDetail = newJob(HelloJob.class).withIdentity("myJob", "group1").build();
            
            // (3)
            Trigger trigger = newTrigger().withIdentity("myTrigger", "group1").startNow()
                    .withSchedule(simpleSchedule().withIntervalInSeconds(2).repeatForever()).build();
     
            // (4)
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.scheduleJob(jobDetail, trigger); // it can be befor or after start. Both are possible.
            scheduler.start();
            Thread.sleep(10000);
            scheduler.shutdown();

        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    

}
