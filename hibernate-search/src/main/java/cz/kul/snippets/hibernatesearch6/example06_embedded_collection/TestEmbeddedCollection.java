package cz.kul.snippets.hibernatesearch6.example06_embedded_collection;

import cz.kul.snippets.hibernatesearch6.commons.HibernateSearchTest;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class TestEmbeddedCollection extends HibernateSearchTest {

    public static String TMP_DIR = "/tmp/hibernate-search-snippets/example6/lucene/indexes";

    @Override
    public String getTmpDir() {
        return TMP_DIR;
    }

    @Test
    public void testSearching() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Person person1 = new Person("Jana", "Novakova");
            Person person2 = new Person("Rachel", "Green");
            entityManager.persist(person1);
            entityManager.persist(person2);

            Car car1 = new Car("Ford", "Mondeo");
            car1.setOwner(person1);
            entityManager.persist(car1);

            Car car2 = new Car("VW", "Passat");
            car2.setOwner(person2);
            entityManager.persist(car2);

            Car car3 = new Car("VW", "Tiguan");
            car3.setOwner(person2);
            entityManager.persist(car3);

            person1.getCars().add(car1);
            person2.getCars().add(car2);
            person2.getCars().add(car3);
            entityManager.merge(person1);
            entityManager.merge(person2);

            DNA dna = new DNA("abcde", "A");
            entityManager.persist(dna);
            person1.setDna(dna);
            entityManager.merge(person1);

            return null;
        });

        List<? extends Person> people = jpaService().doInTransactionAndFreshSession(session -> {
            FullTextSession fullTextSession = Search.getFullTextSession(session);
            QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Person.class).get();

//            Query luceneQuery = queryBuilder
//                    .keyword()
//                    .onFields("cars.make")
//                    .matching("VW")
//                    .createQuery();
//
//            FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, Person.class);
//            List<Person> results = fullTextQuery.list();
//            return results;
            return new ArrayList<>();
        });
        Assert.assertEquals(1, people.size());
        Assert.assertEquals("Rachel", people.get(0).getName());
    }

}
