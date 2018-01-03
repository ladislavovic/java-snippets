/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._04_relations._04_onetomany_foreignkey;

import cz.kul.snippets.hibernate._04_relations.Runner;
import java.util.List;
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
public class OneToManyForeignKeyTest extends Runner {

    public OneToManyForeignKeyTest(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected void runImpl() {
        Order order = new Order();
        order.addOrderDetail(new OrderDetail());
        order.addOrderDetail(new OrderDetail());
        order.addOrderDetail(new OrderDetail());
        session().saveOrUpdate(order);
        session().flush();        
        session().clear();
        
        Order order2 = (Order) session().get(Order.class, order.getId());
        List<OrderDetail> details = order2.getOrderDetails(); 
        Assert.assertEquals(3, details.size());
    }
    
}
