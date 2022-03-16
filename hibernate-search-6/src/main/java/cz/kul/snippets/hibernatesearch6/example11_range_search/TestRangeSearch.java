package cz.kul.snippets.hibernatesearch6.example11_range_search;

import cz.kul.snippets.hibernatesearch6.commons.HibernateSearch6Test;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

@SuppressWarnings("Duplicates")
public class TestRangeSearch extends HibernateSearch6Test {

    @Test
    public void testSearching() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            entityManager.persist(new Person("Jana", 28L));
            entityManager.persist(new Person("Petra", 33L));
            return null;
        });
        List<Person> people = jpaService().doInTransactionAndFreshSession(session -> {

            String query = (
                "{" +
                "  'range': { " +
                "    'age': {" +
                "      'gte': 20," +
                "      'lte': 30" +
                "    }" +
                "  }" +
                "}"
            ).replaceAll("'", "\"");

            SearchSession searchSession = Search.session(session);
            return searchSession.search(Person.class)
                .extension(ElasticsearchExtension.get())
                .where(f -> f.fromJson(query))
                .fetchAllHits();
        });
        Assert.assertEquals(1, people.size());
        Assert.assertEquals("Jana", people.get(0).getName());
    }

}
