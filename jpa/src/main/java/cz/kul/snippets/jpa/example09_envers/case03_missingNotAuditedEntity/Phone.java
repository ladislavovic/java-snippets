package cz.kul.snippets.jpa.example09_envers.case03_missingNotAuditedEntity;

import cz.kul.snippets.jpa.example09_envers.case03_missingNotAuditedEntity.hibernatecustomization.EnversEmptyEntityIfNotFoundTuplizer;
import org.hibernate.annotations.Tuplizer;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Audited
@Tuplizer(impl = EnversEmptyEntityIfNotFoundTuplizer.class)
public class Phone {

    @Id
    @GeneratedValue
    private Integer phoneId;

    private String number;

    public Integer getPhoneId() {
        return phoneId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    
}
