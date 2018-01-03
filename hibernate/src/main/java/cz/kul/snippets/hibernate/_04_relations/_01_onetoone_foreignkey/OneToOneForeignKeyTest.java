/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._04_relations._01_onetoone_foreignkey;

import cz.kul.snippets.hibernate._04_relations.Runner;
import org.hibernate.SessionFactory;
import org.junit.Assert;

/**
 * One-to-one relation by foreign key. Notes:
 *   * it is default way how to do one-to-one relation
 *   * if you save whole structure starting from parent, you have to set
 *     link to parent in child entity manually. Hibernate does not do it
 *     automatically. The methods which manage it are called "link management
 *     methods"
 *   * cascade can be on both sides of relation
 */
public class OneToOneForeignKeyTest extends Runner {

    public OneToOneForeignKeyTest(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected void runImpl() {
        Order order = new Order();
        order.setOrderDetail(new OrderDetail());
        order.getOrderDetail().setPrice(10.0);
        session().saveOrUpdate(order);
        session().flush();        
        session().clear();
        
        Order order2 = (Order) session().get(Order.class, order.getId());
        OrderDetail detail = order2.getOrderDetail(); 
        Assert.assertEquals(order.getId(), order2.getId());
        Assert.assertEquals(10.0, detail.getPrice(), 0.01);
    }
    
}
