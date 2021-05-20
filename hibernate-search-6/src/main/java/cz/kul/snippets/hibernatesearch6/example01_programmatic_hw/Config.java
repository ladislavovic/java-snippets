package cz.kul.snippets.hibernatesearch6.example01_programmatic_hw;

import cz.kul.snippets.jpa.common.JPAConfig;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class Config extends JPAConfig {

    @Override
    public Map<String, Object> additionalProperties() {
        Map<String, Object> properties = super.additionalProperties();
        properties.put("hibernate.search.backend.type", "elasticsearch");
        properties.put("hibernate.search.backend.hosts", "localhost:9200");
        properties.put("hibernate.search.backend.protocol", "http");
        properties.put("hibernate.search.mapping.process_annotations", "false");
        properties.put("hibernate.search.backend.dynamic_mapping", "true");
        properties.put("hibernate.search.automatic_indexing.synchronization.strategy", "sync");
        properties.put("hibernate.search.mapping.configurer", MappingConfigurer.class.getName());
        return properties;
    }

}
