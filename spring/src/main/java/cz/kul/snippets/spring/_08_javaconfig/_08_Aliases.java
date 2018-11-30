package cz.kul.snippets.spring._08_javaconfig;

import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertNotNull;

public class _08_Aliases {

    @Configuration
    public static class Config {

        @Bean(name = {"bean1", "alias"})
        public Bean1 fooBean() {
            return new Bean1();
        }
    }

    @Test
    public void testHelloWorld() {
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            assertNotNull(ctx.getBean("bean1"));
            assertNotNull(ctx.getBean("alias"));
        });
    }

}
