package cz.kul.snippets.hibernatesearch6.example04_class_bridge;

import cz.kul.snippets.jpa.common.JPAConfig;
import org.hibernate.search.cfg.SearchMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.ElementType;
import java.util.Map;

@Configuration
public class Config extends JPAConfig {

    @Override
    public Map<String, Object> additionalProperties() {
        Map<String, Object> properties = super.additionalProperties();
        properties.put("hibernate.search.default.directory_provider", "filesystem");
        properties.put("hibernate.search.default.indexBase", TestClassBridge.TMP_DIR);
        properties.put("hibernate.search.model_mapping", searchConfiguration());
        return properties;
    }

    @Bean
    public SearchMapping searchConfiguration() {
        SearchMapping sm = new SearchMapping();
        sm.entity(Person.class).indexed();
        sm.entity(Person.class).property("name", ElementType.FIELD).field().name("name");
        return sm;
    }
}
