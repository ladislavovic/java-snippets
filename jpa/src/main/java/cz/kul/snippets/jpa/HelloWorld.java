package cz.kul.snippets.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cz.kul.snippets.jpa.model.Foo;

/**
 * Hello world!
 *
 */
public class HelloWorld {
	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext("jpa.xml");
		EntityManagerFactory emf = (EntityManagerFactory) context.getBean("entityManagerFactory");
		EntityManager em = emf.createEntityManager();
		doSomething(em);
		em.close();
	}

	private static void doSomething(EntityManager em) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		Foo foo = new Foo();
		foo.setEmail("email");
		em.persist(foo);

		tx.commit();
	}
}
