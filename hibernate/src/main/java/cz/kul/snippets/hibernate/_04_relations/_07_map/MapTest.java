/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._04_relations._07_map;

import cz.kul.snippets.hibernate._04_relations._06_manytomany.*;
import cz.kul.snippets.hibernate._04_relations.Runner;
import java.util.List;
import org.hibernate.SessionFactory;
import org.junit.Assert;

/**
 * Relation mapped as a map
 *   * basically it is like mapping list or set but then you have to specify,
 *     what will be used as map key.
 *
 */
public class MapTest extends Runner {

    public MapTest(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected void runImpl() {
        {
            Order order = new Order();
            order.addDetail1(new Detail1("val1"));
            session().saveOrUpdate(order);
            session().flush();
            session().clear();

            Long id = order.getId();
            Order order2 = (Order) session().get(Order.class, id);
            Assert.assertEquals("val1", order2.getDetails1Map().get("val1").getValue());
        }
        
        {
            Order order = new Order();
            order.addDetail2(new Detail2("val1"));
            session().saveOrUpdate(order);
            session().flush();
            session().clear();

            Long id = order.getId();
            Order order2 = (Order) session().get(Order.class, id);
            Assert.assertEquals("val1", order2.getDetails2Map().get("val1").getValue());
        }
        
        {
            DetailType detailType = new DetailType();
            detailType.setId(1L);
            session().saveOrUpdate(detailType);
            Order order = new Order();
            Detail3 detail3 = new Detail3("val3");
            detail3.setDetailType(detailType);
            order.addDetail3(detail3);
            session().saveOrUpdate(order);
            session().flush();
            session().clear();

            Long id = order.getId();
            Order order2 = (Order) session().get(Order.class, id);
            Assert.assertEquals("val3", order2.getDetails3Map().get(detailType).getValue());
        }
        
        
    }

}
