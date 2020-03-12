package cz.kul.snippets.javassist;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class example02_proxy {

    public static class Foo {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
        public String defaultName() {
            return "Monica";
        }


    }

    public static class MyMethodHandler implements MethodHandler {
        
        private Foo original;

        public MyMethodHandler(Foo original) {
            this.original = original;
        }

        @Override
        public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
            System.out.println("Name: " + thisMethod.getName());

            Object result = thisMethod.invoke(original, args);
            
//            Object result = proceed.invoke(original, args);  // execute the original method.
            if (thisMethod.getName().equals("getName") && result == null) {
                result = original.defaultName();
            }
            return result;
        }
    }
    
    @Test
    public void test() throws  Exception {
        ProxyFactory f = new ProxyFactory();
        f.setSuperclass(Foo.class);
        f.setFilter(m -> {
            // ignore finalize()
            return !m.getName().equals("finalize");
        });
        Class c = f.createClass();

        Foo original = new Foo();
        original.setName("aaa");


        Foo proxy = (Foo) c.newInstance();

        
        ((ProxyObject) proxy).setHandler(new MyMethodHandler(original));
        
        Assert.assertEquals("Monica", proxy.getName());
        
    }
    
    

}
