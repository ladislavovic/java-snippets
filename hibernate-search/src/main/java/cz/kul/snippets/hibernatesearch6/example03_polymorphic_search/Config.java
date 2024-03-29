package cz.kul.snippets.hibernatesearch6.example03_polymorphic_search;

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
        properties.put("hibernate.search.default.indexBase", TestPolymorphicSearch.TMP_DIR);
        properties.put("hibernate.search.model_mapping", searchConfiguration());
        return properties;
    }

    @Bean
    public SearchMapping searchConfiguration() {
        SearchMapping sm = new SearchMapping();
        sm.entity(AbstractPerson.class).property("wholeName", ElementType.METHOD).field().name("wholeName");
        sm.entity(AbstractPerson.class).property("name", ElementType.FIELD).field().name("name");
        sm.entity(Person.class).indexed();
        return sm;
    }
}
