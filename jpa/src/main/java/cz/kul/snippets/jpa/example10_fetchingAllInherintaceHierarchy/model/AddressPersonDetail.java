package cz.kul.snippets.jpa.example10_fetchingAllInherintaceHierarchy.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

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
