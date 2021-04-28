package cz.kul.snippets.hibernatesearch.example05_elasticsearch;

import javax.persistence.Entity;

@Entity
public class ArtificialPerson extends AbstractPerson {

    public ArtificialPerson() {
    }

    public ArtificialPerson(String name) {
        super(name, "");
    }

    @Override
    public String getWholeName() {
        return String.format("Artificial person %s", getName());
    }

}
