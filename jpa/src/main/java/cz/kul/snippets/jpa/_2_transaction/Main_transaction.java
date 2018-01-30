package cz.kul.snippets.jpa._2_transaction;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cz.kul.snippets.jpa.common.JPAConfig;
import cz.kul.snippets.jpa.common.model.Person;

/**
 * Declarative transactions are realized via AOP Proxies
 * 
 * Transactional annotationsy sould be used for concrete classes
 * only, not for interfaces. Annotations from interfaces are not
 * available in concrete class implementation. So the informatio
 * about Transactional annotation presence is not available for
 * all proxy modes.
 *
 */
public class Main_transaction {
	
	final static Logger log = Logger.getLogger(Main_transaction.class);
	
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(JPAConfig.class);
			ctx.register(Config.class);
			ctx.refresh();

			PersonService personService = ctx.getBean(PersonService.class);
			personService.createPerson();
			List<Person> persons = personService.findAll();
			assertEquals(1, persons.size());
		}
	}

}
