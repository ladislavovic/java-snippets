package cz.kul.snippets.jpa.example09_envers.case02_missingRevisionOnOptionalRelation;

import cz.kul.snippets.jpa.example09_envers.case02_missingRevisionOnOptionalRelation.hibernatecustomization.EnversEmptyEntityIfNotFoundTuplizer;
import org.hibernate.annotations.Tuplizer;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Audited
@Tuplizer(impl = EnversEmptyEntityIfNotFoundTuplizer.class)
public class Address {

    @Id
    @GeneratedValue
    private Integer id;

    private String address;

    public Integer getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
}
