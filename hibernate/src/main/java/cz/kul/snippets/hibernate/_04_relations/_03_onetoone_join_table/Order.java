/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._04_relations._03_onetoone_join_table;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author kulhalad
 */
@Entity(name = "order_one_to_one_join_table")
public class Order implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id_order")
    private Long id;
    
    // it is parent side of relation
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "order")
    private OrderDetail orderDetail;
    
    public Order() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
        orderDetail.setOrder(this);
    }
    
}
