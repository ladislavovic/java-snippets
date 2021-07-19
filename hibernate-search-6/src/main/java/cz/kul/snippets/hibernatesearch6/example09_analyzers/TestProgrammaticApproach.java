package cz.kul.snippets.hibernatesearch6.example09_analyzers;

import cz.kul.snippets.hibernatesearch6.commons.HibernateSearch6Test;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.hibernate.search.backend.elasticsearch.ElasticsearchBackend;
import org.hibernate.search.engine.backend.Backend;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.mapping.SearchMapping;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SuppressWarnings("Duplicates")
public class TestProgrammaticApproach extends HibernateSearch6Test {

    @Test
    public void testSearching() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            SearchMapping mapping = Search.mapping(entityManager.getEntityManagerFactory());
            Backend backend = mapping.backend();
            ElasticsearchBackend elasticsearchBackend = backend.unwrap( ElasticsearchBackend.class );
            RestClient client = elasticsearchBackend.client( RestClient.class );


            Request request = new Request("GET", "_analyze");
            request.setEntity(new NStringEntity(
                "{\"analyzer\":\"whitespace\", \"text\": \"brown fox\"}",
                ContentType.APPLICATION_JSON));


            try {
                Response response = client.performRequest(request);
                HttpEntity entity = response.getEntity();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                entity.writeTo(baos);
                System.out.println(new String(baos.toByteArray(), StandardCharsets.UTF_8));

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        });

    }

    List<String> analyze(EntityManager entityManager, String analyzerName, String text) {
        SearchMapping mapping = Search.mapping(entityManager.getEntityManagerFactory());
        Backend backend = mapping.backend();
        ElasticsearchBackend elasticsearchBackend = backend.unwrap( ElasticsearchBackend.class );
        RestClient client = elasticsearchBackend.client( RestClient.class );

        String payload = String.format("{\"analyzer\":\"%s\", \"text\": \"%s\"}", analyzerName, text);
        Request request = new Request("GET", "_analyze");
        request.setEntity(new NStringEntity(payload, ContentType.APPLICATION_JSON));

        try {
            Response response = client.performRequest(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                new ObjectMappe


            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            entity.writeTo(baos);
            System.out.println(new String(baos.toByteArray(), StandardCharsets.UTF_8));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
