package cz.kul.snippets.jpa.example05_onPersistEventListener;

import cz.kul.snippets.agent.AgentManager;
import cz.kul.snippets.jpa.common.JPATest;
import cz.kul.snippets.jpa.example05_onPersistEventListener.model.Item;
import cz.kul.snippets.jpa.example05_onPersistEventListener.model.Order;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

public class TestOnPersistEventListener extends JPATest {

    public static final String ON_PERSIST_EVENT = "onPersist(PersistEvent)";
    public static final String ON_PERSIST_EVENT_MAP = "onPersist(PersistEvent, Map)";

    @Test
    public void testShouldCallMethodWithoutCacheMapWhenThereIsNoCascading() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            AgentManager.addAgent(ON_PERSIST_EVENT, x -> x);
            AgentManager.addAgent(ON_PERSIST_EVENT_MAP, x -> x);

            Order order = new Order("Order");
            entityManager.persist(order);

            assertEquals(1, AgentManager.getAgentLog(ON_PERSIST_EVENT).getCallCount());
            assertEquals(order, AgentManager.getAgentLog(ON_PERSIST_EVENT).getFirstResult());
            assertEquals(0, AgentManager.getAgentLog(ON_PERSIST_EVENT_MAP).getCallCount());
            return null;
        });
    }

    /**
     * When you perist entity with cascading it starts to be tricky.
     *
     * First the listener is called for entity persist due to cascade. In the "createdAlready"
     * map is the entity which has the cascade mapping.
     *
     * Then the listener is called for entity witch has the cascade mapping.
     */
    @Test
    @Ignore
    public void testListenerForEntitiesStoredDueToCascadeIsCalledFirst() {
        jpaService().doInTransactionAndFreshEM(entityManager -> {
            AgentManager.addAgent(ON_PERSIST_EVENT, x -> x);
            AgentManager.addAgent(ON_PERSIST_EVENT_MAP, x -> x);

            Order order = new Order("Order");
            Item item = new Item("Item");
            order.addItem(item);
            entityManager.persist(order);

            // calling of onPersist(PersistEvent, Map) during Item persisting
            assertEquals(0, AgentManager.getAgentLog(ON_PERSIST_EVENT_MAP).getFirstCallIndex());
            HashSet<Object> mapValues = new HashSet<>(Arrays.asList(item, order));
            assertArrayEquals(new Object[] {item, mapValues}, (Object[]) AgentManager.getAgentLog(ON_PERSIST_EVENT_MAP).getFirstResult());

            // calling of onPersist(PersistEvent) during Order persisting
            assertEquals(1, AgentManager.getAgentLog(ON_PERSIST_EVENT).getFirstCallIndex());
            assertEquals(order, AgentManager.getAgentLog(ON_PERSIST_EVENT).getFirstResult());

            return null;
        });
    }

}
