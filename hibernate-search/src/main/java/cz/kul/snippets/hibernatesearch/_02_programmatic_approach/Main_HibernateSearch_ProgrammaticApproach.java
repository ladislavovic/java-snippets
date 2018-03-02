package cz.kul.snippets.hibernatesearch._02_programmatic_approach;

import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.Assert;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.function.Function;

public class Main_MainHw {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-hw.xml");
        SessionFactory sessionFactory = ctx.getBean(SessionFactory.class);

        doInTransaction(sessionFactory, session -> {
            session.persist(new Person("Jana", "Novakova"));
            session.persist(new Person("Petra", "Zavodna"));
            return null;
        });

        List<Person> people = doInTransaction(sessionFactory, session -> {
            FullTextSession fullTextSession = Search.getFullTextSession(session);
            QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Person.class).get();
            Query luceneQuery = queryBuilder
                    .keyword()
                    .onFields("name", "surname")
                    .matching("Petra")
                    .createQuery();
            FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, Person.class);
            List<Person> results = fullTextQuery.list();
            return results;
        });
        Assert.assertEquals(1, people.size());

    }

    private static <R> R doInTransaction(SessionFactory factory, Function<Session, R> job) {
        Session session = factory.openSession();
        session.beginTransaction();
        R result = job.apply(session);
        session.getTransaction().commit();
        session.close();
        return result;
    }

}
