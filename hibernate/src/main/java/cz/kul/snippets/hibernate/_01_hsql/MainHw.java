package cz.kul.snippets.hibernate._01_hsql;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainHw {

    public static void main(String[] args) {
        System.out.println("**************** START **************");
        
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-hw.xml");
        PersonService personDao = (PersonService) ctx.getBean("personServiceImpl");

        Person p1 = personDao.insertPerson("Jane");
        Person p2 = personDao.insertPerson("Monica");
        PersonDetail pd1 = personDao.insertPersonDetail(p1, "key1", "val1");
        PersonDetail pd2 = personDao.insertPersonDetail(p1, "key2", "val2");
        PersonDetail pd3 = personDao.insertPersonDetail(p2, "key3", "val3");
//        int number = personDao.getAll().size();
//        System.out.println("Number of persons: " + number);

        personDao.criteriaTest();
        
        System.out.println("**************** END *****************");
    }

}
