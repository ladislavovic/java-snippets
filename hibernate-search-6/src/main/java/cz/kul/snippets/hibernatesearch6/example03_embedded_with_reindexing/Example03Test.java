package cz.kul.snippets.hibernatesearch6.example03_embedded_with_reindexing;

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
public class Example03Test extends HibernateSearch6Test {

	@Test
	public void test() {

		// Prepare data
		Object[] entities = jpaService().doInTransactionAndFreshEM(entityManager -> {
			Person person = new Person("Jana", "Novakova");
			entityManager.persist(person);
			Phone phone1 = new Phone("111 111 111", person);
			entityManager.persist(phone1);
			person.getPhones().add(phone1);
			entityManager.merge(person);
			return new Object[] {person, phone1};
		});
		Person person = (Person) entities[0];
		Phone phone1 = (Phone) entities[1];

		// Check current state
		assertEquals(Sets.newHashSet("111 111 111"), getPhones());

		// Phone change should cause Person reindexing
		jpaService().doInTransactionAndFreshEM(entityManager -> {
			phone1.setNum("111 111 000");
			entityManager.merge(phone1);
			return null;
		});
		assertEquals(Sets.newHashSet("111 111 000"), getPhones());

		// Adding of phone should cause reindexing
		Phone phone2 = jpaService().doInTransactionAndFreshEM(entityManager -> {
			Phone p = new Phone("222 222 222", entityManager.getReference(Person.class, person.getId()));
			entityManager.persist(p);
			return p;
		});
		assertEquals(Sets.newHashSet("111 111 000", "222 222 222"), getPhones());

		// Phone removing should cause Person reindexing
		// BUG - does not work
		jpaService().doInTransactionAndFreshEM(entityManager -> {
			entityManager.remove(entityManager.getReference(Phone.class, phone1.getId()));
			return null;
		});
		assertEquals(Sets.newHashSet("222 222 222"), getPhones());
	}

	private Set<String> getPhones() {
		return jpaService().doInTransactionAndFreshSession(session -> {
			SearchSession searchSession = Search.session(session);
			JsonObject result = searchSession.search(Person.class)
					.extension(ElasticsearchExtension.get())
					.select(f -> f.source())
					.where(f -> f.match().field("name").matching("jana"))
					.fetchSingleHit()
					.get();
			HashSet<String> nums = new HashSet<>();
			JsonElement phones = result.get("phones");
			if (phones.isJsonArray()) {
				JsonArray phonesArr = phones.getAsJsonArray();
				for (int i = 0; i < phonesArr.size(); i++) {
					nums.add(phonesArr.get(i).getAsJsonObject().get("num").getAsString());
				}
			} else {
				nums.add(phones.getAsJsonObject().get("num").getAsString());
			}
			return nums;
		});

	}

}
