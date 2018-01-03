/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._02_entitystate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author kulhalad
 */
@Entity(name = "TOrder")
public class Order {

    @Id
    @GeneratedValue
    private Long id;
    
    private String invoiceText;

    public Order() {
    }
    
    public Order(String invoiceText) {
        this.invoiceText = invoiceText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceText() {
        return invoiceText;
    }

    public void setInvoiceText(String invoiceText) {
        this.invoiceText = invoiceText;
    }
    
}
