package cz.kul.snippets.spring._08_javaconfig;

import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.Bean2;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * TODO is the "alias overriding" the right term?
 * 
 * There is one big difference between alias overriding and name overriding.
 * 
 * When you create another bean definition with the same name, first definition
 * is overrided and bean acording to first definition is never created. So only
 * one bean is created
 * 
 * When you create another bean definition with alias which is the same as existing
 * alias Spring create bean according to both. So two beans are created.
 * 
 * // TODO when alias overrides bean id then overriden bean is not created and there is no message in the log about overriding. Weird.
 * // It is candidate to stack overflow question.
 */
public class _09_AliasesOverriding {

    @Configuration
    public static class Config1 {

        @Bean(name = {"b1_config1", "b1_alias"})
        public Bean1 b1() {
            return new Bean1("config1");
        }
        
        @Bean()
        public Bean2 b2() {
            return new Bean2();
        }
    }

    @Configuration
    public static class Config2 {

        @Bean(name = {"b1_config2", "b1_alias"})
        public Bean1 b1() {
            return new Bean1("config2");
        }

        @Bean()
        public Bean2 b2() {
            return new Bean2();
        }
    }

    @Configuration
    public static class Config3 {

        @Bean(name = {"b1_config3", "b1_config1"})
        public Bean1 b1() {
            return new Bean1("config3");
        }
    }
    
    @Test
    public void aliasDoesNotCauseBeanDefinitionOverriding() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            ctx.register(Config1.class);
            ctx.register(Config2.class);
            ctx.refresh();
            
            // NOTE: alias overriding does not cause bean definitin overriding so 2 instances
            assertEquals(2, ctx.getBeansOfType(Bean1.class).size());
            
            // NOTE: name overriding cause bean definitin overriding so 1 instances
            assertEquals(1, ctx.getBeansOfType(Bean2.class).size());
        }
    }
    
    @Test
    public void aliasOverridesId() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            ctx.register(Config3.class);
            ctx.register(Config1.class);
            ctx.refresh();

            // NOTE: alias always overrides id. Even though it is not last in config processing.
            assertEquals("config3", ((Bean1) ctx.getBean("b1_config1")).getVal());
        }
    }

    @Test
    public void aliasOverridesAlias() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            ctx.register(Config1.class);
            ctx.register(Config2.class);
            ctx.refresh();

            assertEquals("config2", ((Bean1) ctx.getBean("b1_alias")).getVal());
        }
    }
    
}
