package cz.kul.snippets.jpa.common;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.support.AbstractApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.function.Function;

public class JPAService {

    final static Logger log = Logger.getLogger(JPAService.class);

    private AbstractApplicationContext ctx;
    private EntityManagerFactory emf;

    public JPAService(SpringContextInitializer initializer) {
        ctx = initializer.initialize();
        this.emf = (EntityManagerFactory) ctx.getBean("entityManagerFactory");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public <R> R doInTransactionAndFreshEM(Function<EntityManager, R> job) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return doInTransaction(em, job);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public <R> R doInTransaction(EntityManager em, Function<EntityManager, R> job) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        R result = job.apply(em);
        if (tx.getRollbackOnly()) {
            tx.rollback();
        } else {
            tx.commit();
        }
        return result;
    }

    public <R> R doInTransactionAndFreshSession(Function<Session, R> job) {
        SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
        try (Session session = sessionFactory.openSession();) {
            session.beginTransaction();
            R result = job.apply(session);
            session.flush();
            session.getTransaction().commit();
            return result;
        }
    }

    @Override
    protected void finalize() {
        if (ctx != null) {
            ctx.close();
        }
    }

}
