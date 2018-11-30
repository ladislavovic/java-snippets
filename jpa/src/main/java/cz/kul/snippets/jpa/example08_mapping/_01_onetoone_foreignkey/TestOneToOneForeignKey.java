/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.jpa.example08_mapping._01_onetoone_foreignkey;

import cz.kul.snippets.jpa.common.JPATest;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.*;

/**
 * One-to-one relation by foreign key. Notes:
 * <ul>
 *     <li>child has foreign key to parent id. There is also unique constraint. (NOTE: hibernate 5.2 did not generate
 *     the unique constraint, it is weird)</li>
 *     <li>in comparison with "shared primary key" it uses one column more and also one index more. So it
 *     is no so efficient. But if the relation is optional you does not have another option.</li>
 * </ul>
 */
public class TestOneToOneForeignKey extends JPATest {

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
            if (phone == null) {
                if (this.phone != null) {
                    this.phone.setEmployee(null);
                }
            } else {
                phone.setEmployee(this);
            }
            this.phone = phone;
        }

    }

    @Entity
    @Table(name = "phone")
    static class Phone {

        @Id
        @GeneratedValue
        private Long id;

        // Child side of relation
        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "id_employee", nullable = false)
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

}
