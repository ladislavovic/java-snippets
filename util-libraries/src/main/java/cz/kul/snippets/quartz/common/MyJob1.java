package cz.kul.snippets.quartz.common;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class MyJob1 implements Job {
    
    public static final String MY_JOB_1_AGENT = "my_job_1_agent";
    
    private static int counter = 0;

    @Override
    public void execute(JobExecutionContext arg0) {
        counter++;
    }
    
    public static int getExecutionCount() {
        return counter;
    }
    
}
