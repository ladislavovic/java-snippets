package cz.kul.snippets.spring._15_circular_dependency;

import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertEquals;

public class TestCircularDependency {

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
    public void testCircularDependency() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(new MyBeanFactory1())) {
            ctx.register(Cfg.class);
            ctx.refresh();
            A a = ctx.getBean(A.class);
            B b = ctx.getBean(B.class);
            assertEquals(a, b.a);
            assertEquals(b, a.b);
        }
    }
    
    public static class MyBeanFactory1 extends DefaultListableBeanFactory {

        @Override
        protected <T> T doGetBean(String name, Class<T> requiredType, Object[] args, boolean typeCheckOnly) throws BeansException {
            T t = super.doGetBean(name, requiredType, args, typeCheckOnly);
            if (t != null) {
                if (isActuallyInCreation(name)) {
                    throw new IllegalStateException("Circular dependency!" + " Name: " + name + " type: " + t.getClass().getName());
//                    System.out.println("CD detected!" + " Name: " + name + " type: " + t.getClass().getName());
                }
            }
            return t;
        }
    }
    
    public static class MyBeanFactory2 extends DefaultListableBeanFactory {
        
        public MyBeanFactory2() {
            setAllowCircularReferences(false);
        }
        
    }
    
}
