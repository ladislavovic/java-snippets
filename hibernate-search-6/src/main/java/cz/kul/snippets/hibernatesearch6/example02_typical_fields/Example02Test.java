package cz.kul.snippets.hibernatesearch6.example02_typical_fields;

import com.google.gson.JsonObject;
import cz.kul.snippets.hibernatesearch6.commons.HibernateSearch6Test;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SuppressWarnings("Duplicates")
public class Example02Test extends HibernateSearch6Test {

	@Test
	public void testNormalFulltextField() {

		// Prepare data
		Long personId = jpaService().doInTransactionAndFreshEM(entityManager -> {
			Person person = new Person("Jana", "Novakova");
			entityManager.persist(person);
			return person.getId();
		});

		// Test current state
		assertEquals("Jana", getPersonField(personId, "name"));

		// Whene I update the name the fulltext is also updated
		jpaService().doInTransactionAndFreshSession(session -> {
			Person person = session.get(Person.class, personId);
			person.setName("Petra");
			return null;
		});
		assertEquals("Petra", getPersonField(personId, "name"));
	}

	@Test
	public void testKeywordField() {

		// Prepare data
		Long personId = jpaService().doInTransactionAndFreshEM(entityManager -> {
			Person person = new Person("Jana", "Novakova");
			person.setAge(30);
			entityManager.persist(person);
			return person.getId();
		});

		// Test current state
		assertEquals("30", getPersonField(personId, "age"));

		// Whene I update the name the fulltext is also updated
		jpaService().doInTransactionAndFreshSession(session -> {
			Person person = session.get(Person.class, personId);
			person.setAge(31);
			return null;
		});
		assertEquals("31", getPersonField(personId, "age"));
	}

	@Test
	public void testPropertyDerivedFromOtherFields() {

		// Prepare data
		Long personId = jpaService().doInTransactionAndFreshEM(entityManager -> {
			Person person = new Person("Jana", "Novakova");
			entityManager.persist(person);
			return person.getId();
		});

		// Test current state
		assertEquals("Jana Novakova", getPersonField(personId,"wholeName"));

		// Changing of the property "name" should trigger reindexing
		jpaService().doInTransactionAndFreshSession(session -> {
			Person person = session.get(Person.class, personId);
			person.setName("Petra");
			return null;
		});
		assertEquals("Petra Novakova", getPersonField(personId,"wholeName"));

		// Changing of the property "surname" should NOT trigger reindexing, because it is not in configuration
		jpaService().doInTransactionAndFreshSession(session -> {
			Person person = session.get(Person.class, personId);
			person.setSurname("Zavodna");
			return null;
		});
		assertEquals("Petra Novakova", getPersonField(personId,"wholeName"));

		// But when I change another indexed property it reindex the whole entity
		jpaService().doInTransactionAndFreshSession(session -> {
			Person person = session.get(Person.class, personId);
			person.setAge(20);
			return null;
		});
		assertEquals("Petra Zavodna", getPersonField(personId,"wholeName"));
	}

	@Test
	public void propertyNotDerivedFromOtherProperties() {
		// Prepare data
		Long personId = jpaService().doInTransactionAndFreshEM(entityManager -> {
			Person person = new Person("Jana", "Novakova");
			entityManager.persist(person);
			return person.getId();
		});
		String currentDate = getPersonField(personId, "currentDate");

		// When you change not indexed filed the indexing is not executed
		jpaService().doInTransactionAndFreshSession(session -> {
			Person person = session.get(Person.class, personId);
			person.setSex("foo");
			return null;
		});
		assertEquals(currentDate, getPersonField(personId, "currentDate"));

		// But when you change any indexed field it reindex the whole entity and also this not derived field
		jpaService().doInTransactionAndFreshSession(session -> {
			Person person = session.get(Person.class, personId);
			person.setAge(33);
			return null;
		});
		assertNotEquals(currentDate, getPersonField(personId, "currentDate"));
	}

	private String getPersonField(long personId, String fieldName) {
		return getPerson(personId).get(fieldName).getAsString();
	}

	private JsonObject getPerson(long personId) {
		return jpaService().doInTransactionAndFreshSession(session -> {
			SearchSession searchSession = Search.session(session);
			return searchSession.search(Person.class)
					.extension(ElasticsearchExtension.get())
					.select(f -> f.source())
					.where(f -> f.id().matching(personId))
					.fetchSingleHit()
					.get();
		});
	}

}
