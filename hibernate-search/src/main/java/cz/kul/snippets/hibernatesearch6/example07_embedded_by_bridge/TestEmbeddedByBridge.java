package cz.kul.snippets.hibernatesearch6.example07_embedded_by_bridge;

import cz.kul.snippets.hibernatesearch6.commons.HibernateSearchTest;
import org.hibernate.search.elasticsearch.ElasticsearchQueries;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.engine.spi.QueryDescriptor;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

@SuppressWarnings("Duplicates")
public class TestEmbeddedByBridge extends HibernateSearchTest {

	public static String TMP_DIR = "/tmp/hibernate-search-snippets/example7/lucene/indexes";

	@Override
	public String getTmpDir() {
		return TMP_DIR;
	}

	@Test
	public void test() {
		Long personId = jpaService().doInTransactionAndFreshEM(entityManager -> {
			Person person1 = new Person("Jana", "Novakova");
			entityManager.persist(person1);
			return person1.getId();
		});

		// Note because of "conainedIn" the Person object is reindexed
		jpaService().doInTransactionAndFreshEM(entityManager -> {
			Car car1 = new Car("Ford", "Mondeo");
			Car car2 = new Car("WV", "Passat");
			car1.setOwner(entityManager.getReference(Person.class, personId));
			car2.setOwner(entityManager.getReference(Person.class, personId));
			entityManager.persist(car1);
			entityManager.persist(car2);
			return null;
		});

		jpaService().doInTransactionAndFreshEM(em -> {
			FullTextEntityManager fullTextEm = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
			QueryDescriptor query = ElasticsearchQueries.fromQueryString("b_car_model:Mondeo");
			List<Person> people = (List<Person>) fullTextEm.createFullTextQuery(query, Person.class).getResultList();
			Assert.assertEquals(1, people.size());
			Assert.assertEquals("Jana", people.get(0).getName());
			return null;
		});
	}

	@Test
	public void test_wrongAlgorithmWhichCauseNotActualizedEmbeddedIndex() {
		jpaService().doInTransactionAndFreshEM(entityManager -> {
			// With this approach the Person index does not contains cars.
			//
			// It probably does not run Person indexing after Car persist, because
			// the Person is also created in this transaction so it is indexed anyway.

			Person person1 = new Person("Jana", "Novakova");
			entityManager.persist(person1);
			Car car1 = new Car("Ford", "Mondeo");
			Car car2 = new Car("WV", "Passat");
			car1.setOwner(person1);
			car2.setOwner(person1);
			entityManager.persist(car1);
			entityManager.persist(car2);
			return null;
		});

		jpaService().doInTransactionAndFreshEM(em -> {
			FullTextEntityManager fullTextEm = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
			QueryDescriptor query = ElasticsearchQueries.fromQueryString("b_car_model:Mondeo");
			List<Person> people = (List<Person>) fullTextEm.createFullTextQuery(query, Person.class).getResultList();
			Assert.assertEquals(0, people.size());
			return null;
		});

	}

}

