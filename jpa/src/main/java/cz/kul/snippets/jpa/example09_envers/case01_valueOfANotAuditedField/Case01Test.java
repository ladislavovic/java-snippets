package cz.kul.snippets.jpa.example09_envers.case01_valueOfANotAuditedField;

import cz.kul.snippets.jpa.common.JPATest;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class Case01Test extends JPATest {

    @Override
    public String getJpaModelPackages() {
        return Case01Test.class.getPackage().getName();
    }
    
    @Test
    public void notAuditedFieldHasANullValueInTheRevision() {
        Integer personId = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Person person = new Person();
            person.setName("Monica");
            person.setSurname("Geler");
            entityManager.persist(person);
            return person.getId();
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Person person = entityManager.find(Person.class, personId);
            person.setName("Rachel");
            person.setName("Green");
            return null;
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);
            List<Number> revisions = auditReader.getRevisions(Person.class, personId);
            Person rev1 = auditReader.find(Person.class, personId, revisions.get(0));
            Assert.assertEquals("Monica", rev1.getName());
            Assert.assertEquals(null, rev1.getSurname());
            return null;
        });
    }


}
