package cz.kul.snippets.hibernatesearch6.example05_embedded_with_routing_bridge;

import cz.kul.snippets.hibernatesearch6.commons.HibernateSearch6Test;
import org.junit.Test;

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

			Attribute attribute3 = new Attribute("_attr3", "val3");
			attribute3.setAttributeSet(attributeSet);
			attributeSet.getAttributes().add(attribute3);
			entityManager.persist(attribute3);

			return null;
		});
	}

}
