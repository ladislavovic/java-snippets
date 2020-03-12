package cz.kul.snippets.spring.example_21_asyncAndScheduled;

import cz.kul.snippets.ThreadUtils;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static org.junit.Assert.assertTrue;

public class TestScheduled {

    public static int fixedDelayTask = 0;

    @Configuration
    @EnableScheduling
    public static class Conf {

        @Bean
        public A a() {
            return new A();
        }
    }

    public static class A {
        
        @Scheduled(fixedDelay = 1000)
        public void task1() {
            ThreadUtils.sleep(1000);
            fixedDelayTask++;
        }
    }

    @Test
    public void fixedDelay() {
        // Fixed delay means the test is run again after the given delay after
        // the previous run ends.
        SpringTestUtils.runInSpring(Conf.class, ctx -> {
            ThreadUtils.sleep(5000);
            assertTrue(fixedDelayTask >=2 && fixedDelayTask <= 3);
        });
    }
    
}
