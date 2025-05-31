package cz.kul.snippets.jpa.example05_onPersistEventListener;

import cz.kul.snippets.agent.AgentManager;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.PersistContext;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;

public class MyPersistEventListener implements PersistEventListener {

    @Override
    public void onPersist(PersistEvent event) throws HibernateException {
        Object entity = event.getObject();
        AgentManager.executeAgent(TestOnPersistEventListener.ON_PERSIST_EVENT, entity);
    }

    @Override
    public void onPersist(PersistEvent event, PersistContext createdAlready) throws HibernateException {
        AgentManager.executeAgent(TestOnPersistEventListener.ON_PERSIST_EVENT_MAP, event);
    }
}
