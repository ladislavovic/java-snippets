/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.jpa.exampleMapping._03_onetoone_join_table;

import cz.kul.snippets.jpa.common.JPATest;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.*;

/**
 * One-to-one relation by join table. Notes
 * <ul>
 *     <li>you can map join table as normal entity if you want</li>
 *     <li>according to log, hibernate does not create unique indexes, I think it should</li>
 * </ul>
 */
public class TestOneToOneJoinTable extends JPATest {

    @Entity
    @Table(name = "employee")
    static class Employee {

        @Id
        private Long id;

        public Employee() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    @Entity
    @Table(name = "phone")
    static class Phone {

        @Id
        private Long id;

        // Child side of relation
        @OneToOne(fetch = FetchType.LAZY)
        @JoinTable(
                name = "employee_phone_rel",
                joinColumns = @JoinColumn(name = "id_phone"), // reference to the owning side
                inverseJoinColumns = @JoinColumn(name = "id_employee"))
        private Employee employee;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Employee getEmployee() {
            return employee;
        }

        public void setEmployee(Employee employee) {
            this.employee = employee;
        }
    }

    @Test
    public void testIfItWorks() {
        Phone detachedPhone = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Employee employee = new Employee();
            employee.setId(123L);
            entityManager.persist(employee);
            Phone phone = new Phone();
            phone.setId(456L);
            phone.setEmployee(employee);
            entityManager.persist(phone);
            return phone;
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Phone phone = entityManager.find(Phone.class, detachedPhone.getId());
            Assert.assertEquals(new Long(123), phone.getEmployee().getId());
            return null;
        });
    }

}
