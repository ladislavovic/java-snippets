package cz.kul.snippets.jpa._3_fetchingstrategies;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cz.kul.snippets.jpa.common.JPAConfig;

/*
 * What is owning side??
 * 
 * 
 * 
 */

public class Main_Fetchingstrategies {

	final static Logger log = Logger.getLogger(Main_Fetchingstrategies.class);

	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(JPAConfig.class);
			ctx.register(Config.class);
			ctx.refresh();

			MyService myService = ctx.getBean(MyService.class);
			Long customerId = myService.createCustomer("cust1", "addr1", "addr2");
			
			log.info("SELECT:");
//			Customer customer = myService.findCustomerByName("cust1");
			myService.findCustomerById(customerId);
			
		}
	}

}
