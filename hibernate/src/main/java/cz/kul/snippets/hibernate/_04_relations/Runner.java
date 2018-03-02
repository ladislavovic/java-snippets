/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._04_relations;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author kulhalad
 */
public abstract class Runner {
    
    private Session session;
    
    protected SessionFactory sessionFactory;
    
    public Runner(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public void run() {
        try {
            System.out.println("#### " + this.getClass().getName() + " running");
            session = sessionFactory.openSession();
            session.getTransaction().begin();
            runImpl();
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                Transaction transaction = session.getTransaction();
                if (transaction != null) {
                    transaction.rollback();
                }
                session.close();
                session = null;
            }
        }
    }
    
    protected abstract void runImpl();

    protected Session session() {
        return session;
    }
    
}
