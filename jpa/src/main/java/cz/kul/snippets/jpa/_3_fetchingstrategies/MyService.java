package cz.kul.snippets.jpa._3_fetchingstrategies;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyService {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void testOneToMany() {

		// (1) Create data
		Customer customer = new Customer();
		Address a1 = new Address();
		Address a2 = new Address();
		a1.setCustomer(customer);
		a2.setCustomer(customer);
		customer.getAddresses().add(a1);
		customer.getAddresses().add(a2);
		em.persist(customer);
		em.flush();
		Integer customerId = customer.getId();

		// (2) Detach
		em.detach(customer);
		em.detach(a1);
		em.detach(a2);

		// (3) Count instances
		List l = em.createQuery("select c from Customer as c").getResultList();
		System.out.println(l.size());

		// (4)
		Customer merged = em.merge(customer);
		List l2 = em.createQuery("select c from Customer as c").getResultList();
		System.out.println(l2.size());
	}

}
