package cz.kul.snippets.hibernatesearch._02_programmatic_approach;

import cz.kul.snippets.hibernatesearch._01_helloworld.PersonDetail;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.Set;

@Entity
@Indexed
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @Field
    private String name;

    @Field
    private String surname;

    private String sex;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy="person")
    private Set<PersonDetail> details;

    public Person() {
    }

    public Person(String name, String surname) {
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

    public Set<PersonDetail> getDetails() {
        return details;
    }

    public void setDetails(Set<PersonDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", surname=" + surname + "]";
    }
}
