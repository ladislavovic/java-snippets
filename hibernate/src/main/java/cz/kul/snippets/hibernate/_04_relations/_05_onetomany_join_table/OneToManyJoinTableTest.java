/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._04_relations._05_onetomany_join_table;

import cz.kul.snippets.hibernate._04_relations.Runner;
import java.util.List;
import org.hibernate.SessionFactory;
import org.junit.Assert;

/**
 * One-to-one relation by join table. Notes
 *     * do not know if join table has unique indexes to guarantee one-by-one relation
 *     * you can map join table as normal entity if you want
 */
public class OneToManyJoinTableTest extends Runner {

    public OneToManyJoinTableTest(SessionFactory sessionFactory) {
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
        
        Long orderId = order.getId();
        Order order2 = (Order) session().get(Order.class, orderId);
        Assert.assertEquals(3, order2.getOrderDetails().size());
    }
    
}
