package cz.kul.snippets.jpa.example06_notJoinTheParentTableWithJoinStrategy.model;

import cz.kul.snippets.jpa.example06_notJoinTheParentTableWithJoinStrategy.model.A;
import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import javax.persistence.Entity;

@Entity
public class SubA extends A {

    private String val;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

}
