package cz.kul.snippets.cglib.example01_createProxy;

import static org.junit.Assert.assertEquals;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.junit.Test;

/**
 * CGlib is a library for dynamic proxy creation.
 * 
 * It can dynamically create proxy class to another class or interface on runtime. It is very
 * similar to JDK Proxy, but it can also proxy class, not only interface like JDK Proxy.
 */
public class TestCreateProxy {
    
    @Test
    public void createProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(MyBean.class);
        enhancer.setCallback((MethodInterceptor) (o, method, args, methodProxy) ->  {
            Object result = methodProxy.invokeSuper(o, args);
            if (result instanceof String) {
                result = "PROXY_" + result;
            }
            return result;
        });
        MyBean bean = (MyBean) enhancer.create();
        bean.setVal1("ahoj");
        assertEquals("PROXY_ahoj", bean.getVal1());
    }
    
}

class MyBean {
    String val1;

    public String getVal1() {
        return val1;
    }

    public void setVal1(String val1) {
        this.val1 = val1;
    }

}
