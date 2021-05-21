package cz.kul.snippets.hibernatesearch6.example04_type_bridge;

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
public class TextExample04 extends HibernateSearch6Test {

	@Test
	public void test() {

		// Prepare data
		Object[] entities = jpaService().doInTransactionAndFreshEM(entityManager -> {
			AttributeSet attributeSet = new AttributeSet();
			entityManager.persist(attributeSet);

			Attribute attribute1 = new Attribute("attr1", "val1");
			attribute1.setAttributeSet(attributeSet);
			attributeSet.getAttributes().add(attribute1);
			entityManager.persist(attribute1);

			Attribute attribute2 = new Attribute("attr2", "val2");
			attribute2.setAttributeSet(attributeSet);
			attributeSet.getAttributes().add(attribute2);
			entityManager.persist(attribute2);

			Person person = new Person("Jana", "Novakova");
			person.setAttributeSet(attributeSet);
			attributeSet.setPerson(person);
			entityManager.persist(person);

			return new Object[]{person, attributeSet, attribute1, attribute2};
		});
		Person person = (Person) entities[0];
		AttributeSet attributeSet = (AttributeSet) entities[1];
		Attribute attribute1 = (Attribute) entities[2];
		Attribute attribute2 = (Attribute) entities[3];

		// Check current state
		assertEquals(Sets.newHashSet("val1", "val2"), getAttributeValues());

		// Attribute change must trigger reindex
		jpaService().doInTransactionAndFreshEM(entityManager -> {
			attribute1.setValue("val3");
			entityManager.merge(attribute1);
			return null;
		});
		assertEquals(Sets.newHashSet("val3", "val2"), getAttributeValues());
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
			JsonElement attributes = result.get("attributes");
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
