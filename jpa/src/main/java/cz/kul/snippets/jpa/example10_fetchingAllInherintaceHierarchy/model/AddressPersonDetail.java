package cz.kul.snippets.jpa.example10_fetchingAllInherintaceHierarchy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;

@Entity
public class AddressPersonDetail extends PersonDetail<Address> {

    @ManyToOne(fetch = FetchType.LAZY)
    private Address address;

    public Address getDetail() {
        return address;
    }

    public void setDetail(Address address) {
        this.address = address;
    }

}
