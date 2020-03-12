package cz.kul.snippets.jpa.example09_envers.case03_missingNotAuditedEntity;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

@Entity
//@Audited
public class Person {
    
    @Id
    @GeneratedValue
    private Integer id;

    @Audited
    private String name;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Address address;
    
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

}