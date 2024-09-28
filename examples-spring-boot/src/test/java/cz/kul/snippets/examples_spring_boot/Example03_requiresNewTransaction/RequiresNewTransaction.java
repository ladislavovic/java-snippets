package cz.kul.snippets.examples_spring_boot.Example03_requiresNewTransaction;

import cz.kul.snippets.examples_spring_boot.TestcontainersConfiguration;
import cz.kul.snippets.examples_spring_boot.model.Log;
import cz.kul.snippets.examples_spring_boot.model.Person;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class RequiresNewTransaction
{

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PersonService personService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    void logIsCreatedEvenThoughThePersonCreatingFailed()
    {
        try {
            personService.createPersonWithName("Monica", true);
            Assertions.fail("Should failed");
        } catch (RuntimeException e) {
            // do nothing
        }

        Long noOfPeople = entityManager
            .createQuery("select count(*) from Person p", Long.class)
            .getSingleResult();
        assertThat(noOfPeople).isEqualTo(0);

        Long noOfLogs = entityManager
            .createQuery("select count(*) from Log l", Long.class)
            .getSingleResult();
        assertThat(noOfLogs).isEqualTo(1);
    }

    @Test
    void newTransactionDoesNotSeeTheDataOfOriginalTransaction()
    {
        // Prepare TransactionTemplate to run transaction programmatically
        TransactionTemplate tx1 = new TransactionTemplate(transactionManager);
        tx1.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        // This lambda will be executed in transaction
        tx1.executeWithoutResult(transactionStatus -> {
            Person person = new Person();
            person.setName("Monica");
            entityManager.persist(person);
            entityManager.flush();
            assertThat(personService.getNoOfPeople()).isEqualTo(1); // The Person entity is created. I can work with it in tx1. But it is not commited.

            // Preparing another TransactionTemplate to run new transaction
            TransactionTemplate tx2 = new TransactionTemplate(transactionManager);
            tx2.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // set it to execute another transaction. Otherwise, it would use the existing one.

            // Run another transaction.
            tx2.executeWithoutResult(transactionStatus2 -> {
                assertThat(personService.getNoOfPeople()).isEqualTo(0); // it does not see Person entity created in the first transaction, because the first transaction has not commited yet.
            });
        });
    }

}

@Service("personService_ex3")
class PersonService
{

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private LogService logService;

    @Transactional
    public Long createPersonWithName(String name, boolean shouldFail)
    {
        logService.log("Creating person with name: %s".formatted(name));

        Person person = new Person();
        person.setName(name);
        person.setBirthdate(new Date(Instant.now().minus(1000, ChronoUnit.DAYS).toEpochMilli()));
        entityManager.persist(person);

        if (shouldFail) {
            throw new RuntimeException("The nasty error occurred.");
        }

        return person.getId();
    }

    public Long getNoOfPeople() {
        return entityManager
            .createQuery("select count(*) from Person p", Long.class)
            .getSingleResult();
    }

}

@Service("logService_ex3")
class LogService
{
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String message)
    {
        entityManager.persist(new Log(message));
    }

}
