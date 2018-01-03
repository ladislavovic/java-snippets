/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._04_relations._06_manytomany;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author kulhalad
 */
@Entity(name="order_detail_many_to_many")
public class OrderDetail {
    
    @Id
    @GeneratedValue
    @Column(name = "id_order_detail")
    private Long id;

    // It is a child side of relation
//    @ManyToMany
//    @JoinTable( name = "order_order_detail_rel_mtm", 
//		joinColumns = @JoinColumn( name = "id_order_detail"),
//                inverseJoinColumns = @JoinColumn( name = "id_order"))
    @ManyToMany(mappedBy = "orderDetails")
    public List<Order> orders = new ArrayList<>();

    private Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void addOrder(Order order) {
        orders.add(order);
    }
    
}
