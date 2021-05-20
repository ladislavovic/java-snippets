package cz.kul.snippets.hibernatesearch6.example08_collection_indexing;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String surname;

    private String sex;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Set<Car> cars = new HashSet<>();

    public Person() {
    }

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public List<String> getCarModels() {
        return cars.stream().map(Car::getModel).collect(Collectors.toList());
    }

    public String getWholeName() {
        return String.format("%s %s", getName(), getSurname());
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

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", surname=" + surname + "]";
    }

}
