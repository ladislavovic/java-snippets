package cz.kul.snippets.hibernatesearch6.example10_reindexing_when_dependency_updated;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class PersonService {

	@PersistenceContext
	private EntityManager entityManager;

	public List<Person> getCarOwner(Car car) {
		Query query = entityManager.createQuery("select p from Person p where p.id = :personId");
		query.setParameter("personId", car.getOwnerPersonId());
		List<Person> resultList = query.getResultList();
		return resultList;
	}

	public List<Car> getOwnedCars(Person person) {
		Query query = entityManager.createQuery("select c from Car c where c.ownerPersonId = :personId");
		query.setParameter("personId", person.getId());
		List<Car> resultList = query.getResultList();
		return resultList;
	}

}
