/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._04_relations._06_manytomany;

import cz.kul.snippets.hibernate._04_relations.Runner;
import java.util.List;
import org.hibernate.SessionFactory;
import org.junit.Assert;

/**
 * Many-to-many relation. Notes:
 *     * here it is very important which side is owner of the relation. JPA persist
 *       state according to owning (child) side, parent side is ignored. For example 
 *       if you add child to parent and save parent, child is not saved. Just
 *       only child side is managing relation table.
 *       So you have to manage bidirectional M2M relation yourself.
 *       To be more general, it is feature of JPA: 
 *         <b>As the relationship is bi-directional,
 *         so as the application updates one side of the relationship, the other side should 
 *         also get updated, and be in synch. It is responsibility of application, not JPA.</b>
 */
public class ManyToManyTest extends Runner {

    public ManyToManyTest(SessionFactory sessionFactory) {
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
        List<OrderDetail> details = order2.getOrderDetails();
        Assert.assertEquals(3, details.size());
    }
    
}
