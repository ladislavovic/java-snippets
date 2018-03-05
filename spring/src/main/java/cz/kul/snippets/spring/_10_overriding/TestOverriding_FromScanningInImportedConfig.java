package cz.kul.snippets.spring._10_overriding;

import cz.kul.snippets.spring._10_overriding.module1.Module1Bean;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.Assert.assertEquals;

public class TestOverriding_FromScanningInImportedConfig {

    @Configuration
    @Import(Importee.class)
    public static class Config {

        @Bean
        public Module1Bean foo() {
            return new Module1Bean("config");
        }

    }

    @Configuration
    @ComponentScan("cz.kul.snippets.spring._10_overriding.module1")
    public static class Importee { }

    @Test
    public void importerWins() {
        SpringTestUtils.runInSpring(TestOverriding_FromScanningInImportedConfig.Config.class, ctx -> {
            Module1Bean bean = ctx.getBean(Module1Bean.class);
            assertEquals("config", bean.getVal());
        });
    }
}
