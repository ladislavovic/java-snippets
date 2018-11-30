package cz.kul.snippets.spring._08_javaconfig;

import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertNotNull;

public class _01_TestHelloWorld {

    @Configuration
    public static class ConfigHelloWorld {

        @Bean
        public Bean1 fooBean() {
            return new Bean1();
        }
    }

    @Test
    public void testHelloWorld() {
        SpringTestUtils.runInSpring(ConfigHelloWorld.class, ctx -> {
            Bean1 bean = ctx.getBean(Bean1.class);
            assertNotNull(bean);
        });
    }

}
