package cz.kul.snippets.spring._08_javaconfig;

import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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
public class _05_RegisterConfigMultipleTimes {

    @Configuration
    public static class Config {

        @Bean
        public Bean1 b1() {
            return new Bean1();
        }
    }

    @Test
    public void beansAreCreatedCorrectly() {
        // It is absolutelly no problem to register one configuration more times.
        // Spring just process it more times and override some bean definitions.
        //
        // Maybe it looks silly in this example but it easilly can happen when you have
        // more configurations and they import other configurations.
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            ctx.register(Config.class);
            ctx.register(Config.class);
            ctx.refresh();
            ctx.getBean(Bean1.class);
        }
    }

}
