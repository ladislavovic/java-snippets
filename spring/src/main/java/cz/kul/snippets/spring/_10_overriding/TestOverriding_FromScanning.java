package cz.kul.snippets.spring._10_overriding;

import cz.kul.snippets.spring._10_overriding.module1.Module1Bean;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertEquals;

public class TestOverriding_FromScanning {

    @Configuration
    @ComponentScan("cz.kul.snippets.spring._10_overriding.module1")
    public static class Config {

        @Bean
        public Module1Bean foo() {
            return new Module1Bean("config");
        }

    }

    @Test
    public void configWins() {
        SpringTestUtils.runInSpring(TestOverriding_FromScanning.Config.class, ctx -> {
            Module1Bean bean = (Module1Bean) ctx.getBean("foo");
            assertEquals("config", bean.getVal());
        });
    }

}
