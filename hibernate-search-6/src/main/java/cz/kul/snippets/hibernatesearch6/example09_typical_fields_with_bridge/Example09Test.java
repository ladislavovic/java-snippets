package cz.kul.snippets.hibernatesearch6.example09_typical_fields_with_bridge;

import com.google.gson.JsonObject;
import cz.kul.snippets.hibernatesearch6.commons.HibernateSearch6Test;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SuppressWarnings("Duplicates")
public class Example09Test extends HibernateSearch6Test {

	@Test
	public void test() {

		// Prepare data
		Long personId = jpaService().doInTransactionAndFreshEM(entityManager -> {
			Person person = new Person("Jana Novakova");
			entityManager.persist(person);
			return person.getId();
		});

	}

}
