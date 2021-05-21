package cz.kul.snippets.hibernatesearch6.example06_embedded_collection;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cz.kul.snippets.hibernatesearch6.commons.HibernateSearchTest;
import org.apache.lucene.search.Query;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.elasticsearch.ElasticsearchProjectionConstants;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("Duplicates")
public class TestEmbeddedCollection extends HibernateSearchTest {

	public static String TMP_DIR = "/tmp/hibernate-search-snippets/example6/lucene/indexes";

	@Override
	public String getTmpDir() {
		return TMP_DIR;
	}

	@Test
	public void test() {

		// Prepare data
		Object[] entities = jpaService().doInTransactionAndFreshEM(entityManager -> {
			Person person = new Person("Jana", "Novakova");
			entityManager.persist(person);
			Car car1 = new Car("Ford", "Mondeo", person);
			entityManager.persist(car1);
			person.getCars().add(car1);
			entityManager.merge(person);
			return new Object[] {person, car1};
		});
		Person person = (Person) entities[0];
		Car car1 = (Car) entities[1];

		// Check current state
		assertEquals(Sets.newHashSet("Ford"), getCars());

		// Car change should cause Person reindexing
		jpaService().doInTransactionAndFreshEM(entityManager -> {
			car1.setMake("Honda");
			entityManager.merge(car1);
			return null;
		});
		assertEquals(Sets.newHashSet("Honda"), getCars());

		// Adding of car should cause reindexing
		Car car2 = jpaService().doInTransactionAndFreshEM(entityManager -> {
			Person personRef = entityManager.getReference(Person.class, person.getId());
			Car p = new Car("Toyota", "RAV4", personRef);

//		Car p = new Car("Toyota", "RAV4", person);
//    Pitfall - this would not not work.
//    By this approach the Person entity is probably added to the entity manager and
//    fulltext reindexing is based on its state. And it has not the new car in its collection
//    so it is not added to the index.

			entityManager.persist(p);
			return p;
		});
		assertEquals(Sets.newHashSet("Honda", "Toyota"), getCars());

		// Car removing also cause Person reindexing
		// BUG - does not work
		jpaService().doInTransactionAndFreshEM(entityManager -> {
			entityManager.remove(entityManager.getReference(Car.class, car1.getId()));
			return null;
		});
		assertEquals(Sets.newHashSet("Toyota"), getCars());
	}

	private Set<String> getCars() {
		return jpaService().doInTransactionAndFreshSession(session -> {
			FullTextSession fullTextSession = Search.getFullTextSession(session);
			QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Person.class).get();

			Query luceneQuery = queryBuilder
					.keyword()
					.onFields("name")
					.matching("Jana")
					.createQuery();

			FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, Person.class);
			fullTextQuery.setProjection(ElasticsearchProjectionConstants.SOURCE);
			List result = fullTextQuery.list();
			String source = (String) ((Object[]) result.get(0))[0];
			JsonObject sourceJson = new JsonParser().parse(source).getAsJsonObject();
			JsonArray makes = sourceJson.get("car_make").getAsJsonArray();
			HashSet<String> makesSet = new HashSet<>();
			for (JsonElement make : makes) {
				String makeString = make.getAsString();
				makesSet.add(makeString);
			}
			return makesSet;
		});
	}

}
