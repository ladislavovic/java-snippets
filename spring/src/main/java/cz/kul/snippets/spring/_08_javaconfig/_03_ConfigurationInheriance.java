package cz.kul.snippets.spring._08_javaconfig;

import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class _03_ConfigurationInheriance {

    @Before
    public void beforeEachTest() {
        SpringTestUtils.cleanMemmoryAppender();
    }

    @Configuration
    public static class ConfigParent {

        @Bean
        public Bean1 foo() {
            return new Bean1("foo");
        }

        @Bean
        public Bean1 bar() {
            return new Bean1("bar");
        }
    }

    @Configuration
    public static class ConfigChild extends ConfigParent {

        @Override
        @Bean
        public Bean1 bar() {
            return new Bean1("bar_child");
        }
    }

    @Test
    public void testChildOnly() {
        // Here the spring configure context by child only. This approach is absolutely OK
        // 
        // Spring check also parent metadata and configure context properly. Does not matter if
        // parent class has @Configuration annotation or not.
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            ctx.register(ConfigChild.class);
            ctx.refresh();
            
            assertFalse(SpringTestUtils.isBeanOverriding("bar", ConfigParent.class, ConfigChild.class));
            
            Bean1 foo = (Bean1) ctx.getBean("foo");
            Bean1 bar = (Bean1) ctx.getBean("bar");
            assertEquals("foo", foo.getVal());
            assertEquals("bar_child", bar.getVal());
        }
    }
    
    @Test
    public void testParentAndChild() {
        // Here the spring configure context by both parent and child. This approach is absolutely OK
        // 
        // No bean is created more times or something like that. Spring first create bean definitions
        // according to given config classes and when all config classes are processed then it
        // starts to create beans (it means call particular methos). So here child overrides parent.
        //
        // Only pitfall is when spring first process child and then parent. Then parent overrides chidl
        // and it is probably not what you want. So you must ensure right order of configuration files
        // especially during autoscanning.        
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            ctx.register(ConfigParent.class);
            ctx.register(ConfigChild.class);
            ctx.refresh();
            
            assertTrue(SpringTestUtils.isBeanOverriding("bar", ConfigParent.class, ConfigChild.class));
            
            Bean1 foo = (Bean1) ctx.getBean("foo");
            Bean1 bar = (Bean1) ctx.getBean("bar");
            assertEquals("foo", foo.getVal());
            assertEquals("bar_child", bar.getVal());
        }
    }

}
