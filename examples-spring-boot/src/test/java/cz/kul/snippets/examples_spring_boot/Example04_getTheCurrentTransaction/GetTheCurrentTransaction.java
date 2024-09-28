package cz.kul.snippets.examples_spring_boot.Example04_getTheCurrentTransaction;

import cz.kul.snippets.examples_spring_boot.TestcontainersConfiguration;
import cz.kul.snippets.examples_spring_boot.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class GetTheCurrentTransaction
{

    @Autowired
    private PersonService personService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void test()
    {
        Long monicaId = personService.createPersonWithName("Monica");

        assertThat(entityManager.find(Person.class, monicaId))
            .isNotNull();
    }

}

@Service("personService_ex4")
class PersonService
{

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Long createPersonWithName(String name)
    {
        // Print information about the current transaction
        TransactionUtils.logCurrentTransactionInfo();

        Person person = new Person();
        person.setName(name);
        person.setBirthdate(new Date(Instant.now().minus(1000, ChronoUnit.DAYS).toEpochMilli()));
        entityManager.persist(person);
        return person.getId();
    }

}

class TransactionUtils
{

    public static void logCurrentTransactionInfo()
    {
        TransactionStatus transactionStatus = TransactionAspectSupport.currentTransactionStatus();
        System.out.println("The current transaction: {hasTransaction=%s, transactionName=%s, readOnly=%s, rollbackOnly=%s}".formatted(
            transactionStatus.hasTransaction(),
            transactionStatus.getTransactionName(),
            transactionStatus.isReadOnly(),
            transactionStatus.isRollbackOnly()
        ));
    }

}
