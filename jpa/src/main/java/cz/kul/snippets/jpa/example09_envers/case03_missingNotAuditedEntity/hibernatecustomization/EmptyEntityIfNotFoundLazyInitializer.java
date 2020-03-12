package cz.kul.snippets.jpa.example09_envers.case03_missingNotAuditedEntity.hibernatecustomization;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.proxy.LazyInitializer;
import org.hibernate.proxy.ProxyConfiguration;
import org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.lang.reflect.Method;

public class EmptyEntityIfNotFoundLazyInitializer implements LazyInitializer, ProxyConfiguration.Interceptor {
    
    private ByteBuddyInterceptor wrapped;

    private Method getIdentifierMethod;
    
    public EmptyEntityIfNotFoundLazyInitializer(ByteBuddyInterceptor wrapped, Method getIdentifierMethod) {
        
        this.wrapped = wrapped;
        this.getIdentifierMethod = getIdentifierMethod;
        
    }

    @Override
    public void initialize() throws HibernateException {
        try {
            wrapped.initialize();
        } catch (EntityNotFoundException e) {
            handleEntityNotFound();   
        }
    }
    
    private void handleEntityNotFound() {
        try {
            Object emptyImplementation = wrapped.getPersistentClass().newInstance();
            
            wrapped.setImplementation(emptyImplementation);
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override
    public Serializable getIdentifier() {
        return wrapped.getIdentifier();
    }

    @Override
    public void setIdentifier(Serializable id) {
        wrapped.setIdentifier(id);
    }

    @Override
    public String getEntityName() {
        return wrapped.getEntityName();
    }

    @Override
    public Class getPersistentClass() {
        return wrapped.getPersistentClass();
    }

    @Override
    public boolean isUninitialized() {
        return wrapped.isUninitialized();
    }

    @Override
    public Object getImplementation() {
        return wrapped.getImplementation();
    }

    @Override
    public Object getImplementation(SharedSessionContractImplementor session) throws HibernateException {
        return wrapped.getImplementation();
    }

    @Override
    public void setImplementation(Object target) {
        wrapped.setImplementation(target);
    }

    @Override
    public boolean isReadOnlySettingAvailable() {
        return wrapped.isReadOnlySettingAvailable();
    }

    @Override
    public boolean isReadOnly() {
        return wrapped.isReadOnly();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        wrapped.setReadOnly(readOnly);
    }

    @Override
    public SharedSessionContractImplementor getSession() {
        return wrapped.getSession();
    }

    @Override
    public void setSession(SharedSessionContractImplementor session) throws HibernateException {
        wrapped.setSession(session);
    }

    @Override
    public void unsetSession() {
        wrapped.unsetSession();
    }

    @Override
    public void setUnwrap(boolean unwrap) {
        wrapped.setUnwrap(unwrap);
    }

    @Override
    public boolean isUnwrap() {
        return wrapped.isUnwrap();
    }

    @Override
    public Object intercept(Object instance, Method method, Object[] arguments) throws Throwable {
        if (method.getName().equals("getHibernateLazyInitializer")) {
            return this;
        }
        
        if (method.equals(getIdentifierMethod)) {
            return wrapped.getIdentifier();
        }
        
        try {
            return wrapped.intercept(instance, method, arguments);
        } catch (EntityNotFoundException e) {
            handleEntityNotFound();
            return wrapped.intercept(instance, method, arguments);
        }
    }
}
