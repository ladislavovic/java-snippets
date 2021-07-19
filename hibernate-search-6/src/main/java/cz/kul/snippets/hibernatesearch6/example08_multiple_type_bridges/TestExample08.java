package cz.kul.snippets.hibernatesearch6.example08_multiple_type_bridges;

import cz.kul.snippets.hibernatesearch6.commons.HibernateSearch6Test;
import org.junit.Test;

public class TestExample08 extends HibernateSearch6Test {

	@Test
	public void test() {

		// Prepare data
		Object[] entities = jpaService().doInTransactionAndFreshEM(entityManager -> {
			Node node1 = new Node("node1");
			entityManager.persist(node1);

			Link link1 = new Link("link1");
			entityManager.persist(link1);

			return null;
		});

	}

}
