package cz.kul.snippets.hibernatesearch.example05_elasticsearch;

import cz.kul.snippets.hibernatesearch.commons.HibernateSearchTest;
import org.apache.lucene.search.Query;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

@SuppressWarnings("Duplicates")
public class TestElasticsearch extends HibernateSearchTest {

    public static String TMP_DIR = "/tmp/hibernate-search-snippets/example2/lucene/indexes";

    @Override
    public String getTmpDir() {
        return TMP_DIR;
    }

    @Test
    public void testSearching() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            entityManager.persist(new Person("Jana", "Novakova"));
            entityManager.persist(new Person("Petra", "Zavodna"));
            entityManager.persist(new ArtificialPerson("LK s.r.o."));
            return null;
        });
        List<? extends AbstractPerson> people = jpaService().doInTransactionAndFreshSession(session -> {
            FullTextSession fullTextSession = Search.getFullTextSession(session);
            QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(AbstractPerson.class).get();

            Query luceneQuery = queryBuilder
                    .keyword()
                    .onFields("wholeNameLuceneField")
                    .matching("Petra")
                    .createQuery();

            FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, AbstractPerson.class);
            List<AbstractPerson> results = fullTextQuery.list();
            return results;
        });
        Assert.assertEquals(1, people.size());
    }

}
