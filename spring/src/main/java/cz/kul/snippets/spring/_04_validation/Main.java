package cz.kul.snippets.spring._04_validation;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

/**
 * Spring can validate beans. 
 * 
 * Validator can check if properties are present, if value is from 
 * required range, ...
 * 
 * @author kulhalad
 * @since 7.4
 *
 */
public class Main {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = null;
        try {
            context = new ClassPathXmlApplicationContext("04_validation.xml");

            Person p = new Person();
            p.setAge(-10);
            
            Errors e = new BeanPropertyBindingResult(p, "person one");
            
            PersonValidator pv = new PersonValidator();
            pv.validate(p, e);

            for (ObjectError err : e.getAllErrors()) {
                System.out.println("b: " + err);
            }
            
        } finally {
            context.close();
        }

    }

}
