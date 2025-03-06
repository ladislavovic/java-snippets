package cz.kul.snippets.jpa.example08_mapping._07_map_by_mapKey;

import cz.kul.snippets.jpa.common.JPATest;
import org.junit.Assert;
import org.junit.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapKey;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import java.util.*;

/**
 * Mapping a relation as a java.util.Map
 *
 * The most common way is to map *-to-many relation as a collection. But you can map it also as a map. The imprtant
 * question is what to use as a key. According to that you choose a way how to map it.
 *
 * This example shows how to map, if the map key is from referenced table.
 *
 * TODO @MapKeyJoinColumn/@MapKeyJoinColumns in another example
 */
public class TestMapByMapKey extends JPATest {

    @Entity
    @Table(name = "employee")
    static class Employee {

        @Id
        @GeneratedValue
        private Long id;

        // it is child side of relation (relation owner)
        @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @JoinTable(
                name = "employee_phone_rel",
                joinColumns = @JoinColumn( name = "id_employee"),
                inverseJoinColumns = @JoinColumn( name = "id_phone"))
        @MapKey(name = "number")
        private Map<String, Phone> phones = new HashMap<>();

        public Employee() { }

        public Long getId() {
            return id;
        }

        public Map<String, Phone> getPhones() {
            return phones;
        }

        public void setPhones(Map<String, Phone> phones) {
            this.phones = phones;
        }

    }

    @Entity
    @Table(name = "phone")
    static class Phone {

        @Id
        @GeneratedValue
        private Long id;

        private String number;

        public Long getId() {
            return id;
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
            Phone phone = (Phone) o;
            return id != null && Objects.equals(id, phone.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    @Test
    public void testIfItWorks() {
        final String PHONE_NUM_1 = "111 111 111";
        final String PHONE_NUM_2 = "222 222 222";

        Employee detachedEmployee = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Employee employee = new Employee();
            Phone phone1 = new Phone();
            Phone phone2 = new Phone();
            phone1.setNumber(PHONE_NUM_1);
            phone2.setNumber(PHONE_NUM_2);
            employee.getPhones().put(PHONE_NUM_1, phone1);
            employee.getPhones().put(PHONE_NUM_2, phone2);
            entityManager.persist(employee);
            return employee;
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Employee employee = entityManager.find(Employee.class, detachedEmployee.getId());
            Assert.assertEquals(2, employee.getPhones().size());
            Assert.assertEquals(PHONE_NUM_1, employee.getPhones().get(PHONE_NUM_1).getNumber());
            return null;
        });
    }

}
