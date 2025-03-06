/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.jpa.example08_mapping._05_onetomany_unidirectional_join_table;

import cz.kul.snippets.jpa.common.JPATest;
import org.junit.Assert;
import org.junit.Test;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Unidirectional One-to-one relation by join table. Notes:
 * <ul>
 *     <li>relation is unidirectional, "many" side does not have the relation</li>
 *     <li>not efficient. Lot of tables, indexes, ... Very simalr to many-to-many structure</li>
 *     <li>it is default mapping for unidirectional one-to-many. You does not have to add
 *     @JoinTable annotation</li>
 * </ul>
 */
public class TestOneToManyUnidirectionalJoinTable extends JPATest {

    @Entity
    @Table(name = "employee")
    static class Employee {

        @Id
        private Long id;

        // it is child side of relation
        @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @JoinTable(
                name = "employee_phone_rel",
                joinColumns = @JoinColumn( name = "id_employee"),
                inverseJoinColumns = @JoinColumn( name = "id_phone"))
        private Set<Phone> phones = new HashSet<>();

        public Employee() { }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Set<Phone> getPhones() {
            return phones;
        }

        public void setPhones(Set<Phone> phones) {
            this.phones = phones;
        }
    }

    @Entity
    @Table(name = "phone")
    static class Phone {

        @Id
        private Long id;

        public Long getId() {
            return id;
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

        public void setId(long id) {
            this.id = id;
        }
    }

    @Test
    public void testIfItWorks() {
        Employee detachedEmployee = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Employee employee = new Employee();
            employee.setId(123L);
            Phone phone = new Phone();
            phone.setId(456L);
            employee.getPhones().add(phone);
            entityManager.persist(employee);
            return employee;
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Employee employee = entityManager.find(Employee.class, detachedEmployee.getId());
            Assert.assertEquals(1, employee.getPhones().size());
            Assert.assertEquals(Long.valueOf(456L), employee.getPhones().iterator().next().getId());
            return null;
        });
    }

}
