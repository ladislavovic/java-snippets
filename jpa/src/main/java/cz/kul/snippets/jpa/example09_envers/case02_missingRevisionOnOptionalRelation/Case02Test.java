package cz.kul.snippets.jpa.example09_envers.case02_missingRevisionOnOptionalRelation;

import cz.kul.snippets.jpa.common.JPATest;
import org.hibernate.Hibernate;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("Duplicates")
public class Case02Test extends JPATest {

    @Override
    public String getJpaModelPackages() {
        return Case02Test.class.getPackage().getName();
    }

    @Test
    public void test() {
        Integer [] ids = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Address address = new Address();
            address.setAddress("Ostrava");
            entityManager.persist(address);

            Phone phone = new Phone();
            phone.setNumber("123");
            entityManager.persist(phone);

            Person person = new Person();
            person.setName("Monica");
            person.setAddress(address);
            person.setPhone(phone);
            entityManager.persist(person);

            return new Integer[] {person.getId(), address.getId(), phone.getPhoneId()};
        });
        Integer personId = ids[0];
        Integer addressId = ids[1];
        Integer phoneId = ids[1];
        
        // initialize by hibernate.initialize()
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);
            List<Number> revisions = auditReader.getRevisions(Person.class, personId);
            Person rev1 = auditReader.find(Person.class, personId, revisions.get(0));
            Hibernate.initialize(rev1.getAddress());

            Assert.assertEquals("Monica", rev1.getName());
            Assert.assertNotNull(rev1.getAddress());
            Assert.assertEquals("Ostrava", rev1.getAddress().getAddress());

            return null;
        });
        
        // initialize by method calling
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);
            List<Number> revisions = auditReader.getRevisions(Person.class, personId);
            Person rev1 = auditReader.find(Person.class, personId, revisions.get(0));

            Assert.assertEquals("Monica", rev1.getName());
            Assert.assertEquals("Ostrava", rev1.getAddress().getAddress());

            return null;
        });
        
        // remove revisions        
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Query q = entityManager.createNativeQuery("delete from Address_AUD aa");
            int updatedCount = q.executeUpdate();
            Assert.assertEquals(1, updatedCount);
            
            Query q2 = entityManager.createNativeQuery("delete from Phone_AUD aa");
            int updatedCount2 = q2.executeUpdate();
            Assert.assertEquals(1, updatedCount2);
            
            return null;
        });
        
        
        // call initialize
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);
            List<Number> revisions = auditReader.getRevisions(Person.class, personId);
            Person rev1 = auditReader.find(Person.class, personId, revisions.get(0));
            Assert.assertEquals(addressId, rev1.getAddress().getId());
            Hibernate.initialize(rev1.getAddress());
            Hibernate.initialize(rev1.getPhone());
            
            Assert.assertEquals("Monica", rev1.getName());
            Assert.assertNotNull(rev1.getAddress());
            Assert.assertEquals(addressId, rev1.getAddress().getId());
            Assert.assertNull(rev1.getAddress().getAddress());

            return null;
        });
        
        // by method call
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);
            List<Number> revisions = auditReader.getRevisions(Person.class, personId);
            Person rev1 = auditReader.find(Person.class, personId, revisions.get(0));
            
            Assert.assertEquals("Monica", rev1.getName());
            Assert.assertNotNull(rev1.getAddress());
            Assert.assertEquals(addressId, rev1.getAddress().getId());
            Assert.assertNull(rev1.getAddress().getAddress());

            return null;
        });
    }

    @Test
    public void normalHibernate() {
        Integer[] ids = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Address address = new Address();
            address.setAddress("Ostrava");
            entityManager.persist(address);

            Phone phone = new Phone();
            phone.setNumber("123");
            entityManager.persist(phone);

            Person person = new Person();
            person.setName("Monica");
            person.setAddress(address);
            person.setPhone(phone);
            entityManager.persist(person);

            return new Integer[]{person.getId(), address.getId(), phone.getPhoneId()};
        });
        Integer personId = ids[0];
        Integer addressId = ids[1];
        Integer phoneId = ids[1];

        // initialize by hibernate.initialize()
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Person person = entityManager.find(Person.class, personId);
            Assert.assertEquals("Monica", person.getName());
            
            Hibernate.initialize(person.getAddress());
            Assert.assertEquals("Ostrava", person.getAddress().getAddress());
            
            Assert.assertEquals("123", person.getPhone().getNumber());
            return null;
        });
    }
    
}
