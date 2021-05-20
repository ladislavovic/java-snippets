package cz.kul.snippets.hibernatesearch6.example07_embedded_by_bridge;

import cz.kul.snippets.jpa.common.JPAConfig;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
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
        properties.put("hibernate.search.default.indexmanager", "elasticsearch");
        properties.put("hibernate.search.default.elasticsearch.host", "http://127.0.0.1:9200");
        properties.put("hibernate.search.default.elasticsearch.index_schema_management_strategy", "drop-and-create");
        properties.put("hibernate.search.default.elasticsearch.required_index_status", "yellow");
        properties.put("hibernate.search.default.elasticsearch.refresh_after_write", "true");
        properties.put("hibernate.search.default.elasticsearch.dynamic_mapping", "true");
        properties.put("hibernate.search.model_mapping", searchConfiguration());
        return properties;
    }

    @Bean
    public SearchMapping searchConfiguration() {
        SearchMapping sm = new SearchMapping();

        sm.entity(Person.class)
            .classBridge(PersonClassBridge.class)
            .name("bridge1")
            .index(Index.YES)
            .store(Store.YES)
            .indexed();
        // todo go through upper configuration

        sm.entity(Car.class)
            .property("owner", ElementType.FIELD)
            .containedIn();

        return sm;
    }
}
