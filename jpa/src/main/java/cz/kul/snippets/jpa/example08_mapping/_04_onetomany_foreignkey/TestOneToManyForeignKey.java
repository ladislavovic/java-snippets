/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.jpa.example08_mapping._04_onetomany_foreignkey;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import cz.kul.snippets.jpa.common.JPATest;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.*;

/**
 * One-to-many relation by foreign key. Notes:
 * <ul>
 *     <li>implement it by foreign key is the most common and most efficient way</li>
 *     <li>it can exists in uni and also bi directional way</li>
 *     <li>if you the relation is bidirecitonal, you shoud have "link management methods"
 *     on parent side</li>
 * </ul>
 */
public class TestOneToManyForeignKey extends JPATest {

    @Entity
    @Table(name = "employee")
    static class Employee {

        @Id
        @GeneratedValue
        private Long id;

        // parent side of relation
        @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private Set<Phone> phones = new HashSet<>();

        public Employee() { }

        public Long getId() {
            return id;
        }

        public Set<Phone> getPhones() {
            return phones;
        }

        public void setPhones(Set<Phone> phones) {
            this.phones = phones;
        }

        public void addPhone(Phone phone) {
            this.phones.add(phone);
            phone.setEmployee(this);
        }

        public void removePhone(Phone phone) {
            this.phones.remove(phone);
            phone.setEmployee(null);
        }
    }

    @Entity
    @Table(name = "phone")
    static class Phone {

        @Id
        @GeneratedValue
        private Long id;

        // Child side of relation
        @ManyToOne(fetch = FetchType.LAZY)
        private Employee employee;

        public Long getId() {
            return id;
        }

        public Employee getEmployee() {
            return employee;
        }

        public void setEmployee(Employee employee) {
            this.employee = employee;
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
        Phone detachedPhone = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Employee employee = new Employee();
            Phone phone = new Phone();
            employee.addPhone(phone);
            entityManager.persist(employee);
            return phone;
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Phone phone = entityManager.find(Phone.class, detachedPhone.getId());
            Assert.assertNotNull(phone.getEmployee());
            return null;
        });
    }

}
