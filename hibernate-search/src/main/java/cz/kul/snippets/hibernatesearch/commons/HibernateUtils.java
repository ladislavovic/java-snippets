package cz.kul.snippets.hibernatesearch.commons;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.util.FileSystemUtils;

import java.util.function.Function;

public class HibernateUtils {

    public static <R> R doInTransaction(SessionFactory factory, Function<Session, R> job) {
        Session session = factory.openSession();
        session.beginTransaction();
        R result = job.apply(session);
        session.getTransaction().commit();
        session.close();
        return result;
    }

}
