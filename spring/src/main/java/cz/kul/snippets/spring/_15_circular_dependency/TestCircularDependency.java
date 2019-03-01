package cz.kul.snippets.spring._15_circular_dependency;

import cz.kul.snippets.SnippetsTest;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertEquals;

public class TestCircularDependency extends SnippetsTest {

    @Configuration
    public static class Cfg {

        @Bean
        public A a() {
            return new A();
        }
        
        @Bean
        public B b() {
            return new B();
        }
    }
    
    public static class A {
        @Autowired
        B b;
    }
    
    public static class B {
        @Autowired
        A a;
    }

    @Test
    public void testForbiddingOfCircuralDependencyWay1() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(new NoCicrcularDependencyBeanFactory1())) {
            ctx.register(Cfg.class);
            assertThrows(UnsatisfiedDependencyException.class, () -> ctx.refresh());
        }
    }
    
    @Test
    public void testForbiddingOfCircuralDependencyWay2() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(new NoCircularDependencyBeanFactory2())) {
            ctx.register(Cfg.class);
            assertThrows(UnsatisfiedDependencyException.class, () -> ctx.refresh());
        }
    }
    
    public static class NoCicrcularDependencyBeanFactory1 extends DefaultListableBeanFactory {

        @Override
        protected <T> T doGetBean(String name, Class<T> requiredType, Object[] args, boolean typeCheckOnly) throws BeansException {
            T t = super.doGetBean(name, requiredType, args, typeCheckOnly);
            if (t != null) {
                if (isActuallyInCreation(name)) {
                    throw new IllegalStateException("Circular dependency!" + " Name: " + name + " type: " + t.getClass().getName());
                }
            }
            return t;
        }
    }
    
    public static class NoCircularDependencyBeanFactory2 extends DefaultListableBeanFactory {
        
        public NoCircularDependencyBeanFactory2() {
            setAllowCircularReferences(false);
        }
        
    }
    
}
