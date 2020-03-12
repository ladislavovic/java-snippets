package cz.kul.snippets.spring.example_21_asyncAndScheduled;

import cz.kul.snippets.ThreadUtils;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class TestVoidAsyncMehod {
    
    private static Map<String, Long> sharedMemmory = new HashMap<>();
    
    @Configuration
    @EnableAsync
    public static class Conf {
        
        @Bean
        public A a() {
            return new A();
        }
    }
    
    public static class A {
        @Async
        public void doSomething() {
            sharedMemmory.put("threadId", Thread.currentThread().getId());
        }
    }
    
    @Test
    public void methodShouldRunAsynchronously() {
        SpringTestUtils.runInSpring(Conf.class, ctx -> {
            A a = (A) ctx.getBean("a");
            a.doSomething();
            ThreadUtils.sleep(1000);
            Long mainThreadId = Thread.currentThread().getId();
            Long asyncThreadId = sharedMemmory.get("threadId");
            assertNotEquals(mainThreadId, asyncThreadId);
            assertNotNull(asyncThreadId);
        });
    }
    
}
