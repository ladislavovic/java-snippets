package cz.kul.snippets.spring._10_overriding;

import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.Assert.assertEquals;

public class TestOverriding_FromDefinitionInImportedConfig {

    @Configuration
    @Import({Importee.class})
    public static class Importer {

        @Bean
        public Bean1 foo() {
            return new Bean1("importer");
        }

    }

    @Configuration
    public static class Importee {

        @Bean
        public Bean1 foo() {
            return new Bean1("importee");
        }

    }

    @Test
    public void importerWins() {
        SpringTestUtils.runInSpring(Importer.class, ctx -> {
            // NOTE: it is OK, because bean is overriden so there is still only
            // one bean of type Bean1. in the context.
            Bean1 bean = ctx.getBean(Bean1.class);
            assertEquals("importer", bean.getVal());
        });
    }

}
