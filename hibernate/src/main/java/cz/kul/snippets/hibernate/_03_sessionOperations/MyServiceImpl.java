/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._03_sessionOperations;

import org.hibernate.Hibernate;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kulhalad
 */
@Service
public class MyServiceImpl implements MyService {
    
    @Autowired
    private SessionFactory sessionFactory;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
    
    @Override
    @Transactional
    public void contains() {
        Order order = new Order("order");
        Assert.assertFalse(session().contains(order));
        
        session().save(order);
        Assert.assertTrue(session().contains(order));
        
        session().evict(order);
        Assert.assertFalse(session().contains(order));
    }

    @Override
    @Transactional
    public void evict() {
        Assert.assertEquals(0, getEntityCount());
        
        Order order = new Order("order");
        session().save(order);
        Assert.assertEquals(1, getEntityCount());
        
        session().evict(order);
        Assert.assertEquals(0, getEntityCount());
    }
    
    @Override
    @Transactional
    public void get() {
        Order order = new Order("order");
        session().save(order);
        session().evict(order);
        Long id = order.getId();
        
        Order o1 = (Order) session().get(Order.class, id);
        Order o2 = (Order) session().get(Order.class, id);
        Assert.assertTrue(o1 == o2);
        
        Assert.assertNull(session().get(Order.class, 12345L));
    }

    @Override
    @Transactional
    public void load() {
        // It returns the proxy only. Not the real object.
        // So it returns proxy even though the object does not
        // exists;
        // Sometimes it is good for performance. For example:
        //   Stock stock = (Stock)session.load(Stock.class, new Integer(2));
        //   StockTransaction stockTransactions = new StockTransaction();
        //   stockTransactions.setStock(stock);        
        //   session.save(stockTransactions);
        Order order = (Order) session().load(Order.class, 123L);
        Assert.assertFalse(Hibernate.isInitialized(order));
        try {
            order.setInvoiceText("inv");
            session().flush();
            Assert.fail();
        } catch (ObjectNotFoundException ex) {
            // because it can not set property, the
            // object does not exists
        }
    }

    @Override
    @Transactional
    public void merge() {
        // Copy the state of the given object onto the persistent object with the same identifier.
        // If it is transient instance, it is persisted first
        //
        // It is a way how to update data in DB. 
        // It is the preferred way over update and in JPA it it the only way
        //
        // Differences with update()
        //   * update does not returns anything, merge returns updated object
        //   * you can not update object, which is already associated
        //     with the session (given object can be persisted, but when you
        //     update by transient or detached instance and the instance with
        //     the same id is already in session, an exception is thrown)
        
        // Update
        Order detachedOrder = getDetachedOrder();
        detachedOrder.setInvoiceText("updated");
        Order persisted = (Order) session().merge(detachedOrder);
        Assert.assertEquals(detachedOrder.getId(), persisted.getId());
        Assert.assertEquals("updated", persisted.getInvoiceText());
        
        // Persist new instance by merge
        Order orderTransiend = new Order("inv");
        Order orderPersisted = (Order) session().merge(orderTransiend);
        Assert.assertTrue(orderTransiend.getId() == null);
        Assert.assertTrue(orderPersisted.getId() != null);
        Assert.assertTrue(orderTransiend != orderPersisted);
        
        // Merge persistent instance (it is useless but you can)
        session().merge(orderPersisted);
    }
    
    @Override
    @Transactional
    public void saveOrUpdate() {
        // save transient instance
        Order order = new Order();
        Long id = (Long) session().save(order);
        Assert.assertEquals(id, order.getId());
        session().flush();
        session().clear();
        
        // update detached instance
        Order detached = getDetachedOrder();
        detached.setInvoiceText("updated");
        session().saveOrUpdate(detached);
        
        // can't update if instance is already associated with the session
        Order detached2 = getDetachedOrder();
        Order persisted = (Order) session().get(Order.class, detached2.getId());
        detached2.setInvoiceText("updated");
        try {
            session().saveOrUpdate(detached2);
            Assert.fail("It should fail because the entity is already associated with the session");
        } catch (NonUniqueObjectException e) {
        }
    }

    @Override
    @Transactional
    public void refresh() {
        // Re-read the state of the given instance from the db. Not usable very
        // often. It is good when for example data are changed by trigger or
        // by sql update and you need to have actual data.        
        forTransient : {
            Order order = new Order("order");
            try {
                session().refresh(order);
                Assert.fail("not possible for transient objects.");
            } catch (Exception e) {
            }
        }
        
        forPersistent : {
            Order order = new Order("order1");
            session().saveOrUpdate(order);
            SQLQuery sql = session().createSQLQuery("update TOrder2 set invoiceText = 'aaa'");
            sql.executeUpdate();            
            Assert.assertEquals("order1", order.getInvoiceText());
            session().refresh(order);
            Assert.assertEquals("aaa", order.getInvoiceText());
        }
        
        forDetached : {
            Order detachedOrder = getDetachedOrder();
            String dbInvoiceText = detachedOrder.getInvoiceText();
            String newInvoiceText = "foo_bar";
            detachedOrder.setInvoiceText(newInvoiceText);
            session().refresh(detachedOrder);
            Assert.assertNotEquals(dbInvoiceText, newInvoiceText);
            Assert.assertEquals(dbInvoiceText, detachedOrder.getInvoiceText());
        }
        
    }

    private int getEntityCount(){
        return session().getStatistics().getEntityCount();
    }
    
    private Order getDetachedOrder() {
        Order order = new Order("detached");
        Order saved = (Order) session().merge(order);
        session().flush();
        session().evict(saved);
        return saved;
    }

}
