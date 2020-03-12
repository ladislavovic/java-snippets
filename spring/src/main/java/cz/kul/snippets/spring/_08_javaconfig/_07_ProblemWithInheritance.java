package cz.kul.snippets.spring._08_javaconfig;

import cz.kul.snippets.spring._08_javaconfig.module1.A;
import cz.kul.snippets.spring._08_javaconfig.module1.B;
import cz.kul.snippets.spring._08_javaconfig.module1.C;
import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * TODO: when you have @Bean annotation only in parent (class B) 
 * class C does not register any bean.
 * I think it should because it inherit it from B. Why????
 * So I still do not understand it fully. Look on that carefully. What is bean registration?
 * What when @Bean is in parent only? Etc.
 */
public class _07_ProblemWithInheritance {

    @Before
    public void beforeEachTest() {
        SpringTestUtils.cleanMemmoryAppender();
    }

    @Configuration
    @ComponentScan("cz.kul.snippets.spring._08_javaconfig.module1")
    public static class Config {
    }

    /**
     * This si problematic because when you have @Cnfiguration on parent and also on child,
     * component scan configure context by both classes. It brings these problems;
     * <ul>
     *     <li>bean definitions are registered more times, so overriding occurs</li>
     *     <li>You can not affect order of configuration execution. Basically it
     *     is recursive and alphabetic. So you can not ensure parent is inicialized before
     *     children</li>
     * </ul>
     */
    @Test
    public void configurationAnnotationOnParentAndChild() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class)) {

            Bean1 bean = (Bean1) ctx.getBean("bean1");

            BeanDefinition beanDefinition = ctx.getBeanDefinition("bean1");

            assertEquals("C", bean.getVal());
            assertEquals(3, SpringTestUtils.getBeanRegistrationCount());
            assertTrue(SpringTestUtils.isBeanOverriding("bean1", A.class, B.class));
            assertTrue(SpringTestUtils.isBeanOverriding("bean1", B.class, C.class));
        };
    }

}
