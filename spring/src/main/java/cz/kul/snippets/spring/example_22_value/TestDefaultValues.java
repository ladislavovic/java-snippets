package cz.kul.snippets.spring.example_22_value;

import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestDefaultValues {

    @Configuration
    @PropertySource(
            name = "propertyPlaceholderConfigurer",
            value = {"classpath:example22.properties"},
            ignoreResourceNotFound = true)
    public static class Conf {

        // Error: IllegalArgumentException: Could not resolve placeholder 'val0'
        //
        // @Value("${val0}")
        //private String val0;

        // It is OK. Property val0 is missing but there is an default value
        @Value("${val0:def}")
        private String val0_def;
        
        @Value("${val0:#{null}}")
        private String val0_def_null;
        
        @Value("${val1}")
        private String val1;
        
        @Value("${val1}")
        private Integer val1_Integer;
        
        // Error: NumberFormatException: For input string: ""
        //
        // @Value("${val1}")
        // private int val1_int;
        
        // Again not working, the default value is not applied because there is a value: ""
        //
        // @Value("${val1:42}")
        // private int val1_int_def;

        // Here the value will be "", because val1 is present with "" value, so the default is not used
        @Value("${val1:'def'}")
        private String val1_def;

        @Bean
        public Map<String, Object> values() {
            Map<String, Object> values = new HashMap<>();
            values.put("val0_def", val0_def);
            values.put("val0_def_null", val0_def_null);
            values.put("val1", val1);
            values.put("val1_Integer", val1_Integer);
            values.put("val1_def", val1_def);
            return values;
        }

        // NOTE: It must be here to resolve ${} in @Value annotations.
        @Bean
        public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }

    @Test
    public void defaultValues() {
        // Fixed delay means the test is run again after the given delay after
        // the previous run ends.
        SpringTestUtils.runInSpring(Conf.class, ctx -> {
            Map<String, Object> values = ctx.getBean("values", Map.class);
            assertEquals("def", values.get("val0_def"));
            assertEquals(null, values.get("val0_def_null"));
            assertEquals("", values.get("val1"));
            assertEquals(null, values.get("val1_Integer"));
            assertEquals("", values.get("val1_def"));
        });
    }

}
