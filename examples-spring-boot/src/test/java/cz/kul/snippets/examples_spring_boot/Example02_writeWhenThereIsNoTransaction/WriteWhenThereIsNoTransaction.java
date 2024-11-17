package cz.kul.snippets.examples_spring_boot.Example02_writeWhenThereIsNoTransaction;

import cz.kul.snippets.examples_spring_boot.TestcontainersConfiguration;
import cz.kul.snippets.examples_spring_boot.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class WriteWhenThereIsNoTransaction
{

    @Autowired
    private PersonService personService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Dao dao;

    @Test
    void createPerson()
    {
//        assertThatExceptionOfType(jakarta.persistence.TransactionRequiredException.class)
//            .isThrownBy(() -> personService.createPersonWithName("Monica"));

        try {
//            personService.createPersonWithName("Monica");
            dao.save(new Person("Monica"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

@Repository
interface Dao extends JpaRepository<Person, Long>
{


}

@Service("personService_ex2")
class PersonService
{

    @PersistenceContext
    private EntityManager entityManager;

    public Long createPersonWithName(String name)
    {
        Person person = new Person();
        person.setName(name);
        person.setBirthdate(new Date(Instant.now().minus(1000, ChronoUnit.DAYS).toEpochMilli()));
        entityManager.persist(person);
        return person.getId();
    }

}
