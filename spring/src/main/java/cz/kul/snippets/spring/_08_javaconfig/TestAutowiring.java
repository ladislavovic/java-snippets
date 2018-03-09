package cz.kul.snippets.spring._08_javaconfig;

import cz.kul.snippets.spring._08_javaconfig.test_autowiring.Bean2;
import cz.kul.snippets.spring._08_javaconfig.test_autowiring.Bean3;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * When you create bean in Java Config autowired dependencies are inserted
 * automatically. Probably by some postprocessor.
 */
public class TestAutowiring {

    @Configuration
    @ComponentScan("cz.kul.snippets.spring._08_javaconfig.test_autowiring")
    public static class Config {

        @Bean
        public Bean2 configBean2() {
            Bean2 bean2 = new Bean2();
            assertNull(bean2.getBean1());
            return bean2;
        }

        @Bean
        public Bean3 configBean3() {
            Bean3 bean3 = new Bean3();
            assertNull(bean3.getBean1());
            return bean3;
        }

    }

    @Test
    public void dependenciesShouldBeAutowired_onComponent() {
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            Bean2 bean2 = (Bean2) ctx.getBean("configBean2");
            assertNotNull(bean2.getBean1());
        });
    }

    @Test
    public void dependenciesShouldBeAutowired_onNonComponent() {
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            Bean3 bean3 = (Bean3) ctx.getBean("configBean3");
            assertNotNull(bean3.getBean1());
        });
    }

}
