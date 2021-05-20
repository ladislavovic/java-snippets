package cz.kul.snippets.hibernatesearch6.example04_class_bridge;

import cz.kul.snippets.hibernatesearch6.commons.HibernateSearchTest;
import org.apache.lucene.search.Query;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.Test;

import java.util.List;

@SuppressWarnings("Duplicates")
public class TestClassBridge extends HibernateSearchTest {

    public static String TMP_DIR = "/var/hibernate-search-snippets/example4/lucene/indexes";

    @Override
    public String getTmpDir() {
        return TMP_DIR;
    }

    @Override
    public Class<?>[] getConfigClasses() {
        return new Class[] {Config.class};
    }

    @Test
    public void testSearching() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            entityManager.persist(new Person("Jana", "Novakova"));
            entityManager.persist(new Person("Petra", "Zavodna"));
            return null;
        });
        List<? extends Person> people = jpaService().doInTransactionAndFreshSession(session -> {
            FullTextSession fullTextSession = Search.getFullTextSession(session);
            QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Person.class).get();
            Query luceneQuery = queryBuilder
                    .keyword()
                    .onField("name")
                    .matching("Petra")
                    .createQuery();
            FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, Person.class);
            List<Person> results = fullTextQuery.list();
            return results;
        });
//        Assert.assertEquals(1, people.size());
    }

}
