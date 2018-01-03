package cz.kul.snippets.jpa._2onetomany;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/*
 * What is owning side??
 * 
 * 
 * 
 */

public class OneToMany {

	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"2onetomany.xml");
		OneToManyService service = (OneToManyService) context
				.getBean("oneToManyService");
		service.testCascadeMerge();
	}

}
