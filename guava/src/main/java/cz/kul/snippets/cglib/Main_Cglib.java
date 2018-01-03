package cz.kul.snippets.cglib;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * CGlib is a library for dynamic proxy creation.
 * 
 * It can dynamically create proxy class to another class or interface on runtime. It is very
 * similar to JDK Proxy, but it can also proxy class, not only interface like JDK Proxy.
 * 
 * @author kulhalad
 * @since 7.4.2
 */
public class Main_Cglib {
    
    public static void main(String[] args) {
        createProxy();
    }

    private static void createProxy() {
        // It create proxy in runtime based on a bean and add interceptor
        // to method calling. The interceptor can do anything - load data,
        // log, check permissions, ...

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(MyBean.class);
        enhancer.setCallback(new MyInterceptor());
        MyBean bean = (MyBean) enhancer.create();
        bean.setVal1("ahoj");
        assertEquals("PROXY!_ahoj", bean.getVal1());
    }
}

class MyBean {
    String val1;
    String val2;

    public String getVal1() {
        return val1;
    }

    public void setVal1(String val1) {
        this.val1 = val1;
    }

    public String getVal2() {
        return val2;
    }

    public void setVal2(String val2) {
        this.val2 = val2;
    }

}

class MyInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object result = proxy.invokeSuper(obj, args);
        if (result instanceof String) {
            result = "PROXY!_" + result;
        }
        return result;
    }

}
