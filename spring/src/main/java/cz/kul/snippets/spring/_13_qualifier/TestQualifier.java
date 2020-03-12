package cz.kul.snippets.spring._13_qualifier;

import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.List;

public class TestQualifier {

    @Qualifier
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Cat1 {

    }

    @Configuration
    @ComponentScan("cz.kul.snippets.spring._13_qualifier")
    public static class Config {

        @Bean
        public String str1() {
            return "str1";
        }

        @Bean
        @Cat1
        public String str2() {
            return "str2";
        }

        @Bean
        @Qualifier("cat2")
        public String str3() {
            return "str3";
        }

        @Bean
        @Cat1
        @Qualifier("cat2")
        public String str4() {
            return "str4";
        }

        @Bean
        public Collection<String> cat1(@Cat1 List<String> cat1) {
            return cat1;
        }

        @Bean
        public Collection<String> cat2(@Qualifier("cat2") List<String> cat2) {
            return cat2;
        }

    }

    @Test
    public void qualifierByMyOwnAnnotation() {
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            Collection<String> cat1 = ctx.getBean("cat1", Collection.class);
            Assert.assertTrue(cat1.contains("str2"));
            Assert.assertTrue(cat1.contains("str4"));
        });
    }

    @Test
    public void qualifierBySimpleQualifier() {
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            Collection<String> cat2 = ctx.getBean("cat2", Collection.class);
            Assert.assertTrue(cat2.contains("str3"));
            Assert.assertTrue(cat2.contains("str4"));
        });
    }

}
