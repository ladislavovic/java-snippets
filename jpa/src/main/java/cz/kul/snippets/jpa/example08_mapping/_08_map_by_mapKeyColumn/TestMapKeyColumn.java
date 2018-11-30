package cz.kul.snippets.jpa.example08_mapping._08_map_by_mapKeyColumn;

import cz.kul.snippets.jpa.common.JPATest;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Mapping a relation as a java.util.Map
 *
 * This example shows how to map, if the map key is NOT from referenced table and it is basic type. Notes:
 *
 * <ul>
 *     <li>in many-to-many relation another column for key value is created in relation table.</li>
 * </ul>
 */
public class TestMapKeyColumn extends JPATest {

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
        @MapKeyColumn(name = "type_KEY")
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
        final String PHONE_TYPE_PHONE = "PHONE";
        final String PHONE_TYPE_MOBILE = "MOBILE";
        final String PHONE_NUM_1 = "111 111 111";
        final String PHONE_NUM_2 = "222 222 222";

        Employee detachedEmployee = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Employee employee = new Employee();
            Phone phone1 = new Phone();
            Phone phone2 = new Phone();
            phone1.setNumber(PHONE_NUM_1);
            phone2.setNumber(PHONE_NUM_2);
            employee.getPhones().put(PHONE_TYPE_PHONE, phone1);
            employee.getPhones().put(PHONE_TYPE_MOBILE, phone2);
            entityManager.persist(employee);
            return employee;
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Employee employee = entityManager.find(Employee.class, detachedEmployee.getId());
            Assert.assertEquals(2, employee.getPhones().size());
            Assert.assertEquals(PHONE_NUM_1, employee.getPhones().get(PHONE_TYPE_PHONE).getNumber());
            return null;
        });
    }

    @Test
    public void tmp() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Query query = entityManager.createQuery("select entry(e.phones) from " + Employee.class.getName() + " e");
            query.getResultList();
            return null;
        });
    }

}
