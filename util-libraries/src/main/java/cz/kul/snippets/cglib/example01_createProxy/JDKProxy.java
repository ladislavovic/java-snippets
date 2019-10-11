package cz.kul.snippets.cglib.example01_createProxy;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKProxy {
    
    @Test
    public void createSimpleProxy() {
        Bean proxy = (Bean) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[]{Bean.class},
                new ReturnsNullAlways());
        Assert.assertNull(proxy.getName());
    }
    
    @Test
    public void itCreatesDynamicClass() {
        Bean proxy = (Bean) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[]{Bean.class},
                new ReturnsNullAlways());
        Assert.assertTrue(proxy.getClass().getSimpleName().matches("\\$Proxy.+"));
    }
    
}

class ReturnsNullAlways implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}

interface Bean {
    String getName();
}

class BeanImpl implements Bean {
    public String getName() {
        return "jane";
    }
}
