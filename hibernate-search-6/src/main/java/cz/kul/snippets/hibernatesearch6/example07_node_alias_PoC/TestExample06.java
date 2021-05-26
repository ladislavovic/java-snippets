package cz.kul.snippets.hibernatesearch6.example07_node_alias_PoC;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cz.kul.snippets.hibernatesearch6.commons.HibernateSearch6Test;
import cz.kul.snippets.hibernatesearch6.example06_ca_proof_of_concept.CAValue;
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
			Node node = new Node("node1");
			entityManager.persist(node);

			NodeAlias nodeAlias = new NodeAlias("node1_al1", node);
			node.getAliases().add(nodeAlias);
			entityManager.persist(nodeAlias);

			return new Object[]{node, nodeAlias};
		});
		Node node = (Node) entities[0];
		NodeAlias nodeAlias = (NodeAlias) entities[1];

		// Check current state
		assertEquals(Sets.newHashSet("node1_al1"), getAliases());

		// Attribute change must trigger reindex
		jpaService().doInTransactionAndFreshEM(entityManager -> {
			NodeAlias nodeAlias1 = entityManager.find(NodeAlias.class, nodeAlias.getId());
			nodeAlias1.setAlias("node1_al2");
			return null;
		});
		assertEquals(Sets.newHashSet("node1_al2"), getAliases());

		// Attribute adding must trigger reindex
		jpaService().doInTransactionAndFreshEM(entityManager -> {
			NodeAlias alias2 = new NodeAlias("node1_al3", entityManager.getReference(Node.class, node.getId()));
			entityManager.persist(alias2);
			return null;
		});
		assertEquals(Sets.newHashSet("node1_al2", "node1_al3"), getAliases());

		// Attribute removing must trigger reindex - BUG?? Does not work.
//		jpaService().doInTransactionAndFreshEM(entityManager -> {
//			entityManager.remove(entityManager.getReference(CAValue.class, caValue1.getId()));
//			return null;
//		});
//		assertEquals(Sets.newHashSet("val2", "val3"), getAttributeValues());
	}

	private Set<String> getAliases() {
		return jpaService().doInTransactionAndFreshSession(session -> {
			SearchSession searchSession = Search.session(session);
			JsonObject result = searchSession.search(Node.class)
					.extension(ElasticsearchExtension.get())
					.select(f -> f.source())
					.where(f -> f.match().field("name").matching("node1"))
					.fetchSingleHit()
					.get();
			HashSet<String> values = new HashSet<>();
			JsonElement attributes = result.get("aliasesStr");
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
