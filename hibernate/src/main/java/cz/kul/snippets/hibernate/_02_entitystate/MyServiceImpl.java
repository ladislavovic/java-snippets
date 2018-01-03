/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._02_entitystate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyServiceImpl implements MyService {
    
    @Autowired
    private SessionFactory sessionFactory;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
    
    @Override
    @Transactional
    public void stateTransient() {
        // Transient - not persisted. It has no ID and no corresponding row in DB
        Order order = new Order("order1");
        Assert.assertNull(order.getId());
    }
    
    @Override
    @Transactional
    public void statePersistent() {
        // Transient - has an identifier and is associated with the session
        Order order = new Order("order1");
        session().saveOrUpdate(order);
        Assert.assertNotNull(order.getId());
    }
    
    @Override
    @Transactional
    public void stateDetached() {
        // Detached - entity which has an identifier but it is not longer associated
        //            with a persistance context
        Order order = new Order("order1");
        session().saveOrUpdate(order);
        session().evict(order);
        Assert.assertFalse(session().contains(order));
    }

    @Override
    @Transactional
    public void stateRemoved() {
        // Detached - entity which has an identifier but it is not longer associated
        //            with a persistance context
        Order order = new Order("order1");
        session().saveOrUpdate(order);
        session().delete(order);
        Assert.assertFalse(session().contains(order));
        Assert.assertNotNull(order.getId());
    }
    
}
