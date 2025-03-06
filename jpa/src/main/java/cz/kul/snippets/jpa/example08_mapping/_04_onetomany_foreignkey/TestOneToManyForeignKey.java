/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.jpa.example08_mapping._04_onetomany_foreignkey;

import java.util.List;

import cz.kul.snippets.jpa.common.JPATest;
import org.hibernate.Hibernate;
import org.junit.Assert;
import org.junit.Test;

import jakarta.persistence.Query;

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

    @Test
    public void testIfItWorks() {
        Phone04 detachedPhone = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Employee04 employee = new Employee04();
            Phone04 phone = new Phone04();
            employee.addPhone(phone);
            entityManager.persist(employee);
            return phone;
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Phone04 phone = entityManager.find(Phone04.class, detachedPhone.getId());
            Assert.assertNotNull(phone.getEmployee());
            return null;
        });
    }

    @Test
    public void plainHQLJoinDoesNotFetchData() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Employee04 employee = new Employee04();
            employee.addPhone(new Phone04("111 111 111"));
            employee.addPhone(new Phone04("222 222 222"));
            employee.addPhone(new Phone04("333 333 333"));
            entityManager.persist(employee);
            return null;
        });

        Employee04 detachedEmployee = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Query query = entityManager.createQuery("select distinct e from Employee04 e join e.phones p where e.id > 0");
            List<Employee04> list = query.getResultList();
            return list.get(0);
        });
        Assert.assertFalse(Hibernate.isInitialized(detachedEmployee.getPhones()));

        detachedEmployee = jpaService().doInTransactionAndFreshEM(entityManager -> {
            Query query = entityManager.createQuery("select distinct e from Employee04 e join fetch e.phones p where e.id > 0");
            List<Employee04> list = query.getResultList();
            return list.get(0);
        });
        Assert.assertTrue(Hibernate.isInitialized(detachedEmployee.getPhones()));
    }



}
