package cz.kul.snippets.spring._08_javaconfig;

import cz.kul.snippets.spring._10_overriding.module1.Module1Bean;
import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Imagine module application:
 *
 *                  Module: App
 *                  Config: AppConfig.java (import Module1Config, Module2Config)
 *                    |
 *     -----------------------------------
 *    |                                  |
 * Module: Module1                    Module: Module2
 * Config: Module1Config.java         Config: Module2Config.java (import Module1Config)
 *                                       |
 *                                    Module: Module1
 *
 * The problem is, Module1Config is imported two times on AppConfig. And it is not easy to inport it only once because:
 *   * module2 depends on module1 so it must import module1 config
 *   * app know it depends on module1 and module2 but do not know more about modules. It is hard to know transient
 *     dependencies.
 * Fortunatelly it is not problem for spring and multiple import does not cause no harm.
 */
public class TestDoubleConfigImport {

    private static int numberOfExecutions;

    @Configuration
    public static class Module1Config {

        @Bean
        public Bean1 module1Bean() {
            numberOfExecutions++;
            return new Bean1("module1");
        }
    }

    @Configuration
    @Import(Module1Bean.class)
    public static class Module2Config {

        @Bean
        public Bean1 module2Bean() {
            return new Bean1("module2");
        }
    }

    @Configuration
    @Import({
            Module1Config.class,
            Module2Config.class
    })
    public static class AppConfig {

        @Bean
        public Bean1 appBean() {
            return new Bean1("appBean");
        }
    }

    @Before
    public void before() {
        numberOfExecutions = 0;
    }

    @Test
    public void beansAreCreatedCorrectly() {
        SpringTestUtils.runInSpring(AppConfig.class, ctx -> {
            Set<String> beanNames = new HashSet<>(Arrays.asList(ctx.getBeanNamesForType(Bean1.class)));
            assertEquals(new HashSet<>(Arrays.asList("module1Bean", "module2Bean", "appBean")), beanNames);
        });
    }

    @Test
    public void module1BeanInitMethodIsExecutedOnce() {
        SpringTestUtils.runInSpring(AppConfig.class, ctx -> {
            assertEquals(1, numberOfExecutions);
        });
    }

}
