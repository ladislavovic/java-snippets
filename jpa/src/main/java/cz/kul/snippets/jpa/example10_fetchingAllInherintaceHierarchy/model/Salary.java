package cz.kul.snippets.jpa.example10_fetchingAllInherintaceHierarchy.model;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Salary {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private int salary;

    public Integer getId() {
        return id;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
    
}
