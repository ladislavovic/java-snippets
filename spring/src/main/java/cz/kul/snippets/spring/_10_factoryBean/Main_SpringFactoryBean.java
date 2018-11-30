package cz.kul.snippets.spring._10_factoryBean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Using a FactoryBean can be a good practice to encapsulate complex construction logic 
 * or make configuring highly configurable objects easier in Spring.
 */
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class Main_SpringFactoryBean {

    public static void main(String[] args) {
        factoryFromInterface();
        factoryFromAbstractFactoryBean();
    }
    
    private static void factoryFromInterface() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            ctx.register(AppConfig.class);
            ctx.refresh();
            
            // This way you can get a bean produced by factory
            Bean1 foo = (Bean1) ctx.getBean("Bean1");
            assertEquals("fooId", foo.getId());
            
            // This way you can get a factory directly
            Factory_FromInterface factory = (Factory_FromInterface) ctx.getBean("&Bean1");
            assertEquals("factoryId", factory.getFactoryId());
        }
    }
    
    private static void factoryFromAbstractFactoryBean() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            ctx.register(AppConfig.class);
            ctx.refresh();
            Bean1 foo1 = (Bean1) ctx.getBean("foo2");
            Bean1 foo2 = (Bean1) ctx.getBean("foo2");
            assertTrue(foo1 != foo2);
        }
    }
    
    @Configuration
    static class AppConfig {
      
        @Bean(name = "Bean1")
        public Factory_FromInterface fooFactory() {
            Factory_FromInterface factory = new Factory_FromInterface();
            factory.setFactoryId("factoryId");
            factory.setFooId("fooId");
            return factory;
        }
        
        @Bean(name = "Bean2")
        public Factory_FromAbstractFactoryBean fooFactory2() {
            Factory_FromAbstractFactoryBean factory = new Factory_FromAbstractFactoryBean();
            return factory;
        }

    }
    

}
