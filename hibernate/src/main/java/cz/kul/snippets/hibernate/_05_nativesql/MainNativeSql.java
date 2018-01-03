/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._05_nativesql;

import cz.kul.snippets.hibernate._05_nativesql.Person5;
import cz.kul.snippets.hibernate._05_nativesql.PersonDetail5;
import cz.kul.snippets.hibernate._05_nativesql.PersonService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author kulhalad
 */
public class MainNativeSql {
    
    public static void main(String[] args) {
        System.out.println("**************** START **************");
        
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-05-nativesql.xml");
        String [] names = ctx.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println("name: " + name);
        }
        PersonService personDao = (PersonService) ctx.getBean("PersonService");

        Person5 p1 = personDao.insertPerson("Jane");
        Person5 p2 = personDao.insertPerson("Monica");
        PersonDetail5 pd1 = personDao.insertPersonDetail(p1, "key1", "val1");
        PersonDetail5 pd2 = personDao.insertPersonDetail(p1, "key2", "val2");
        PersonDetail5 pd3 = personDao.insertPersonDetail(p2, "key3", "val3");

        personDao.testIt();
        
        System.out.println("**************** END *****************");
    }
    
}
