package cz.kul.snippets.spring._08_javaconfig;

import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class _04_JavaConfigAndAutowiring {
    
    public static class A { 
    }
    
    public static class B {
        
        @Autowired
        private A a;

        public A getA() {
            return a;
        }
    }

    @Configuration
    public static class Config {

        @Bean
        public A a() {
            return new A();
        }

        @Bean
        public B b() {
            B b = new B();
            assertNull(b.getA());
            return b;
        }

    }

    @Test
    public void dependenciesAreAutowired() {
        // Autowired properties are injected automatically. Probably by some postprocessor.
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            B b = ctx.getBean(B.class);
            assertNotNull(b.getA());
        });
    }

}
