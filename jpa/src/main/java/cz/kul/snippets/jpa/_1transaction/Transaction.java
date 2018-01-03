package cz.kul.snippets.jpa._1transaction;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Declarative transactions are realized via AOP Proxies
 * 
 * Transactional annotationsy sould be used for concrete classes
 * only, not for interfaces. Annotations from interfaces are not
 * available in concrete class implementation. So the informatio
 * about Transactional annotation presence is not available for
 * all proxy modes.
 * 
 * 
 * @author Ladislav Kulhanek
 *
 */
public class Transaction {

	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext("springcontext-transaction.xml");
		FooService fooService = (FooService) context.getBean("fooService");
		fooService.createFoo();
	}

}
