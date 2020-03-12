package cz.kul.snippets.jpa.example01_modifyEntityDuringLoadByEventListener;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Configuration
public class Config {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @PostConstruct
    protected void setEventListeners() {
        SessionFactoryImpl sessionFactory = emf.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.POST_LOAD).appendListener(new PersonNameModifier());
    }

}
