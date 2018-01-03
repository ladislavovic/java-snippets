/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._04_relations._06_manytomany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author kulhalad
 */
@Entity(name = "order_many_to_many")
public class Order implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id_order")
    private Long id;
    
    // it is child side of relation (relation owner)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable( name = "order_order_detail_rel_mtm", 
		joinColumns = @JoinColumn( name = "id_order"),
                inverseJoinColumns = @JoinColumn( name = "id_order_detail"))
    private List<OrderDetail> orderDetails = new ArrayList<>();
    
    public Order() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
    
    public void addOrderDetail(OrderDetail od) {
        orderDetails.add(od);
    }
    
}
