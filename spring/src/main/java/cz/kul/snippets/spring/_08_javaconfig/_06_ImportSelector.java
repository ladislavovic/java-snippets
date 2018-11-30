package cz.kul.snippets.spring._08_javaconfig;

import cz.kul.snippets.spring.common.Annotation1;
import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import static org.junit.Assert.assertNotNull;

// TODO simplify
public class _06_ImportSelector {

    @Configuration
    @Import(Config3.class)
    public static class Config1 {

        @Bean
        public Bean1 config1Bean() {
            return new Bean1("config1");
        }
    }

    @Configuration
    @Import(Config3.class)
    public static class Config2 {

        @Bean
        public Bean1 config2Bean() {
            return new Bean1("config2");
        }
    }

    @Configuration
    public static class Config3 {

        @Bean
        public Bean1 config3Bean() {
            return new Bean1("config3");
        }
    }

    public static class MyImportSelector implements ImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                    importingClassMetadata.getAnnotationAttributes(
                            Annotation1.class.getName(), false));

            String value = attributes.getString("value");

            String [] configurations = "1".equals(value) ?
                    new String[] {Config1.class.getName()} :
                    new String[] {Config2.class.getName()};
            return configurations;
        }
    }

    @Configuration
    @Annotation1("1")
    @Import(MyImportSelector.class)
    public static class AppConfig {
    }

    @Test
    public void configurationIsImportedConditionally() {
        SpringTestUtils.runInSpring(AppConfig.class, ctx -> {
            Object bean = ctx.getBean("config1Bean");
            assertNotNull(bean);
        });
    }

    @Test
    public void transitiveImportsWork() {
        SpringTestUtils.runInSpring(AppConfig.class, ctx -> {
            Object bean = ctx.getBean("config3Bean");
            assertNotNull(bean);
        });
    }

}
