package cz.kul.snippets.jpa.example08_mapping._04_onetomany_foreignkey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "phone")
class Phone04 {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="phone_number")
    private String number;

    // Child side of relation
    @ManyToOne(fetch = FetchType.LAZY)
    private Employee04 employee;

    public Phone04() {
    }

    public Phone04(String number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public Employee04 getEmployee() {
        return employee;
    }

    public void setEmployee(Employee04 employee) {
        this.employee = employee;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone04 phone = (Phone04) o;
        return id != null && Objects.equals(id, phone.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
