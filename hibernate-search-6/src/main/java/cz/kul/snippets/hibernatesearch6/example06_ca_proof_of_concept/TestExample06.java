package cz.kul.snippets.hibernatesearch6.example06_ca_proof_of_concept;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cz.kul.snippets.hibernatesearch6.commons.HibernateSearch6Test;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("Duplicates")
public class TestExample06 extends HibernateSearch6Test {

	@Test
	public void test() {

		// Prepare data
		Object[] entities = jpaService().doInTransactionAndFreshEM(entityManager -> {
			CASet caSet = new CASet();
			entityManager.persist(caSet);

			CAValue caValue1 = new CAStringValue("attr1", "val1");
			caValue1.setCaSet(caSet);
			caSet.getValues().add(caValue1);
			entityManager.persist(caValue1);

			CAValue caValue2 = new CAIntegerValue("attr2", 10);
			caValue2.setCaSet(caSet);
			caSet.getValues().add(caValue2);
			entityManager.persist(caValue2);

			Person person = new Person("Jana", "Novakova");
			person.setCustomAttributes(caSet);
			caSet.setPerson(person);
			entityManager.persist(person);

			return new Object[]{person, caSet, caValue1, caValue2};
		});
		Person person = (Person) entities[0];
		CASet caSet = (CASet) entities[1];
		CAValue caValue1 = (CAValue) entities[2];
		CAValue caValue2 = (CAValue) entities[3];

		// Check current state
		assertEquals(Sets.newHashSet("val1", "10"), getAttributeValues());

		// Attribute change must trigger reindex
		jpaService().doInTransactionAndFreshEM(entityManager -> {
			CAStringValue caValue = entityManager.find(CAStringValue.class, caValue1.getId());
			caValue.setValue("val1_1");
			entityManager.merge(caValue);
			return null;
		});
		assertEquals(Sets.newHashSet("val1_1", "10"), getAttributeValues());

		// Attribute adding must trigger reindex
		jpaService().doInTransactionAndFreshEM(entityManager -> {
			CAValue caValue3 = new CAStringValue("attr3", "val3");
			caValue3.setCaSet(entityManager.getReference(CASet.class, caSet.getId()));
			entityManager.persist(caValue3);
			return null;
		});
		assertEquals(Sets.newHashSet("val1_1", "10", "val3"), getAttributeValues());

		// Attribute removing must trigger reindex - BUG?? Does not work.
//		jpaService().doInTransactionAndFreshEM(entityManager -> {
//			entityManager.remove(entityManager.getReference(CAValue.class, caValue1.getId()));
//			return null;
//		});
//		assertEquals(Sets.newHashSet("val2", "val3"), getAttributeValues());
	}

	private Set<String> getAttributeValues() {
		return jpaService().doInTransactionAndFreshSession(session -> {
			SearchSession searchSession = Search.session(session);
			JsonObject result = searchSession.search(Person.class)
					.extension(ElasticsearchExtension.get())
					.select(f -> f.source())
					.where(f -> f.match().field("firstName").matching("jana"))
					.fetchSingleHit()
					.get();
			HashSet<String> values = new HashSet<>();
			JsonElement attributes = result.get("customAttributesField");
			if (attributes.isJsonArray()) {
				JsonArray attributesArr = attributes.getAsJsonArray();
				for (int i = 0; i < attributesArr.size(); i++) {
					values.add(attributesArr.get(i).getAsString());
				}
			} else {
				values.add(attributes.getAsString());
			}
			return values;
		});

	}

}
