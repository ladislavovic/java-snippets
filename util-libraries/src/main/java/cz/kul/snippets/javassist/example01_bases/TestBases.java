package cz.kul.snippets.javassist.example01_bases;

import static org.junit.Assert.assertEquals;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.junit.Test;

import java.net.URL;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;

public class TestBases {

    @Test
     public void createClassFromScratch() throws CannotCompileException, InstantiationException, IllegalAccessException {
        ClassPool pool = ClassPool.getDefault();
        CtClass fooCtClass = pool.makeClass("ClassFromScratch");

        @SuppressWarnings("rawtypes")
        Class fooClass = fooCtClass.toClass();
        Object newInstance = fooClass.newInstance();
        assertEquals("ClassFromScratch", newInstance.getClass().getName());
    }


    @Test
    public void aaa() throws Exception {

        ClassLoader customClassLoader = new URLClassLoader(new URL[] {}, Thread.currentThread().getContextClassLoader());

        ClassPool pool = ClassPool.getDefault();
        CtClass fooCtClass = pool.makeClass("ClassFromScratch");

        Class fooClass = fooCtClass.toClass(customClassLoader, null);



//        Class<?> aClass = Class.forName(fooClass.getName());


        Class<?> aClass1 = Class.forName(fooClass.getName(), true, customClassLoader);


    }

    @Test
    public void wrapMethod() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass fooClass = pool.get("cz.kul.snippets.javassist.example01_bases.TestBases$Foo");
        CtMethod method = fooClass.getDeclaredMethod("doSomething");
        method.insertBefore("System.out.println(\"Method " + method.getLongName() + " starts\");");
        method.insertAfter("System.out.println(\"Method " + method.getLongName() + " quits with value: \" + $_);");
        Class modified = fooClass.toClass();
        Foo foo = (Foo) modified.newInstance();
        assertEquals(10, foo.doSomething());
    }

    @Test
    public void modifyReturnsValue() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass fooClass = pool.get("cz.kul.snippets.javassist.example01_bases.TestBases$Foo2");
        CtMethod method = fooClass.getDeclaredMethod("doSomething");
        method.insertAfter("$_ = 20;");
        Class modified = fooClass.toClass();
        Foo2 foo = (Foo2) modified.newInstance();
        assertEquals(20, foo.doSomething());
    }

    public static class Foo {

        public int doSomething() {
            int result = 10;
            return result;
        }

    }
    
    public static class Foo2 {

        public int doSomething() {
            int result = 10;
            return result;
        }

    }
}
