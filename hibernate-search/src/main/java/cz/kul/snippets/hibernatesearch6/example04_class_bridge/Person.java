package cz.kul.snippets.hibernatesearch6.example04_class_bridge;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String surname;

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

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", surname=" + surname + "]";
    }

}
