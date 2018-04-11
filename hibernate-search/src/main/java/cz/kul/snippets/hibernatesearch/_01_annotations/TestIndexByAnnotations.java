package cz.kul.snippets.hibernatesearch._01_annotations;

import org.apache.lucene.search.Query;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cz.kul.snippets.hibernatesearch.HibernateUtils.doInTransaction;

public class TestIndexByAnnotations {

    private SessionFactory sessionFactory;

    private AbstractApplicationContext ctx;

    @Before
    public void before() {
        clean();
        ctx = new ClassPathXmlApplicationContext("01-spring.xml");
        sessionFactory = ctx.getBean(SessionFactory.class);

        doInTransaction(sessionFactory, session -> {
            Person p1 = new Person("Jana", "Novakova");

            AbstractPersonDetail d1 = new StringPersonDetail();
            d1.setStringValue("ucetni");
            d1.setPerson(p1);

            AbstractPersonDetail d2 = new IntegerPersonDetail();
            d2.setIntegerValue(10);
            d2.setPerson(p1);

            p1.getDetails().add(d1);
            p1.getDetails().add(d2);

            Person p2 = new Person("Petra", "Zavodna");

            session.persist(p1);
            session.persist(p2);
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
        List<Person> people = doInTransaction(sessionFactory, session -> {
            FullTextSession fullTextSession = Search.getFullTextSession(session);
            QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Person.class).get();
            Query luceneQuery = queryBuilder
                    .keyword()
                    .onFields("name", "surname", "details.value2")
                    .matching("ucetni")
                    .createQuery();
            FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, Person.class);
            List<Person> results = fullTextQuery.list();
            return results;
        });
        Assert.assertEquals(1, people.size());
    }

    private static void clean() {
        FileSystemUtils.deleteRecursively(new File("/var/hibernate-seach-snippets"));
    }

    @Entity(name = "PersonEntity")
    @Indexed
    public static class Person {

        @Id
        @GeneratedValue
        private Long id;

        @Field
        private String name;

        @Field
        private String surname;

        private String sex;

        @OneToMany(fetch = FetchType.LAZY, mappedBy="person", cascade = {CascadeType.ALL})
        @IndexedEmbedded()
        private Set<AbstractPersonDetail<? extends Object>> details = new HashSet<>();

        public Person() {
        }

        public Person(String name, String surname) {
            this.name = name;
            this.surname = surname;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public Set<AbstractPersonDetail<?>> getDetails() {
            return details;
        }

        public void setDetails(Set<AbstractPersonDetail<?>> details) {
            this.details = details;
        }

        @Override
        public String toString() {
            return "Person [id=" + id + ", name=" + name + ", surname=" + surname + "]";
        }
    }

    @Entity(name = "PersonDetailEntity")
    @Inheritance(strategy=InheritanceType.SINGLE_TABLE)
    public static abstract class AbstractPersonDetail<T extends Object> {

        @Id
        @GeneratedValue
        private Long id;

        private String stringValue;

        private Integer integerValue;

        @ManyToOne
        @JoinColumn(name = "person_id", nullable = false)
        private Person person;

//        @Field
//        @FieldBridge(impl = ToStringBridge.class)
        public abstract T getValue();

        @Field
        @FieldBridge(impl = ToStringBridge.class)
        public Object getValue2() {
            return getValue();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
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
                    ", person=" + person +
                    '}';
        }
    }

    @Entity
    @DiscriminatorValue("str")
    public static class StringPersonDetail extends AbstractPersonDetail<String> {

        @Override
        public String getValue() {
            return super.getStringValue();
        }

    }

    @Entity
    @DiscriminatorValue("int")
    public static class IntegerPersonDetail extends AbstractPersonDetail<Integer> {

        @Override
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
