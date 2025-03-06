package cz.kul.snippets.jpa.example07_nativeQuery;

import cz.kul.snippets.jpa.common.JPATest;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;
import org.junit.Assert;
import org.junit.Test;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import java.math.BigInteger;

public class TestNativeQuery extends JPATest {

    @Entity
    @Table(name = "employee")
    public static class Employee {
        @Id
        @GeneratedValue
        private Long id;

        @Column(name = "val_tiny_int", columnDefinition = "TINYINT")
        private Long valTinyInt;

        @Column(name = "val_big_int", columnDefinition = "BIGINT")
        private Long valBigInt;

        public Employee(Long valTinyInt, Long valBigInt) {
            this.valTinyInt = valTinyInt;
            this.valBigInt = valBigInt;
        }

        public Long getId() {
            return id;
        }

        public Long getValTinyInt() {
            return valTinyInt;
        }

        public Long getValBigInt() {
            return valBigInt;
        }
    }

    /**
     * This is the most simple sql query in hibernate.
     * <p>
     * Note, that you do not specify what the query returns so it returns
     * instances of BigInteger. It is a little bit unexpected and probably
     * depends on particular DB scheme.
     */
    @Test
    public void testSimpleNativeQuery() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            entityManager.persist(new Employee(10L, 20L));
            return null;
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Query q1 = entityManager.createNativeQuery("SELECT val_tiny_int FROM employee");
            Object result = q1.getResultList().get(0);
            Assert.assertEquals(Byte.class, result.getClass());
            return null;
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Query q1 = entityManager.createNativeQuery("SELECT val_big_int FROM employee");
            Object result = q1.getResultList().get(0);
            Assert.assertEquals(BigInteger.class, result.getClass());
            return null;
        });
    }

    /**
     * By addScalar() method you can explicitely say what is returned
     * by the query. Then Hibernate do not work with ResultSetMetadata
     * and get metadata information from your specification. Notes:
     * <ul>
     * <li>it is quicker - no ResultSetMetadata overhead</li>
     * <li>you can specify what is to be returned - Integer, Long, BigInteger, ...</li>
     * <li>only columns specified by addScalar are returned. Even though there
     * are more columns in "select part"</li>
     * </ul>
     * TODO is there any alternative in JPA?
     */
    @Test
    public void testNativeQueryWithAddScalar() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            entityManager.persist(new Employee(10L, 20L));
            return null;
        });
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Query q1 = entityManager.createNativeQuery("SELECT val_big_int FROM employee");
            NativeQuery nativeQuery = q1.unwrap(NativeQuery.class);
            nativeQuery.addScalar("val_big_int", StandardBasicTypes.INTEGER);
            Object result = q1.getResultList().get(0);
            Assert.assertEquals(Integer.class, result.getClass());
            return null;
        });
    }

}
