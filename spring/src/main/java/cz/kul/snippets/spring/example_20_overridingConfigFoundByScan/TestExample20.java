package cz.kul.snippets.spring.example_20_overridingConfigFoundByScan;

import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

public class TestExample20 {

    @Configuration
    @ComponentScan("cz.kul.snippets.spring.example_20_overridingConfigFoundByScan.hierarchy")
    public static class Config {
    }

    @Test
    public void parentIsNOTAutomaticallyOerridenByChild() {
        // In this test spring found both configs - parent and child and create bean definitions.
        // But parent is found as the last and the last win.
        // The order is gien by package/class name but it is of course highly dependent on the
        // configuration, it is nothing you can rely on.
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            String a = (String) ctx.getBean("a");
            Assert.assertEquals("a_parent", a);
        });
    }
    
    @Test
    public void useImportIfYouNeedOverride() {
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            String a = (String) ctx.getBean("a");
            Assert.assertEquals("a_parent", a);
        });
    }
    
    
    
    
}
