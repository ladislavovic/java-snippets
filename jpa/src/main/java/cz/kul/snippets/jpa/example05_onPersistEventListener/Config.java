package cz.kul.snippets.jpa.example05_onPersistEventListener;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import javax.annotation.PostConstruct;

@Configuration
public class Config {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @PostConstruct
    protected void setEventListeners() {
        SessionFactoryImpl sessionFactory = emf.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.PERSIST).appendListener(new MyPersistEventListener());
    }

}
