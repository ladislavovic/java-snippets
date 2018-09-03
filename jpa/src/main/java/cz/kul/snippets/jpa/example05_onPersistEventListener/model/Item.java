/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.jpa.example05_onPersistEventListener.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ITEM_TABLE")
public class Item {
    
    @Id
    @GeneratedValue
    private Long id;
    private String value;
    
    @ManyToOne()
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    public Item(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order person) {
        this.order = person;
    }
    
    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", value=" + value + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
