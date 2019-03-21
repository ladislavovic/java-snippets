package cz.kul.snippets.spring._09_javaconfig_and_overriding;

import cz.kul.snippets.SnippetsTest;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.*;

public class TestOverridingByComponentScan extends SnippetsTest {
    
    @Configuration
    @ComponentScan({
            "cz.kul.snippets.spring._09_javaconfig_and_overriding.module11",
            "cz.kul.snippets.spring._09_javaconfig_and_overriding.module12"
    })
    public static class Config1 {
    }
    
    @Configuration
    @ComponentScan({
            "cz.kul.snippets.spring._09_javaconfig_and_overriding.module11",
    })
    public static class Config2 {
        
        @Bean(name = "a")
        public String aBean() {
            return "aBean";
        }
    }
    
    /**
     * You just can not do it. Component scan is performed at the beginning of init phase
     * and it is performed as a one step. If you have more beans with the same name,
     * it ends with an error.
     * You also can not override "explicit" bean definition by compoment scan, because
     * Component scan is performed always as a first.
     */
    @Test
    public void overrideInComponentScanFailBecauseThereIsNoDefinedOrder() {
        assertThrowsRootCause(IllegalStateException.class, () -> {
            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.register(Config1.class);
            ctx.refresh();
        });
    }
    
    @Test
    public void youCanOverrideBeanCreatedByComponentScan() {
        SpringTestUtils.runInSpring(Config2.class, ctx -> {
            Assert.assertEquals(String.class, ctx.getBean("a").getClass());
        });
        
    }
    
    
}
