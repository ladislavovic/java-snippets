package cz.kul.snippets.jpa.example09_envers.case03_missingNotAuditedEntity.hibernatecustomization;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.envers.internal.entities.mapper.relation.lazy.AbstractDelegateSessionImplementor;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.ProxyConfiguration;
import org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor;
import org.hibernate.proxy.pojo.bytebuddy.ByteBuddyProxyFactory;
import org.hibernate.proxy.pojo.bytebuddy.ByteBuddyProxyHelper;
import org.hibernate.type.CompositeType;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Set;

public class EnversEmptyEntityIfNotFoundProxyFactory extends ByteBuddyProxyFactory {

    private Method getIdentifierMethod;

    public EnversEmptyEntityIfNotFoundProxyFactory(ByteBuddyProxyHelper byteBuddyProxyHelper) {
        super(byteBuddyProxyHelper);
    }

    @Override
    public HibernateProxy getProxy(Serializable id, SharedSessionContractImplementor session) throws HibernateException {
        HibernateProxy proxy = super.getProxy(id, session);

        if (proxy.getHibernateLazyInitializer().getSession() instanceof AbstractDelegateSessionImplementor) {
            ByteBuddyInterceptor wrapped = (ByteBuddyInterceptor) proxy.getHibernateLazyInitializer();
            EmptyEntityIfNotFoundLazyInitializer lazyInitializer = new EmptyEntityIfNotFoundLazyInitializer(wrapped, getIdentifierMethod);
            ( (ProxyConfiguration) proxy ).$$_hibernate_set_interceptor( lazyInitializer );
        }

        return proxy;
    }

    @Override
    public void postInstantiate(String entityName, Class persistentClass, Set<Class> interfaces, Method getIdentifierMethod, Method setIdentifierMethod, CompositeType componentIdType) throws HibernateException {
        this.getIdentifierMethod = getIdentifierMethod;
        super.postInstantiate(entityName, persistentClass, interfaces, getIdentifierMethod, setIdentifierMethod, componentIdType);
    }
    
}
