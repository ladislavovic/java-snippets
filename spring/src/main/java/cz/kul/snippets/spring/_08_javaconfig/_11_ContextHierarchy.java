package cz.kul.snippets.spring._08_javaconfig;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class _11_ContextHierarchy {

    @Configuration
    public static class ParentContextConfiguration {

        @Bean
        public String bean1() {
            return "parent_b1";
        }
        
        @Bean
        public String bean2() {
            return "parent_b2";
        }

    }

    @Configuration
    public static class ChildContextConfiguration {

        @Bean
        public String bean1() {
            return "child_b1";
        }
        
        @Bean
        public String bean3(@Qualifier("bean2") String dep) {
            return dep + "_" + 3;
        }

    }

    @Test
    public void test() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            ctx.register(ParentContextConfiguration.class);
            ctx.refresh();
            try (AnnotationConfigApplicationContext ctx2 = new AnnotationConfigApplicationContext()) {
                ctx2.register(ChildContextConfiguration.class);
                ctx2.setParent(ctx);
                ctx2.refresh();

                Assert.assertEquals("parent_b2", ctx2.getBean("bean2"));
                Assert.assertEquals("child_b1", ctx2.getBean("bean1"));
                Assert.assertEquals("parent_b2_3", ctx2.getBean("bean3"));
            }

        }

    }

}
