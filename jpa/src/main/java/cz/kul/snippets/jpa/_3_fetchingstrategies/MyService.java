package cz.kul.snippets.jpa._3_fetchingstrategies;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class MyService {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public Long createCustomer(String customerName, String... addrs) {
		Customer customer = new Customer();
		em.persist(customer);
		
		for (String addr : addrs) {
			Address a = new Address();
			a.setAddress(addr);
			a.setCustomer(customer);
			em.persist(a);
		}
		
		return customer.getId();
	}
	
	@Transactional
	public Customer findCustomerById(Long id) {
		Session sess = this.em.unwrap(Session.class);
		sess.enableFetchProfile("profile1");
		
		Customer customer = em.find(Customer.class, id);
		return customer;
	}
	
	public Customer findCustomerByName(String name) {
		Query q = em.createQuery("select c from Customer c where name = :name");
		q.setParameter("name", name);
		
		@SuppressWarnings("unchecked")
		List<Customer> resultList = q.getResultList();
		if (CollectionUtils.isEmpty(resultList)) {
			return null;
		}
		return resultList.get(0);
	}

}
