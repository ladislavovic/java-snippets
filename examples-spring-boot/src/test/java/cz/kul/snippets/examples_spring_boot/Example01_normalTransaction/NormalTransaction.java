package cz.kul.snippets.examples_spring_boot.Example01_normalTransaction;

import cz.kul.snippets.examples_spring_boot.TestcontainersConfiguration;
import cz.kul.snippets.examples_spring_boot.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class NormalTransaction
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

@Service("personService_ex1")
class PersonService
{

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Long createPersonWithName(String name)
    {
        try {
            if (true) {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
            ;
        }

        Person person = new Person();
        person.setName(name);
        person.setBirthdate(new Date(Instant.now().minus(1000, ChronoUnit.DAYS).toEpochMilli()));
        entityManager.persist(person);
        return person.getId();
    }

}
