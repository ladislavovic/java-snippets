package cz.kul.snippets.jpa._3criteria;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext("springcontext-criteria.xml");
		CriteriaService service = (CriteriaService) context.getBean("criteriaService");
		service.test();

	}

}
