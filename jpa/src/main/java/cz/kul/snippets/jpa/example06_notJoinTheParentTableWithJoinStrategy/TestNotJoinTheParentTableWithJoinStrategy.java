package cz.kul.snippets.jpa.example06_notJoinTheParentTableWithJoinStrategy;

import cz.kul.snippets.jpa.common.JPATest;
import cz.kul.snippets.jpa.example06_notJoinTheParentTableWithJoinStrategy.model.SubA;
import org.junit.Test;

import javax.persistence.Query;

public class TestNotJoinTheParentTableWithJoinStrategy extends JPATest {

    @Test
    public void test() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {

            SubA subA = new SubA();
            subA.setVal("a");
            entityManager.persist(subA);
            entityManager.flush();

            subA = new SubA();
            subA.setVal("b");
            entityManager.persist(subA);
            entityManager.flush();

            {
                Query query = entityManager.createQuery("select val from SubA");
                query.getResultList();
            }
//            {
//                Query query = entityManager.createQuery("from A");
//                query.getResultList();
//            }
            return null;
        });
    }
}
