package cz.kul.snippets.spring._08_javaconfig;

import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class _10_CallingBeanFactoryMethod {

    @Configuration
    public static class Cfg {

        @Bean
        public Bean1 factoryMethod() {
            return new Bean1();
        }
        
        public Bean1 notFactoryPublicMethod() {
            return factoryMethod();
        }
        
        public Bean1 notFactoryPublicMethod2() {
            return notFactoryPrivateMethod();
        }
        
        private Bean1 notFactoryPrivateMethod() {
            return factoryMethod();
        }
    }

    @Test
    public void test_beanMethodCreatesBeanOnlyFirstTimeBecauseOfProxy() {
        SpringTestUtils.runInSpring(Cfg.class, ctx -> {
            Cfg cfg = ctx.getBean(Cfg.class);
            Assert.assertTrue(cfg.factoryMethod() == cfg.factoryMethod());
        });
    }
    
    @Test
    public void test_itWorksAlsoWhenAMethodDoesNotHaveBeanAnnotation() {
        SpringTestUtils.runInSpring(Cfg.class, ctx -> {
            Cfg cfg = ctx.getBean(Cfg.class);
            Assert.assertTrue(cfg.notFactoryPublicMethod() == cfg.notFactoryPublicMethod());
        });
    }
    
    @Test
    public void test_itWorksWithPrivateMethodWell() {
        SpringTestUtils.runInSpring(Cfg.class, ctx -> {
            Cfg cfg = ctx.getBean(Cfg.class);
            Assert.assertTrue(cfg.notFactoryPublicMethod2() == cfg.notFactoryPublicMethod2());
        });
    }
    
}
