package cz.kul.snippets.jpa._3criteria;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.kul.snippets.jpa.model.Device;
import cz.kul.snippets.jpa.model.Group;

@Service
public class CriteriaService {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void test() {

		// prepare data
		Group g = new Group();
		g.setName("Group 1");
		em.persist(g);

		Device d1 = new Device();
		d1.setName("device 1");
		d1.setGroup(g);
		em.persist(d1);

		Device d2 = new Device();
		d2.setName("device 2");
		d2.setGroup(g);
		em.persist(d2);

		em.flush();

		// query
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Device> criteriaQuery = builder.createQuery(Device.class);
		Root<Device> root = criteriaQuery.from(Device.class);
		criteriaQuery.select(root);

		ParameterExpression<Integer> p = builder.parameter(Integer.class);
		Predicate predicate = builder.gt(root.<Integer> get("id"), p);
		criteriaQuery.where(predicate);

		TypedQuery<Device> q = em.createQuery(criteriaQuery);
		q.setParameter(p, 1);
		List<Device> result = q.getResultList();
		System.out.println(result);

	}

}
