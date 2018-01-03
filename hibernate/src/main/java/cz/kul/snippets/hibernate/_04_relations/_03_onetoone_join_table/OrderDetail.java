/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._04_relations._03_onetoone_join_table;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author kulhalad
 */
@Entity(name="order_detail_one_to_one_join_table")
public class OrderDetail {
    
    @Id
    @GeneratedValue
    @Column(name = "id_order_detail")
    private Long id;

    // It is a child side of relation
    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable( name = "order_order_detail_rel", 
		joinColumns = @JoinColumn( name = "id_order_detail"),
                inverseJoinColumns = @JoinColumn( name = "id_order"))
    public Order order;

    private Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
