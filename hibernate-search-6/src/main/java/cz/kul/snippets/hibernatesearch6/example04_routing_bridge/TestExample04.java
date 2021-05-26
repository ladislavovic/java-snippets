package cz.kul.snippets.hibernatesearch6.example04_routing_bridge;

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
public class TestExample04 extends HibernateSearch6Test {

	@Test
	public void test() {
		jpaService().doInTransactionAndFreshEM(entityManager -> {
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

			// todo add assert. Now I must manually check index in Kibana

			return null;
		});
	}

}
