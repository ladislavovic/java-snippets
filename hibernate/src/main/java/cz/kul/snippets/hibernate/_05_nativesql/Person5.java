package cz.kul.snippets.hibernate._05_nativesql;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Person5 {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;
    private String sex;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy="person")
    private Set<PersonDetail5> details;

    public Person5() {
    }

    public Person5(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Set<PersonDetail5> getDetails() {
        return details;
    }

    public void setDetails(Set<PersonDetail5> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", surname=" + surname + "]";
    }
}
