package cz.kul.snippets.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class HelloWorld {
	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext("jpa.xml");
		EntityManagerFactory emf = (EntityManagerFactory) context.getBean("entityManagerFactory");
		EntityManager em = emf.createEntityManager();
//		doSomething(em);
		em.close();
	}

}
