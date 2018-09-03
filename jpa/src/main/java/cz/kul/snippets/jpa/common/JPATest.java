package cz.kul.snippets.jpa.common;

import cz.kul.snippets.SnippetsTest;
import cz.kul.snippets.jpa.common.model.Person;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashMap;
import java.util.Map;

public abstract class JPATest extends SnippetsTest {

    private JPAService jpaService;

    public JPAService jpaService() {
        if (jpaService == null) {
            synchronized (this) {
                if (jpaService == null) {
                    jpaService = freshJpaService();
                }
            }
        }
        return jpaService;
    }

    public JPAService freshJpaService() {
        String commonJPAModel = Person.class.getPackage().getName();
        String testJPAModel = getPackageName();
        String JPAModel = commonJPAModel + "," + testJPAModel;

        Map<String, Object> props = new HashMap<>();
        props.put("entityManagerFactory.packagesToScan", JPAModel);

        SpringContextInitializer initializer = new SpringContextInitializer(new String[]{getPackageName()}, new Class[]{JPAConfig.class}, props);
        return new JPAService(initializer);
    }

    public String getPackageName() {
        String p = this.getClass().getPackage().getName();
        return p;
    }

    public static final void assertUninitializedHibernateProxy(Object instance) {
        if (!isUnitializedHibernateProxy(instance)) {
            throw new AssertionError("Instance is not an Uninitialized Hibernate Proxy");
        }
    }

    public static final void assertInitialized(Object instance) {
        if (isUnitializedHibernateProxy(instance)) {
            throw new AssertionError("Instance is an Uninitialized Hibernate Proxy");
        }
    }

    public static final boolean isUnitializedHibernateProxy(Object instance) {
        if (instance == null) return false;
        if (!(instance instanceof HibernateProxy)) return false;
        return ((HibernateProxy) instance).getHibernateLazyInitializer().isUninitialized();
    }

}
