/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._05_nativesql;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PersonDetail5 {
    
    @Id
    @GeneratedValue
    private Long id;
    private String key;
    private String value;
    
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)    private Person5 person;
    
    public PersonDetail5() {
    }
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public Person5 getPerson() {
        return person;
    }

    public void setPerson(Person5 person) {
        this.person = person;
    }
    
    @Override
    public String toString() {
        return "PersonDetail{" + "id=" + id + ", key=" + key + ", value=" + value + '}';
    }
    
}
