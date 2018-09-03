/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.jpa.exampleMapping._06_manytomany;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import cz.kul.snippets.jpa.common.JPATest;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.Runner;

import javax.persistence.*;

/**
 * Many-to-many relation.
 */
public class TestManyToMany extends JPATest {

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
            phones.add(phone);
            phone.getEmployees().add(this);
        }
    }

    @Entity
    @Table(name = "phone")
    static class Phone {

        @Id
        @GeneratedValue
        private Long id;

        @ManyToMany(mappedBy = "phones", fetch = FetchType.LAZY)
        private Set<Employee> employees = new HashSet<>();

        public Long getId() {
            return id;
        }

        public Set<Employee> getEmployees() {
            return employees;
        }

        public void setEmployees(Set<Employee> employees) {
            this.employees = employees;
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
        Employee[] detachedEmployees = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Employee employee1 = new Employee();
            Employee employee2 = new Employee();
            Phone phone1 = new Phone();
            Phone phone2 = new Phone();
            employee1.addPhone(phone1);
            employee1.addPhone(phone2);
            employee2.addPhone(phone2);
            entityManager.persist(employee1);
            entityManager.persist(employee2);
            return new Employee[] {employee1, employee2};
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Employee employee1 = entityManager.find(Employee.class, detachedEmployees[0].getId());
            Employee employee2 = entityManager.find(Employee.class, detachedEmployees[1].getId());
            Assert.assertEquals(2, employee1.getPhones().size());
            Assert.assertEquals(1, employee2.getPhones().size());
            return null;
        });
    }

}
