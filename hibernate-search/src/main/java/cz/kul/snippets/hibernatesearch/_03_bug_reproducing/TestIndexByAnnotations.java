package cz.kul.snippets.hibernatesearch._03_bug_reproducing;

import org.apache.lucene.search.Query;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.bridge.StringBridge;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.FileSystemUtils;

import javax.persistence.*;
import java.io.File;
import java.util.List;

import static cz.kul.snippets.hibernatesearch.HibernateUtils.doInTransaction;

public class TestIndexByAnnotations {

    private SessionFactory sessionFactory;

    private AbstractApplicationContext ctx;

    @Before
    public void before() {
        clean();
        ctx = new ClassPathXmlApplicationContext("03-spring.xml");
        sessionFactory = ctx.getBean(SessionFactory.class);

        doInTransaction(sessionFactory, session -> {
            AbstractPersonDetail d1 = new StringPersonDetail();
            d1.setStringValue("ucetni");

            AbstractPersonDetail d2 = new IntegerPersonDetail();
            d2.setIntegerValue(10);

            session.persist(d1);
            session.persist(d2);
            return null;
        });
    }

    @After
    public void after() {
        if (ctx != null) {
            ctx.close();
        }
    }

    @Test
    public void fuultextSearch() {
        List<AbstractPersonDetail> people = doInTransaction(sessionFactory, session -> {
            FullTextSession fullTextSession = Search.getFullTextSession(session);
            QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(AbstractPersonDetail.class).get();
            Query luceneQuery = queryBuilder
                    .keyword()
                    .onFields("value")
                    .matching("ucetni")
                    .createQuery();
            FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, AbstractPersonDetail.class);
//            List<AbstractPersonDetail> results = fullTextQuery.getResultList();
            List<AbstractPersonDetail> results = fullTextQuery.list();
            return results;
        });
        Assert.assertEquals(1, people.size());
    }

    private static void clean() {
        FileSystemUtils.deleteRecursively(new File("/var/hibernate-seach-snippets"));
    }

    @Entity(name = "PersonDetailEntity")
    @Inheritance(strategy=InheritanceType.SINGLE_TABLE)
    public static abstract class AbstractPersonDetail<T extends Object> {

        @Id
        @GeneratedValue
        private Long id;

        private String stringValue;

        private Integer integerValue;

        @Field
        @FieldBridge(impl = ToStringBridge.class)
        public abstract T getValue();

        public Object getValue2() {
            return getValue();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }

        public Integer getIntegerValue() {
            return integerValue;
        }

        public void setIntegerValue(Integer integerValue) {
            this.integerValue = integerValue;
        }

        @Override
        public String toString() {
            return "AbstractPersonDetail{" +
                    "id=" + id +
                    ", stringValue='" + stringValue + '\'' +
                    ", integerValue=" + integerValue +
                    '}';
        }
    }

    @Entity
    @DiscriminatorValue("str")
    @Indexed
    public static class StringPersonDetail extends AbstractPersonDetail<String> {

//        @Field
//        @FieldBridge(impl = ToStringBridge.class)
        public String getValue() {
            return super.getStringValue();
        }

    }

    @Entity
    @DiscriminatorValue("int")
    @Indexed
    public static class IntegerPersonDetail extends AbstractPersonDetail<Integer> {

//        @Field
//        @FieldBridge(impl = ToStringBridge.class)
        public Integer getValue() {
            return super.getIntegerValue();
        }

    }

    public static class ToStringBridge implements StringBridge {

        public String objectToString(Object object) {
            return object == null ? "" : object.toString();
        }
    }
}
