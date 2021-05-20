package cz.kul.snippets.hibernatesearch6.example01_programmatic_hw;

import cz.kul.snippets.hibernatesearch6.commons.HibernateSearch6Test;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

@SuppressWarnings("Duplicates")
public class TestProgrammaticApproach extends HibernateSearch6Test {

    @Test
    public void testSearching() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            entityManager.persist(new Person("Jana", "Novakova"));
            entityManager.persist(new Person("Petra", "Zavodna"));
            return null;
        });
        List<Person> people = jpaService().doInTransactionAndFreshSession(session -> {
            SearchSession searchSession = Search.session(session);
            List<Person> result = searchSession.search(Person.class)
                .where(f -> f.match().field("name").matching("jana"))
                .fetchAllHits();
            return result;
        });
        Assert.assertEquals(1, people.size());
        Assert.assertEquals("Jana", people.get(0).getName());
    }

}
