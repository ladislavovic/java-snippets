package cz.kul.snippets.hibernatesearch6.example10_reindexing_when_dependency_updated;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cz.kul.snippets.hibernatesearch6.commons.HibernateSearch6Test;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("Duplicates")
public class Example10Test extends HibernateSearch6Test {

	@Test
	public void test() {

		// Prepare data
		Long person1Id = jpaService().doInTransactionAndFreshEM(entityManager -> {
			Person person = new Person("Monica");
			entityManager.persist(person);
			return person.getId();
		});
		Long person2Id = jpaService().doInTransactionAndFreshEM(entityManager -> {
			Person person = new Person("Rachel");
			entityManager.persist(person);
			return person.getId();
		});
		String p1DocVersion1 = singlePersonCars(person1Id);
		String p2DocVersion1 = singlePersonCars(person2Id);

		Long carId = jpaService().doInTransactionAndFreshEM(entityManager -> {
			Car mondeo = new Car(person1Id, "Mondeo", "Ford");
			entityManager.persist(mondeo);
			return mondeo.getId();
		});
		String p1DocVersion2 = singlePersonCars(person1Id);
		String p2DocVersion2 = singlePersonCars(person2Id);
		Assert.assertNotEquals(p1DocVersion1, p1DocVersion2);
		Assert.assertEquals(p2DocVersion1, p2DocVersion2);

		jpaService().doInTransactionAndFreshEM(entityManager -> {
			Car car = entityManager.find(Car.class, carId);
			car.setCarName("Kuga");
			return null;
		});
		String p1DocVersion3 = singlePersonCars(person1Id);
		String p2DocVersion3 = singlePersonCars(person2Id);
		Assert.assertNotEquals(p1DocVersion2, p1DocVersion3);
		Assert.assertEquals(p2DocVersion2, p2DocVersion3);

		jpaService().doInTransactionAndFreshEM(entityManager -> {
			Car car = entityManager.find(Car.class, carId);
			car.setMaker("Opel");
			return null;
		});
		String p1DocVersion4 = singlePersonCars(person1Id);
		String p2DocVersion4 = singlePersonCars(person2Id);
		Assert.assertEquals(p1DocVersion3, p1DocVersion4);
		Assert.assertEquals(p2DocVersion3, p2DocVersion4);
	}

	public String singlePersonCars(Long personId) {
		return personCars(personId).iterator().next();
	}

	public Set<String> personCars(Long personId) {
		Set<String> cars = jpaService().doInTransactionAndFreshSession(session -> {
			SearchSession searchSession = Search.session(session);
			Optional<JsonObject> jsonObject = searchSession.search(Person.class)
					.extension(ElasticsearchExtension.get())
					.select(f -> f.source())
					.where(f -> f.id().matching(personId))
					.fetchSingleHit();
			JsonElement element = jsonObject.get().get("cars");
			HashSet<String> result = new HashSet<>();
			if (element != null) {
				if (element.isJsonArray()) {
					for (JsonElement jsonElement : element.getAsJsonArray()) {
						result.add(jsonElement.getAsString());
					}
				} else {
					result.add(element.getAsString());
				}
			}
			return result;
		});
		return cars;
	}

}
