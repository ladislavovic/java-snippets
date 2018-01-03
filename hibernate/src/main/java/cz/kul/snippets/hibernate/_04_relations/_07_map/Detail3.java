/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._04_relations._07_map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author kulhalad
 */
@Entity(name = "detail3")
public class Detail3 {
    
    @Id
    @GeneratedValue
    @Column(name = "id_order_detail_3")
    private Long id;
    
    @Column(name = "detail3_value")
    private String value;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_order_map")
    private Order order;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_detail_type")
    private DetailType detailType;

    public DetailType getDetailType() {
        return detailType;
    }

    public void setDetailType(DetailType detailType) {
        this.detailType = detailType;
    }

    public Detail3() {
    }
    
    public Detail3(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
    
}
