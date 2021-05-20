package cz.kul.snippets.hibernatesearch6.example05_elasticsearch;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AbstractPerson {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String surname;

    private String sex;

    public AbstractPerson() {
    }

    public AbstractPerson(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public abstract String getWholeName();

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
    
    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", surname=" + surname + "]";
    }

}
