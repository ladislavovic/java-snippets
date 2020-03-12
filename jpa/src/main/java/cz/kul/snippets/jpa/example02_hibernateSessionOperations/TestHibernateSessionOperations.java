package cz.kul.snippets.jpa.example02_hibernateSessionOperations;

import cz.kul.snippets.jpa.common.JPATest;
import cz.kul.snippets.jpa.common.model.Person;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.io.Serializable;

public class TestHibernateSessionOperations extends JPATest {

    @Test
    public void testContains() {
        Serializable personId = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Person person = new Person();
            Assert.assertFalse(entityManager.contains(person));
            entityManager.persist(person);
            Assert.assertTrue(entityManager.contains(person));
            return person.getId();
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Person loadedPerson = entityManager.find(Person.class, personId);
            Assert.assertTrue(entityManager.contains(loadedPerson));
            return null;
        });
    }

    /**
     * Evict is in Hibernate Session only. There is detach() in EntityManager.
     */
    @Test
    public void testEvict() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Assert.assertEquals(0, getNumberOfEntitiesInSession(entityManager));
            Person person = new Person();
            entityManager.persist(person);
            Assert.assertEquals(1, getNumberOfEntitiesInSession(entityManager));
            entityManager.unwrap(Session.class).evict(person);
            Assert.assertEquals(0, getNumberOfEntitiesInSession(entityManager));
            return null;
        });
    }

    @Test
    public void testDetach() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Assert.assertEquals(0, getNumberOfEntitiesInSession(entityManager));
            Person person = new Person();
            entityManager.persist(person);
            Assert.assertEquals(1, getNumberOfEntitiesInSession(entityManager));
            entityManager.detach(person);
            Assert.assertEquals(0, getNumberOfEntitiesInSession(entityManager));
            return null;
        });
    }

    @Test
    public void testGetReference_shouldFailIfYouTryToReadNotExistingInstance() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Person notExistingPerson = entityManager.getReference(Person.class, 12345L);
            assertThrows(EntityNotFoundException.class, () -> notExistingPerson.getName());
            return null;
        });
    }

    @Test
    public void testGetReference_shouldInitializeEntityDuringFirstRead() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Person person = new Person();
            entityManager.persist(person);
            entityManager.flush();
            entityManager.clear();

            Person reference = entityManager.getReference(Person.class, person.getId());
            assertUninitializedHibernateProxy(reference);

            reference.getName();
            assertInitialized(reference);

            return null;
        });
    }

    @Test
    public void testMerge_shouldUpdateObjectIfItExists() {
        Person monicaDetached = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Person monica = new Person("monica");
            entityManager.persist(monica);
            return monica;
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            monicaDetached.setName("rachel");
            Person monica = entityManager.merge(monicaDetached);
            Assert.assertEquals("rachel", monica.getName());
            Assert.assertTrue(entityManager.contains(monica));
            return null;
        });
    }

    @Test
    public void testMerge_shouldPersistObjectIfItDoesNotExistsYet() {
        Person monicaNew = new Person("monica");
        Person monicaDetached = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Person monica = entityManager.merge(monicaNew);
            return monica;
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Person monica = entityManager.find(Person.class, monicaDetached.getId());
            Assert.assertNotNull(monica);
            return null;
        });
    }

    private int getNumberOfEntitiesInSession(EntityManager entityManager) {
        return entityManager.unwrap(Session.class).getStatistics().getEntityCount();
    }

}
