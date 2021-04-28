package cz.kul.snippets.jpa.example10_fetchingAllInherintaceHierarchy.model;

import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SecondaryTable;
import java.util.Set;

@Entity
public class Person {
    
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String name;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    @BatchSize(size = 50)
    private Set<PersonDetail<?>> personDetails;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PersonDetail<?>> getPersonDetails() {
        return personDetails;
    }

    public void setPersonDetails(Set<PersonDetail<?>> personDetails) {
        this.personDetails = personDetails;
    }

    public <T> T findPersonDetail(Class<T> personDetailClass) {
        return personDetails.stream()
            .filter(x -> personDetailClass.isInstance(x))
            .map(x -> (T) x)
            .findAny()
            .get();
    }

}