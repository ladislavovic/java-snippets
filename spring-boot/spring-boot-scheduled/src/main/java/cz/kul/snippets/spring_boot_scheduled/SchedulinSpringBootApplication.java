package cz.kul.snippets.spring_boot_scheduled;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class SchedulinSpringBootApplication
{

	@Configuration
/*
* Enables Spring's scheduled task execution capability
* By default, Spring will search for an associated scheduler definition: either a unique
 org.springframework.scheduling.TaskScheduler bean in the context, or a TaskScheduler bean named
 "taskScheduler" otherwise;
 The same lookup will also be performed for a java.util.concurrent.ScheduledExecutorService bean.
 If neither of the two is resolvable, a local single-threaded default scheduler will be created
 and used within the registrar.
* By default, Spring uses single thread to run the tasks so the tasks (even two different ones) does not
  run in parallel.
 */
	@EnableScheduling
	public static class SchedulingSpringConfig
	{

	}

	@Service
	public static class TempDirectoryCleaner
	{
		// * delay means the time between the end of the previous task and the start of the next
		//   task is fixed. Here it is 1 second
		// * Number of units of time to delay before the first execution. It is handy when you do not want
		//   to execute it during the application start
		@Scheduled(fixedDelayString = "PT1S", initialDelayString = "PT3S")
		public void cleanUpTempDirectory()
		{
			System.out.println("Cleaning up temp directory.");
		}

	}

	public static void main(String[] args) {
		SpringApplication.run(SchedulinSpringBootApplication.class, args);
	}

}
