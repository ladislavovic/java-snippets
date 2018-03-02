package cz.kul.snippets.spring._01_xmlconfig_hw;

import static org.junit.Assert.assertEquals;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.Person;

/**
 * Hello world Spring application with xml configuration.
 * It creates a few beans and that's all.
 */
public class Main_SpringXMLConfigHw {
	
	public static void main(String[] args) {
		createBeans();
	}

	public static void createBeans() {
        String configLocation = "_01_spring.xml";
        try (ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(configLocation)) {

        	Person person = (Person) ctx.getBean("person");
            assertEquals(Person.class, person.getClass());
    
            Bean1 p1 = (Bean1) ctx.getBean("prototype");
            assertEquals("foo", p1.getVal());

            // This automatically found appropriate constructor and call it with given parameters
            Bean1 p2 = (Bean1) ctx.getBean("prototype", "bar");
            assertEquals("bar", p2.getVal());
        }

    }

}
