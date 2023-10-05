package cz.kul.snippets.jpa.example02_hibernateSessionOperations;

import cz.kul.snippets.jpa.common.JPATest;
import cz.kul.snippets.jpa.common.model.Person;
import cz.kul.snippets.jpa.common.model.PersonDetail;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.stat.SessionStatistics;
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

    @Test
    public void testIsProxyInSession() {
        PersonDetail detail = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Person monica = new Person("Monica");
            monica = entityManager.merge(monica);

            PersonDetail detail1 = new PersonDetail();
            detail1.setValue("d1");
            detail1.setPerson(monica);
            detail1 = entityManager.merge(detail1);
            return detail1;
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            PersonDetail personDetail = entityManager.find(PersonDetail.class, detail.getId());

            System.out.println("is person initialized: " + Hibernate.isInitialized(personDetail.getPerson()));
            System.out.println("session contains person: " + entityManager.contains(personDetail.getPerson()));

            Person person = entityManager.find(Person.class, personDetail.getPerson().getId());
            System.out.println("Person loaded directly");
            System.out.println("is person initialized: " + Hibernate.isInitialized(personDetail.getPerson()));
            System.out.println("session contains person: " + entityManager.contains(personDetail.getPerson()));
            return null;
        });
    }


    private int getNumberOfEntitiesInSession(EntityManager entityManager) {
        return entityManager.unwrap(Session.class).getStatistics().getEntityCount();
    }

}
