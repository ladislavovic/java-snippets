package cz.kul.snippets.hibernatesearch6.example01_annotations;

import cz.kul.snippets.hibernatesearch6.commons.HibernateSearchTest;
import org.apache.lucene.search.Query;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestIndexByAnnotations extends HibernateSearchTest {

    public static String TMP_DIR = "/var/hibernate-search-snippets/example1/lucene/indexes";

    @Override
    public String getTmpDir() {
        return TMP_DIR;
    }

    @Test
    public void fuultextSearch() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            Person p1 = new Person("Jana", "Novakova");

            PersonDetail d1 = new PersonDetail();
            d1.setValue("ucetni");
            d1.setPerson(p1);
            p1.getDetails().add(d1);

            Person p2 = new Person("Petra", "Zavodna");

            entityManager.persist(p1);
            entityManager.persist(p2);
            return null;
        });
        List<Person> people = jpaService().doInTransactionAndFreshSession(session -> {
            FullTextSession fullTextSession = Search.getFullTextSession(session);
            QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Person.class).get();
            Query luceneQuery = queryBuilder
                    .keyword()
                    .onFields("name", "surname", "details.value")
                    .matching("ucetni")
                    .createQuery();
            FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, Person.class);
            List<Person> results = fullTextQuery.list();
            return results;
        });
        Assert.assertEquals(1, people.size());
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
        private Set<PersonDetail> details = new HashSet<>();

        public Person() {
        }

        public Person(String name, String surname) {
            this.name = name;
            this.surname = surname;
        }

        public void addPersonDetail(PersonDetail detail) {
            details.add(detail);
            detail.setPerson(this);
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

        public Set<PersonDetail> getDetails() {
            return details;
        }

        public void setDetails(Set<PersonDetail> details) {
            this.details = details;
        }

        @Override
        public String toString() {
            return "Person [id=" + id + ", name=" + name + ", surname=" + surname + "]";
        }
    }

    @Entity(name = "PersonDetailEntity")
    public static class PersonDetail {

        @Id
        @GeneratedValue
        private Long id;

        @Field
        private String value;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "person_id", nullable = false)
        private Person person;

        public PersonDetail() {
        }

        public PersonDetail(String value) {
            this.value = value;
        }

        public Long getId() {
            return id;
        }
        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "PersonDetail{" +
                    "id=" + id +
                    ", value ='" + value + '\'' +
                    ", person=" + person +
                    '}';
        }
    }

}
