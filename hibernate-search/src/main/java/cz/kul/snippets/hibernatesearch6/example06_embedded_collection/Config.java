package cz.kul.snippets.hibernatesearch6.example06_embedded_collection;

import cz.kul.snippets.jpa.common.JPAConfig;
import org.hibernate.search.cfg.PropertyMapping;
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
        properties.put("hibernate.search.model_mapping", searchConfiguration());
        return properties;
    }

    @Bean
    public SearchMapping searchConfiguration() {
        SearchMapping sm = new SearchMapping();

        sm.entity(Car.class).indexed();
        PropertyMapping makePropertyMapping = sm.entity(Car.class).property("make", ElementType.FIELD);
        makePropertyMapping.field();
        PropertyMapping modelPropertyMapping = sm.entity(Car.class).property("model", ElementType.FIELD);
        modelPropertyMapping.field();
        PropertyMapping ownerPropertyMapping = sm.entity(Car.class).property("owner", ElementType.FIELD);
        ownerPropertyMapping.containedIn();

        sm.entity(DNA.class).indexed();
        PropertyMapping dnaPropertyMapping = sm.entity(DNA.class).property("code", ElementType.FIELD);
        dnaPropertyMapping.field();
        PropertyMapping typePropertyMapping = sm.entity(DNA.class).property("type", ElementType.FIELD);
        typePropertyMapping.field();

        // indexed() - it says this class should be indexed. But only "id" and "_hibernate_class" fields
        //             are created, so you need also add some other fields which you need fo the fulltext search.
        sm.entity(Person.class).indexed();

        // add entity property to the index
        sm.entity(Person.class)
            .property("name", ElementType.FIELD)
            .field();
        sm.entity(Person.class)
                .property("wholeName", ElementType.METHOD) // Java property/field name 
                .field()
                .name("wholeNameLuceneField"); // Lucene field name

        // add embedded property
        sm.entity(Person.class)
            .property("cars", ElementType.METHOD)
            .indexEmbedded()
            .prefix("car_")
            .includePaths("make");

        sm.entity(Person.class)
            .property("dna", ElementType.METHOD)
            .indexEmbedded()
            .prefix("dna_"); // NOTE: it cause the flat data structure in ES

        return sm;
    }
}
