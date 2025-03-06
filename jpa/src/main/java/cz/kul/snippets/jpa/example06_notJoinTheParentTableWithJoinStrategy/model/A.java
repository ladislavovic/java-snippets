package cz.kul.snippets.jpa.example06_notJoinTheParentTableWithJoinStrategy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class A {

    @Id
    @GeneratedValue
    private Long id;

    private String parentVal;

    public Long getId() {
        return id;
    }

    public String getParentVal() {
        return parentVal;
    }

    public void setParentVal(String parentVal) {
        this.parentVal = parentVal;
    }
}
