package cz.kul.snippets.hibernatesearch.example05_elasticsearch;

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
        properties.put("hibernate.search.default.indexmanager", "elasticsearch");
        properties.put("hibernate.search.default.elasticsearch.host", "http://127.0.0.1:9200");
        properties.put("hibernate.search.elasticsearch.analysis_definition_provider", "cz.kul.snippets.hibernatesearch.example05_elasticsearch.AnalyzerDefinitionProvider");
        properties.put("hibernate.search.default.elasticsearch.index_schema_management_strategy", "create");
        properties.put("hibernate.search.default.elasticsearch.required_index_status", "yellow");
        properties.put("hibernate.search.default.elasticsearch.refresh_after_write", "true");
        properties.put("hibernate.search.model_mapping", searchConfiguration());
        return properties;
    }

    @Bean
    public SearchMapping searchConfiguration() {
        SearchMapping sm = new SearchMapping();

        // indexed() - it says this class should be indexed. But only "id" and "_hibernate_class" fiels
        //             are created, so you must add also filds for indexing to make the indexing
        //             useable in practise.
        sm.entity(Person.class).indexed();

        // indexName() - set name of the index. It is a directory, where the index is stored. Optional.
        sm.entity(Person.class).indexed().indexName("PersonIndex");
        
        // documentId() - Lucene document does not have id, only a internal one, but it is not
        //                important now. But hibernate-search store an field "id" with entity
        //                id during indexing. By default it use just entity id, by this methods
        //                you can configure it more. So it is optional.
        sm.entity(Person.class).indexed()
                .property("id", ElementType.FIELD)
                .documentId()
                .name("id_person"); // lucene field name                
        
        // add entity property to the index
        sm.entity(AbstractPerson.class)
                .property("wholeName", ElementType.METHOD) // Java property/field name 
                .field()
                .name("wholeNameLuceneField") // Lucene field name
                .analyzer("MY_DEFAULT_ANALYZER");
        return sm;
    }
}
