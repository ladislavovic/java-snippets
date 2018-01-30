package cz.kul.snippets.jpa._2_transaction;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.kul.snippets.jpa.common.model.Person;

@Service
public class PersonService {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void createPerson() {
		Person person = new Person();
		person.setFirstname("Pepa");
		person.setSecondname("Novak");
		entityManager.persist(person);
		entityManager.flush();
	}

	public List<Person> findAll() {
		Query q = entityManager.createQuery("from Person");
		@SuppressWarnings("unchecked")
		List<Person> resultList = q.getResultList();
		return resultList;
	}

}
