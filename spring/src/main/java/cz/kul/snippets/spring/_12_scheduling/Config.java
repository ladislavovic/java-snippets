package cz.kul.snippets.spring._12_scheduling;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class Config {

    @Configuration
    public static class Nested {

        @Scheduled(cron = "0/1 * * * * ?")
        public void aJob() {
            System.out.println("Job");
        }

    }

}
