package cz.kul.snippets.spring.example_21_asyncAndScheduled;

import cz.kul.snippets.ThreadUtils;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

public class TestAsyncMehodReturningAValue {

    private static Map<String, Object> sharedMemmory = new HashMap<>();

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
        public Future<Double> sqrt(int a) {
            ThreadUtils.sleep(1000);
            if (a == 0) throw new IllegalArgumentException("Can not calculate root of zero");
            Future<Double> future = new AsyncResult<>(Math.sqrt(a));
            sharedMemmory.put("future", future);   
            return future;
        }
    }

    @Test
    public void happyScenario() {
        SpringTestUtils.runInSpring(Conf.class, ctx -> {
            A a = (A) ctx.getBean("a");
            Future<Double> sqrt = a.sqrt(100);
            try {
                Double result = sqrt.get(2, TimeUnit.SECONDS);
                assertEquals(10.0, result, 0.01);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void throwedException() {
        SpringTestUtils.runInSpring(Conf.class, ctx -> {
            A a = (A) ctx.getBean("a");
            Future<Double> sqrt = a.sqrt(0);
            ExecutionException ee = null;
            try {
                Double result = sqrt.get(2, TimeUnit.SECONDS);
                assertEquals(10.0, result, 0.01);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                ee = e;
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            assertNotNull(ee);
            assertTrue(ee.getCause().getClass() == IllegalArgumentException.class);
        });
    }

    @Test
    public void proxyReturnsTemFuture() {
        // It is good to be avare the Spring magic
        // returns temporal Future object and it is not the same
        // which is manually created in the async method.
        SpringTestUtils.runInSpring(Conf.class, ctx -> {
            A a = (A) ctx.getBean("a");
            Future<Double> sqrt = a.sqrt(100);
            ThreadUtils.sleep(2000);
            assertFalse(sqrt == sharedMemmory.get("future"));
        });
    }

}
