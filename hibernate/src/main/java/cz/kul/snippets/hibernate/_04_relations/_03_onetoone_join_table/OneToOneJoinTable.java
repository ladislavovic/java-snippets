/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._04_relations._03_onetoone_join_table;

import cz.kul.snippets.hibernate._04_relations.Runner;
import java.util.List;
import org.hibernate.SessionFactory;
import org.junit.Assert;

/**
 * One-to-one relation by join table. Notes
 *     * do not know if join table has unique indexes to guarantee one-by-one relation
 *     * you can map join table as normal entity if you want
 */
public class OneToOneJoinTable extends Runner {

    public OneToOneJoinTable(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected void runImpl() {
        // To increase id to prevent all ids starts from 1
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
        Order order2 = (Order) session().get(Order.class, orderId);
        OrderDetail detail = order2.getOrderDetail();
        Assert.assertEquals(10.0, detail.getPrice(), 0.01);
        
        List<OrderOrderDetailRel> rels = session().createCriteria(OrderOrderDetailRel.class).list();
        Assert.assertEquals(1, rels.size());
        Assert.assertEquals(orderId, rels.get(0).getIdOrder());
        Assert.assertEquals(detail.getId(), rels.get(0).getIdOrderDetail());
    }
    
}
