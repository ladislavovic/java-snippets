package cz.kul.snippets.jpa.example09_envers;

import cz.kul.snippets.jpa.common.JPATest;
import cz.kul.snippets.jpa.example09_envers.model.Address;
import cz.kul.snippets.jpa.example09_envers.model.Person;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.Query;
import java.util.List;

public class TestsEnvers extends JPATest {

    @Override
    public String getJpaModelPackages() {
        return Person.class.getPackage().getName();
    }
    
    @Test
    public void test() {
        Integer personId = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Address address = new Address();
            address.setAddress("Ostrava");
            entityManager.persist(address);

            Person person = new Person();
            person.setName("Monica");
            entityManager.persist(person);

            return person.getId();
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Person person = entityManager.find(Person.class, personId);
            person.setName("Rachel");
            return null;
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);
            
            List<Number> revisions = auditReader.getRevisions(Person.class, personId);
            Assert.assertEquals(2, revisions.size());

            Person rev1 = auditReader.find(Person.class, personId, revisions.get(0));
            Assert.assertEquals("Monica", rev1.getName());
            
            Person rev2 = auditReader.find(Person.class, personId, revisions.get(1));
            Assert.assertEquals("Rachel", rev2.getName());

            return null; 
        });
    }
}
