package cz.kul.snippets.jpa.example06_notJoinTheParentTableWithJoinStrategy.model;

import javax.persistence.*;

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
