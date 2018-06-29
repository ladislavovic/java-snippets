package cz.kul.snippets.hibernatesearch.example02_programmatic_approach;

import org.apache.lucene.search.Query;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.util.List;

import static cz.kul.snippets.hibernatesearch.commons.HibernateUtils.doInTransaction;

public class TestProgrammaticApproach {

    private SessionFactory sessionFactory;

    private AbstractApplicationContext ctx;

    @Before
    public void before() {
        clean();
        ctx = new ClassPathXmlApplicationContext("02-spring.xml");
        sessionFactory = ctx.getBean(SessionFactory.class);

        doInTransaction(sessionFactory, session -> {
            session.persist(new Person("Jana", "Novakova"));
            session.persist(new Person("Petra", "Zavodna"));
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
    public void testSearching() {
        List<? extends AbstractPerson> people = doInTransaction(sessionFactory, session -> {
            FullTextSession fullTextSession = Search.getFullTextSession(session);
            QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(AbstractPerson.class).get();
            Query luceneQuery = queryBuilder
                    .keyword()
                    .onFields("wholeName")
                    .matching("Petra")
                    .createQuery();
            FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, AbstractPerson.class);
//            List<AbstractPerson> results = fullTextQuery.getResultList();
            List<AbstractPerson> results = fullTextQuery.list();
            return results;
        });
        Assert.assertEquals(1, people.size());
    }

    private static void clean() {
        FileSystemUtils.deleteRecursively(new File("/var/hibernate-seach-snippets"));
    }

}
