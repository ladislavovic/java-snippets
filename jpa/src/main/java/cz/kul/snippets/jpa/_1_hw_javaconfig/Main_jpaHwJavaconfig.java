package cz.kul.snippets.jpa._1_hw_javaconfig;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cz.kul.snippets.jpa.common.JPAConfig;
import cz.kul.snippets.jpa.common.model.Person;

/**
 * <h1>JPA</h1>
 * 
 * <h2>Persostence Context</h2>
 * <p>
 * A persistence context is like a cache which contains a set of persistent
 * entities, So once the transaction is finished, all persistent objects are
 * detached from the EntityManager's persistence context and are no longer
 * managed
 * </p>
 *
 * <h2>Entity Manager</h2>
 * <p>
 * It is an interface which allows you to interact with persitence contex, that
 * is persist entities, find entities, remove entities, ...
 * </p>
 * 
 * <h2>Persistence Unit</h2>
 * <p>Set of entity classes and some additional config information</p>
 *
 */
public class Main_jpaHwJavaconfig {

	final static Logger log = Logger.getLogger(Main_jpaHwJavaconfig.class);

	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(JPAConfig.class);
			ctx.register(Config.class);

			log.info("### Context refreshing...");
			ctx.refresh();
			log.info("### Context refreshing END");

			EntityManagerFactory emf = (EntityManagerFactory) ctx.getBean("entityManagerFactory");

			log.info("### Creating of EntityManager...");
			EntityManager em = emf.createEntityManager();
			log.info("### Creating of EntityManager END");

			insertData(em);
			loadData(em);

			em.close();
		}
	}

	private static void insertData(EntityManager em) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		Person person = new Person();
		person.setFirstname("Pepa");
		em.persist(person);

		tx.commit();
	}

	private static void loadData(EntityManager em) {
		Query query = em.createQuery("from Person");
		@SuppressWarnings("rawtypes")
		List resultList = query.getResultList();
		assertEquals(1, resultList.size());
	}

}
