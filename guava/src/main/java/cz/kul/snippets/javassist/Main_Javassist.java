package cz.kul.snippets.javassist;

import static org.junit.Assert.assertEquals;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class Main_Javassist {

    public static void main(String[] args) throws Exception {
        createClassFromScratch();
        wrapMethod();
        modifyReturnsValue();
    }

    private static void createClassFromScratch() throws CannotCompileException, InstantiationException, IllegalAccessException {
        ClassPool pool = ClassPool.getDefault();
        CtClass fooCtClass = pool.makeClass("ClassFromScratch");

        @SuppressWarnings("rawtypes")
        Class fooClass = fooCtClass.toClass();
        Object newInstance = fooClass.newInstance();
        assertEquals("ClassFromScratch", newInstance.getClass().getName());
    }

    private static void wrapMethod() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass fooClass = pool.get("cz.kul.snippets.javassist.Foo");
        CtMethod method = fooClass.getDeclaredMethod("doSomething");
        method.insertBefore("System.out.println(\"Method " + method.getLongName() + " starts\");");
        method.insertAfter("System.out.println(\"Method " + method.getLongName() + " quits with value: \" + $_);");
        Class modified = fooClass.toClass();
        Foo foo = (Foo) modified.newInstance();
        foo.doSomething();
        assertEquals(10, foo.doSomething());
    }

    private static void modifyReturnsValue() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass fooClass = pool.get("cz.kul.snippets.javassist.Foo2");
        CtMethod method = fooClass.getDeclaredMethod("doSomething");
        method.insertAfter("$_ = 20;");
        Class modified = fooClass.toClass();
        Foo2 foo2 = (Foo2) modified.newInstance();
        foo2.doSomething();
        assertEquals(20, foo2.doSomething());
    }
}
