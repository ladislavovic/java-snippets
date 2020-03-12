package cz.kul.snippets.spring._17_priority;

import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;

/**
 * In comparison with @Primary you can always make "more priority" component.
 * The disadvantage is you can not use it on spring configuration factory method,
 * it is usable on type only.
 */
public class TestPriority {
    
    @Component
    @Priority(1)
    public static class One extends Bean1 {
        
    }
    
    @Component
    @Priority(2)
    public static class Two extends Bean1 {
        
    }
    
    @Component
    public static class No extends Bean1 {
        
    }
    
    @Configuration
    @ComponentScan("cz.kul.snippets.spring._17_priority")
    public static class Config {
        
    }
    
    @Test
    public void lowerNumberIsHigherPriority() {
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            Bean1 bean = ctx.getBean(Bean1.class);
            Assert.assertEquals(One.class, bean.getClass());
        });
    }
    
}
