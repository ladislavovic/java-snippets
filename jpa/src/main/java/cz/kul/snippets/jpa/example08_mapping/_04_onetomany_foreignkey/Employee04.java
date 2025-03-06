package cz.kul.snippets.jpa.example08_mapping._04_onetomany_foreignkey;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employee")
class Employee04 {

    @Id
    @GeneratedValue
    private Long id;

    // parent side of relation
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Phone04> phones = new HashSet<>();

    public Employee04() { }

    public Long getId() {
        return id;
    }

    public Set<Phone04> getPhones() {
        return phones;
    }

    public void setPhones(Set<Phone04> phones) {
        this.phones = phones;
    }

    public void addPhone(Phone04 phone) {
        this.phones.add(phone);
        phone.setEmployee(this);
    }

    public void removePhone(Phone04 phone) {
        this.phones.remove(phone);
        phone.setEmployee(null);
    }
}
