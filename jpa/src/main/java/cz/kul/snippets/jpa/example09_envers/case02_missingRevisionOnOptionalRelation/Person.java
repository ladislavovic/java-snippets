package cz.kul.snippets.jpa.example09_envers.case02_missingRevisionOnOptionalRelation;

import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
public class Person {
    
    @Id
    @GeneratedValue
    private Integer id;

    @Audited
    private String name;

    @Audited
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Address address;
    
    @Audited
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Phone phone;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }
    
}