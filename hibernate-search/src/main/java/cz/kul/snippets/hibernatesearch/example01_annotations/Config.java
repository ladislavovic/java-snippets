package cz.kul.snippets.hibernatesearch.example01_annotations;

import cz.kul.snippets.jpa.common.JPAConfig;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class Config extends JPAConfig {

    @Override
    public Map<String, Object> additionalProperties() {
        Map<String, Object> properties = super.additionalProperties();
        properties.put("hibernate.search.default.directory_provider", "filesystem");
        properties.put("hibernate.search.default.indexBase", TestIndexByAnnotations.TMP_DIR);
        return properties;
    }
}
