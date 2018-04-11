package cz.kul.snippets.spring._10_overriding;

import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.Assert.assertEquals;

public class TestOverriding_Aliases {

    @Configuration
    @Import(Importee.class)
    public static class Config {

        @Bean(name = "b1Alias")
        public Bean1 b2Alias() {
            return new Bean1("fromConfig");
        }

        @Bean(name = {"foo", "b2Alias"})
        public Bean1 b2() {
            return new Bean1("fromConfig");
        }
    }

    @Configuration
    public static class Importee {

        @Bean(name = {"b1", "b1Alias"})
        public Bean1 b1() {
            return new Bean1("fromImportee");
        }

        @Bean(name = {"b2", "b2Alias"})
        public Bean1 b2() {
            return new Bean1("fromConfig");
        }

    }

    @Test
    public void aliasAlwaysOverridesId() {
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            Bean1 bean = (Bean1) ctx.getBean("b1Alias");
            assertEquals("fromImportee", bean.getVal());
        });
    }

    @Test
    public void aliasOverridesAlias() {
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            Bean1 bean = (Bean1) ctx.getBean("b2Alias");
            assertEquals("fromConfig", bean.getVal());
        });
    }

}
