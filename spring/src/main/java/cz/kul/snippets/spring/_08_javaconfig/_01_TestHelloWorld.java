package cz.kul.snippets.spring._08_javaconfig;

import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

import static org.junit.Assert.assertNotNull;

public class _01_TestHelloWorld {
    
    public static interface Inf {
        
    }
    
    public static class MB1 implements Inf {
        
    }
    
    public static class MB2 implements Inf {
        
    }
    
    public static class MB3 implements Inf {
        
    }

    @Configuration
    public static class ConfigHelloWorld {

        @Bean
        public Bean1 fooBean() {
            return new Bean1();
        }
        
        @Bean public MB1 mb1() {return new MB1();}
        @Bean public MB2 mb2() {return new MB2();}
        @Bean public MB2 mb3() {return new MB2();}
    }

    @Test
    public void testHelloWorld() {
        SpringTestUtils.runInSpring(ConfigHelloWorld.class, ctx -> {
            Bean1 bean = ctx.getBean(Bean1.class);
            assertNotNull(bean);
        });
    }
    
    // TODO temporary test, REMOVE!
    @Test
    public void tmp() {
        SpringTestUtils.runInSpring(ConfigHelloWorld.class, ctx -> {
            Collection<Inf> values = ctx.getBeansOfType(Inf.class).values();
            System.out.println("SIZE: " + values.size());
        });
    }
    
}
