/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._04_relations._02_onetoone_shared_primary_key;

import cz.kul.snippets.hibernate._04_relations.Runner;
import org.hibernate.SessionFactory;
import org.junit.Assert;

/**
 * One-to-one relation by shared primary key. Notes:
 */
public class OneToOneSharedPrimaryKey extends Runner {

    public OneToOneSharedPrimaryKey(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected void runImpl() {
        // To increase id
        session().save(new Order());
        session().save(new Order());
        session().save(new Order());
        
        Order order = new Order();
        order.setOrderDetail(new OrderDetail());
        order.getOrderDetail().setPrice(10.0);
        session().saveOrUpdate(order);
        session().flush();        
        session().clear();
        
        Long orderId = order.getId();
        Order order2 = (Order) session().get(Order.class, order.getId());
        OrderDetail detail = order2.getOrderDetail();
        Assert.assertEquals(orderId, detail.getIdOrder()); // so PK is shared
        Assert.assertEquals(10.0, detail.getPrice(), 0.01);
    }
    
}
