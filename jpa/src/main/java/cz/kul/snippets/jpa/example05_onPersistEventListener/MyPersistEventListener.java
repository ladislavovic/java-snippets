package cz.kul.snippets.jpa.example05_onPersistEventListener;

import cz.kul.snippets.agent.AgentManager;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;

import java.util.HashSet;
import java.util.Map;

public class MyPersistEventListener implements PersistEventListener {

    @Override
    public void onPersist(PersistEvent event) throws HibernateException {
        Object entity = event.getObject();
        AgentManager.executeAgent(TestOnPersistEventListener.ON_PERSIST_EVENT, entity);
    }

    @Override
    public void onPersist(PersistEvent event, Map createdAlready) throws HibernateException {
        Object[] arr = new Object[2];
        arr[0] = event.getObject();
        arr[1] = new HashSet<>(createdAlready.values());
        AgentManager.executeAgent(TestOnPersistEventListener.ON_PERSIST_EVENT_MAP, arr);
    }
}
