package cz.kul.snippets.spring._08_javaconfig;

import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.BeanWithDependency;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertTrue;

public class _02_TestInjecting {

    @Configuration
    public static class ConfigInjecting {

        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }
        
        @Bean
        public BeanWithDependency bean2_1(Bean1 bean1) {
            // NOTE: This is a preferred way according to me.
            return new BeanWithDependency(bean1);
        }

        @Bean()
        public BeanWithDependency bean2_2() {
            // NOTE:
            // Spring ensure Bean1 is really singleton.
            // It does not execute real bean1() method, there is wrapper magic
            // so spring returns Bean1 from the context.
            return new BeanWithDependency(bean1());
        }
    }

    @Test
    public void testInjecting() {
        SpringTestUtils.runInSpring(ConfigInjecting.class, ctx -> {
            Bean1 bean1 = ctx.getBean(Bean1.class);
            BeanWithDependency bean2_1 = (BeanWithDependency) ctx.getBean("bean2_1");
            BeanWithDependency bean2_2 = (BeanWithDependency) ctx.getBean("bean2_2");
            assertTrue(bean1 == bean2_1.getBean1());
            assertTrue(bean1 == bean2_2.getBean1());
        });
    }
    
}
