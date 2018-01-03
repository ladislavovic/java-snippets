/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._02_entitystate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author kulhalad
 */
public class MainEntityState {
    
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-entitystate.xml");
        MyService myService = ctx.getBean(MyService.class);
        
        myService.stateTransient();
        
        myService.statePersistent();
        
        myService.stateDetached();
        
        myService.stateRemoved();
    }
    
}
