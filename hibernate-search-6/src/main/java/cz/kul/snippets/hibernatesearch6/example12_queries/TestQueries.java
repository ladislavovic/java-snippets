package cz.kul.snippets.hibernatesearch6.example12_queries;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.sun.xml.bind.v2.model.core.ID;
import cz.kul.snippets.hibernatesearch6.commons.HibernateSearch6Test;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.RegexpQuery;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.backend.lucene.LuceneExtension;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("Duplicates")
public class TestQueries extends HibernateSearch6Test {

    private static boolean dataPrepared = false;

    private static long P0_ID;
    private static long P1_ID;
    private static long P2_ID;
    private static long P3_ID;
    private static long P4_ID;
    private static long E0_ID;
    private static long E1_ID;

    @Before
    public void prepareData() {
        if (!dataPrepared) {
            dataPrepared = true;
            jpaService().doInTransactionAndFreshEM(entityManager -> {
                Person p0 = new Person("Adelle Smith", 30L);
                Person p1 = new Person("Brigitte Smith", 31L);
                Person p2 = new Person("Cecilia Smith", 32L);
                Person p3 = new Person("Danielle Smith", 33L);
                Person p4 = new Person("Estell Smith", 34L);
                Employee e0 = new Employee("Adelle");
                Employee e1 = new Employee("Brigitte");

                entityManager.persist(p0);
                entityManager.persist(p1);
                entityManager.persist(p2);
                entityManager.persist(p3);
                entityManager.persist(p4);
                entityManager.persist(e0);
                entityManager.persist(e1);

                P0_ID = p0.getId();
                P1_ID = p1.getId();
                P2_ID = p2.getId();
                P3_ID = p3.getId();
                P4_ID = p4.getId();
                E0_ID = e0.getId();
                E1_ID = e1.getId();

                return null;
            });
        }
    }

    @Test
    public void testBasicQuery() {
        SearchResult<Person> result = jpaService().doInTransactionAndFreshSession(session -> {

            SearchSession searchSession = Search.session(session); // 1
            SearchResult<Person> searchResult = searchSession.search(Person.class) // 2
                .where(f -> f.match().field("name").matching("Danielle"))
                .fetch(20); // 3
            return searchResult;

            /*
            1 - get a Hibernate Search Session. From EntityManager.
            2 - specify which entity you want to search
            3 - build & execute query. The result contains hits, total hits and some metadata
             */
        });
        assertEquals(1, result.total().hitCount());
        Person person = result.hits().get(0);
        assertEquals("Danielle", person.getName());
        assertEquals(P3_ID, (long) person.getId());
    }

    @Test
    public void testSearchMultipleEntities() {
        // You can search across multiple entities by one query

        SearchResult<Object> result = jpaService().doInTransactionAndFreshSession(session -> {

            SearchSession searchSession = Search.session(session);
            SearchResult<Object> searchResult = searchSession.search(Lists.newArrayList(Person.class, Employee.class))
                .where(f -> f.match().field("name").matching("Adelle"))
                .fetch(20);
            return searchResult;

        });
        assertEquals(2, result.total().hitCount());
        HashSet<Object> hits = new HashSet<>(result.hits());
        assertTrue(hits.stream().anyMatch(x -> x instanceof Person));
        assertTrue(hits.stream().anyMatch(x -> x instanceof Employee));
    }

    @Test
    public void testPagination() {
        SearchResult<Person> result = jpaService().doInTransactionAndFreshSession(session -> {

            SearchSession searchSession = Search.session(session);
            SearchResult<Person> searchResult = searchSession.search(Person.class)
                .where(f -> f.match().field("name").matching("Smith"))
                .fetch(1, 2); // offset 1, limit 2
            return searchResult;

        });
        assertEquals(5, result.total().hitCount());
        assertEquals(2, result.hits().size());
    }

    @Test
    public void testBooleanPredicate() {
        SearchResult<Person> result = jpaService().doInTransactionAndFreshSession(session -> {

            SearchSession searchSession = Search.session(session);
            SearchResult<Person> searchResult = searchSession.search(Person.class)
                .where(f -> f.bool()
                    .must(f.match().field("name").matching("Smith"))
                    .should(f.match().field("name").matching("Cecilia"))
                )
                .fetch(20);
            return searchResult;

        });
        assertEquals(5, result.total().hitCount());
        assertEquals("Cecilia Smith", result.hits().get(0).getName());
    }

    @Ignore("It looks like it is possible only with lucene backend")
    @Test
    public void testQueryFromLuceneQuery() {
        List<Person> result = jpaService().doInTransactionAndFreshSession(session -> {

            SearchSession searchSession = Search.session(session);
            List<Person> searchResult = searchSession.search(Person.class)
                .extension(LuceneExtension.get())
                .where(f -> f.fromLuceneQuery(
                    new RegexpQuery(new Term("name", "Ade.*"))
                ))
                .fetchHits(20);
            return searchResult;

        });
        assertEquals(1, result.size());
        assertEquals("Adelle Smith", result.get(0).getName());
    }

    @Test
    public void testOneFieldProjection() {
        List<String> result = jpaService().doInTransactionAndFreshSession(session -> {

            SearchSession searchSession = Search.session(session);
            List<String> searchResult = searchSession.search(Person.class)
                .select(f -> f.field("name", String.class))
                .where(f -> f.matchAll())
                .fetchHits(20);
            return searchResult;

        });
        assertTrue(result.contains("Adelle Smith"));
    }

    @Test
    public void testMoreFieldsProjection() {
        List<List<?>> result = jpaService().doInTransactionAndFreshSession(session -> {

            SearchSession searchSession = Search.session(session);
            List<List<?>> searchResult = searchSession.search(Person.class)
                .select(f -> f.composite(
                    f.field("name", String.class),
                    f.field("age", Long.class)))
                .where(f -> f.matchAll())
                .fetchHits(20);
            return searchResult;

        });
        assertTrue(result.contains(Lists.newArrayList("Adelle Smith", 30L)));
    }

    @Test
    public void testElasticsearchSourceProjection() {
        List<JsonObject> result = jpaService().doInTransactionAndFreshSession(session -> {

            SearchSession searchSession = Search.session(session);
            List<JsonObject> searchResult = searchSession.search(Person.class)
                .extension(ElasticsearchExtension.get())
                .select(f -> f.source())
                .where(f -> f.matchAll())
                .fetchHits(20);
            return searchResult;

        });
        assertTrue(result.stream()
            .map(json -> json.get("name").getAsString())
            .anyMatch("Estell Smith"::equals));
    }

}
