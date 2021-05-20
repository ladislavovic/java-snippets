package cz.kul.snippets.hibernatesearch6.example02_method_property;

import com.google.gson.JsonObject;
import cz.kul.snippets.hibernatesearch6.commons.HibernateSearch6Test;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("Duplicates")
public class Example02Test extends HibernateSearch6Test {

	@org.junit.Test
	public void testReindexingTriggering() {

		// Prepare data
		Long personId = jpaService().doInTransactionAndFreshEM(entityManager -> {
			Person person = new Person("Jana", "Novakova");
			entityManager.persist(person);
			return person.getId();
		});

		// Test current state
		assertEquals("Jana Novakova", getWholeName());

		// Changing of the property "name" should trigger reindexing
		jpaService().doInTransactionAndFreshSession(session -> {
			Person person = session.get(Person.class, personId);
			person.setName("Petra");
			return null;
		});
		assertEquals("Petra Novakova", getWholeName());

		// Changing of the property "surname" should NOT trigger reindexing, because it is not in configuration
		jpaService().doInTransactionAndFreshSession(session -> {
			Person person = session.get(Person.class, personId);
			person.setSurname("Zavodna");
			return null;
		});
		assertEquals("Petra Novakova", getWholeName());
	}

	private String getWholeName() {
		return jpaService().doInTransactionAndFreshSession(session -> {
			SearchSession searchSession = Search.session(session);
			JsonObject result = searchSession.search(Person.class)
					.extension(ElasticsearchExtension.get())
					.select(f -> f.source())
					.where(f -> f.match().field("wholeName").matching("novakova"))
					.fetchSingleHit()
					.get();
			return result.get("wholeName").getAsString();
		});

	}

}
