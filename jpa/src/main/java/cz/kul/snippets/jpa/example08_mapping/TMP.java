package cz.kul.snippets.jpa.example08_mapping;

import cz.kul.snippets.jpa.common.JPATest;
import org.junit.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TMP extends JPATest {

    @Entity(name = "EmployeeEntity")
    @Table(name = "employee")
    public static class Employee {

        @Id
        @GeneratedValue
        private Long id;

        @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
        private Set<A> aSet = new HashSet<>();

        public Employee() { }

        public Long getId() {
            return id;
        }

        public Set<A> getaSet() {
            return aSet;
        }

        public void setaSet(Set<A> aSet) {
            this.aSet = aSet;
        }
    }

    @Entity(name = "PhoneEntity")
    @Table(name = "phone")
    public static class Phone {

        @Id
        @GeneratedValue
        private Long id;

        // Child side of relation
        @ManyToOne(fetch = FetchType.LAZY)
        private Employee employee;

        private int status;

        public Long getId() {
            return id;
        }

        public Employee getEmployee() {
            return employee;
        }

        public void setEmployee(Employee employee) {
            this.employee = employee;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Phone phone = (Phone) o;
            return id != null && Objects.equals( id, phone.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    @Entity(name = "AEntity")
    @Inheritance(strategy = InheritanceType.JOINED)
    public static class A {

        @Id
        @GeneratedValue
        private Long id;

        private String valA;

        @ManyToOne(fetch = FetchType.LAZY)
        private Employee employee;

        public Long getId() {
            return id;
        }

        public String getValA() {
            return valA;
        }

        public void setValA(String valA) {
            this.valA = valA;
        }

        public Employee getEmployee() {
            return employee;
        }

        public void setEmployee(Employee employee) {
            this.employee = employee;
        }
    }

    @Entity(name = "SubAEntity")
    public static class SubA extends A {

        private String valSubA;

        public String getValSubA() {
            return valSubA;
        }

        public void setValSubA(String valSubA) {
            this.valSubA = valSubA;
        }
    }

    @Test
    public void testIfItWorks() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            System.out.println("START");
//			Query q = session.createQuery( "select valSubA from SubAEntity" );
//			Query q = session.createQuery( "select ae from AEntity ae join fetch ae.b" );
//            javax.persistence.Query query = entityManager.createQuery("select p.status from EmployeeEntity e join PhoneEntity p on (p.employee.id = e.id and p.status > :status and p.id = :pid) where e.id = 10");
            Query query = entityManager.createQuery("select suba.valSubA from EmployeeEntity e join SubAEntity suba on (e.id = suba.employee.id) where e.id = 10");
//            query.setParameter("status", 5);
//            query.setParameter("pid", 5L);
//			Query q = session.createQuery( "select e from EmployeeEntity e" );
            System.out.println("END");
            query.getResultList();
            return null;
        });
    }

}
