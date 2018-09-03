package cz.kul.snippets.jpa.example01_modifyEntityDuringLoadByEventListener;

import cz.kul.snippets.jpa.common.JPATest;
import cz.kul.snippets.jpa.common.model.Person;
import org.junit.Assert;
import org.junit.Test;

public class TestModifyEntityDuringLoadByEventListener extends JPATest{

    @Test
    public void testNameIsGeneratedByListener() {
        Long personId = jpaService().doInTransactionAndFreshEM(em -> {
            Person person = new Person();
            em.persist(person);
            return person.getId();
        });
        jpaService().doInTransactionAndFreshEM(em -> {
            Person person = em.find(Person.class, personId);
            Assert.assertEquals(PersonNameModifier.GENERATED_PERSON_NAME, person.getName());
            return null;
        });
    }

}
