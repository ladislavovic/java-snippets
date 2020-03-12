package cz.kul.snippets.jpa.common;

import cz.kul.snippets.SnippetsTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractJPATest extends SnippetsTest {

    private JPAService jpaService;

    public String getJpaModelPackages() {
        return getTestPackageName();
    }

    public Class<?>[] getConfigClasses() {
        return new Class[]{JPAConfig.class};
    }

    public final JPAService jpaService() {
        if (jpaService == null) {
            synchronized (this) {
                if (jpaService == null) {
                    jpaService = freshJpaService();
                }
            }
        }
        return jpaService;
    }

    public final JPAService freshJpaService() {
        Map<String, Object> props = new HashMap<>();
        props.put("entityManagerFactory.packagesToScan", getJpaModelPackages());
        props.put("jdbc.url", getJdbcUrl());

        SpringContextInitializer initializer = new SpringContextInitializer(new String[]{getTestPackageName()}, getConfigClasses(), props);
        return new JPAService(initializer);
    }

    public final String getTestPackageName() {
        String p = this.getClass().getPackage().getName();
        return p;
    }

    public final String getTestClassName() {
        String p = this.getClass().getSimpleName();
        return p;
    }

    public final String getJdbcUrl() {
        String dbName = RandomStringUtils.randomAlphabetic(8);
        return "jdbc:hsqldb:mem:" + dbName;
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
