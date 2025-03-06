package cz.kul.snippets.jpa.example06_notJoinTheParentTableWithJoinStrategy.model;

import jakarta.persistence.Entity;

@Entity
public class SubB extends A {

    private Long val2;

    public Long getVal2() {
        return val2;
    }

    public void setVal2(Long val2) {
        this.val2 = val2;
    }

}
