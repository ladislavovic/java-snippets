package cz.kul.snippets.spring._09_javaconfig_and_overriding;

import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import static org.junit.Assert.assertEquals;

public class TestOverriding_FromDefinitionInXmlConfig {

    @Configuration
    @ImportResource("10_01_spring.xml")
    public static class Importer {

        @Bean
        public Bean1 foo() {
            return new Bean1("importer");
        }

    }

    /**
     * This is difference in coomparison with Java configuration.
     * You can normally override bean configured in imported
     * Java config, but you can't do it for bean configured in
     * imported xml config.
     *
     * https://jira.spring.io/browse/SPR-7028
     */
    @Test
    public void xmlWins() {
        SpringTestUtils.runInSpring(Importer.class, ctx -> {
            Bean1 bean = ctx.getBean(Bean1.class);
            assertEquals("xml", bean.getVal());
        });
    }

}
