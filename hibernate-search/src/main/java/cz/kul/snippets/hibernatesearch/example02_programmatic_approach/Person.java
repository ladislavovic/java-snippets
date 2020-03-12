package cz.kul.snippets.hibernatesearch.example02_programmatic_approach;

import javax.persistence.*;

@Entity
public class Person extends AbstractPerson {

    public Person() {
    }

    public Person(String name, String surname) {
        super(name, surname);
    }

    @Override
    public String getWholeName() {
        return String.format("%s %s", getName(), getSurname());
    }

}
