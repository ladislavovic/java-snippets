package cz.kul.snippets.jpa.example09_envers.case01_valueOfANotAuditedField;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Person {
    
    @Id
    @GeneratedValue
    private Integer id;

    @Audited
    private String name;

    @Column(nullable = false)
    private String surname;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    
}