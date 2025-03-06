/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.jpa.example08_mapping._02_onetoone_shared_primary_key;

import cz.kul.snippets.jpa.common.JPATest;
import org.junit.Assert;
import org.junit.Test;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Table;

/**
 * One-to-one relation by shared primary key. Notes:
 * <ul>
 *     <li>
 *         the easiest way how to do it is to use @MapsId annotation. It says that join column of
 *         the ralation should be primary key.
 *     </li>
 *     <li>
 *         Id of the child entity is not generated now.
 *     </li>
 *     <li>
 *         Parent side of OneToOne relation is always EAGER! The reason is Hibernate needs type
 *         and ID to create proxy. But it does not have id on parent side so it must load whole entity.
 *         I think it could be optimised, for example when you use shared primary key Hibernates know
 *         what id to use, but it just can't do it.
 *     </li>
 * </ul>
 */
public class TestOneToOneSharedPrimaryKey extends JPATest {

    @Entity
    @Table(name = "employee")
    static class Employee {

        @Id
        @GeneratedValue
        private Long id;

        // parent side of relation
        @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private Phone phone;

        public Employee() { }

        public Long getId() {
            return id;
        }

        public Phone getPhone() {
            return phone;
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }
    }

    @Entity
    @Table(name = "phone")
    static class Phone {

        @Id
        private Long id;

        // Child side of relation
        @OneToOne(fetch = FetchType.LAZY)
        @MapsId
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
    }

    @Test
    public void testPersisting() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Employee employee = new Employee();
            entityManager.persist(employee);

            Phone phone = new Phone();
            phone.setEmployee(employee);
            entityManager.persist(phone);
            entityManager.flush();
            entityManager.clear();

            phone = entityManager.find(Phone.class, phone.getId());
            Assert.assertEquals(employee.getId(), phone.getEmployee().getId());

            return null;
        });
    }

    @Test
    public void testRelationIsNotNullable() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            assertThrows(
                PersistenceException.class, () -> {
                Phone phone = new Phone();
                entityManager.persist(phone);
                entityManager.flush();
            });
            return null;
        });
    }

    @Test
    public void testParentSideOfOneToOneIsAlwaysEager() {
        Employee detachedEmployee = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Employee employee = new Employee();
            entityManager.persist(employee);
            Phone phone = new Phone();
            phone.setEmployee(employee);
            entityManager.persist(employee);
            return employee;
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Employee employee = entityManager.find(Employee.class, detachedEmployee.getId());
            assertInitialized(employee.getPhone());
            return null;
        });
    }

}
