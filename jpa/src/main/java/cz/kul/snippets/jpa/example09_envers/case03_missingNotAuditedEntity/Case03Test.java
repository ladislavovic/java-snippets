package cz.kul.snippets.jpa.example09_envers.case03_missingNotAuditedEntity;

import cz.kul.snippets.jpa.common.JPATest;
import org.hibernate.Hibernate;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("Duplicates")
public class Case03Test extends JPATest {

    @Override
    public String getJpaModelPackages() {
        return Case03Test.class.getPackage().getName();
    }

    @Test
    public void test() {
        Integer [] ids = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Address address = new Address();
            address.setAddress("Ostrava");
            entityManager.persist(address);

            Person person = new Person();
            person.setName("Monica");
            person.setAddress(address);
            entityManager.persist(person);

            return new Integer[] {person.getId(), address.getId()};
        });
        Integer personId = ids[0];
        Integer addressId = ids[1];
        
        // remove address
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            entityManager.remove(entityManager.getReference(Person.class, personId));
            entityManager.remove(entityManager.getReference(Address.class, addressId));
            return null;
        });
        
        // initialize by hibernate.initialize()
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);
            List<Number> revisions = auditReader.getRevisions(Person.class, personId);
            Person rev1 = auditReader.find(Person.class, personId, revisions.get(0));
            Hibernate.initialize(rev1.getAddress());

            Assert.assertEquals("Monica", rev1.getName());
            Assert.assertNotNull(rev1.getAddress());
            Assert.assertEquals(null, rev1.getAddress().getAddress());

            return null;
        });
        
        
    }

}
