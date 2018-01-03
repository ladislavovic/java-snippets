/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._04_relations._03_onetoone_join_table;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author kulhalad
 */
@Entity(name = "order_order_detail_rel")
public class OrderOrderDetailRel implements Serializable {
    
    @Id
    @Column(name = "id_order")
    private Long idOrder;
    
    @Id
    @Column(name = "id_order_detail")
    private Long idOrderDetail;

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    public Long getIdOrderDetail() {
        return idOrderDetail;
    }

    public void setIdOrderDetail(Long idOrderDetail) {
        this.idOrderDetail = idOrderDetail;
    }
    
}
