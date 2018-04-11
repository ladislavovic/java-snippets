package cz.kul.snippets.spring._12_javaconfig_configuration_inheritance;

import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.kul.snippets.spring.common.SpringTestUtils.getBeanRegistrationCount;
import static org.junit.Assert.assertEquals;

public class TestInheritance {

    @Before
    public void beforeEachTest() {
        SpringTestUtils.cleanMemmoryAppender();
    }

    public static class Parent {

        @Bean
        public Bean1 bean1() {
            return new Bean1("fromParent");
        }

    }

    @Configuration
    public static class Child extends Parent {

        @Bean
        @Override
        public Bean1 bean1() {
            return new Bean1("fromChild");
        }

    }

    /**
     * @Bean annotation can be on both - on parent and child and it works well.
     * Spring registers bean only once (no overriding is there).
     */
    @Test
    public void override_BeanAnnotationOnBoth() {
        SpringTestUtils.runInSpring(TestInheritance.Child.class, ctx -> {
            Bean1 bean = (Bean1) ctx.getBean("bean1");
            assertEquals("fromChild", bean.getVal());
            assertEquals(1, getBeanRegistrationCount());
        });
    }

    public static class Parent2 {

        @Bean
        public Bean1 bean1() {
            return new Bean1("bean1FromParent");
        }

        @Bean
        public Bean1 bean2() {
            return new Bean1("bean2FromParent");
        }

    }

    @Configuration
    public static class Child2 extends Parent2 {

        @Override
        public Bean1 bean1() {
            return new Bean1("bean1FromChild");
        }

    }

    /**
     * Spring looks in the parent class if there is any @Bean annotation and
     * works correctly. Parent does not have to have @Configuration annotation.
     */
    @Test
    public void override_BeanAnnotationOnParent() {
        SpringTestUtils.runInSpring(TestInheritance.Child2.class, ctx -> {
            Bean1 bean1 = (Bean1) ctx.getBean("bean1");
            assertEquals("bean1FromChild", bean1.getVal());

            Bean1 bean2 = (Bean1) ctx.getBean("bean2");
            assertEquals("bean2FromParent", bean2.getVal());

            assertEquals(2, getBeanRegistrationCount());
        });
    }

    @Configuration
    public static class Parent3 {

        @Bean
        public Bean1 bean1() {
            return new Bean1("fromParent");
        }

    }

    @Configuration
    public static class Child3 extends Parent3 {

        @Override
        @Bean
        public Bean1 bean1() {
            return new Bean1("fromChild");
        }

    }

    /**
     * Event thought parent has @Configuration annotation beans are still
     * registered only once, there is no duplicity, no overridding.
     */
    @Test
    public void override_ParentWithConfiguration() {
        SpringTestUtils.runInSpring(TestInheritance.Child3.class, ctx -> {
            Bean1 bean1 = (Bean1) ctx.getBean("bean1");
            assertEquals("fromChild", bean1.getVal());
            assertEquals(1, getBeanRegistrationCount());
        });
    }

}
