/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._03_sessionOperations;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author kulhalad
 */
public class MainSessionOperations {
    
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-sessionoperation.xml");
        MyService myService = ctx.getBean(MyService.class);
        
        myService.contains();
        
        myService.evict();
        
        myService.get();
        
        myService.load();
        
        myService.merge();
        
        myService.refresh();
        
        myService.saveOrUpdate();
        
    }
    
}
