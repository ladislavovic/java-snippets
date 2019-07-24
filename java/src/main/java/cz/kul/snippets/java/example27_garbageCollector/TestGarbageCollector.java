package cz.kul.snippets.java.example27_garbageCollector;

import cz.kul.snippets.ThreadUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.ref.WeakReference;

public class TestGarbageCollector {

    @Test
    public void gcCanRemoveGraphOfObjects() {
        WeakReference<A> ref = new WeakReference<>(createA());
        Assert.assertNotNull(ref.get());
        runGC();
        Assert.assertNull(ref.get());
    }

    private A createA() {
        A a = new A();
        B b = new B();
        a.b = b;
        b.a = a;
        return a;
    }

    public static class A {
        B b;
    }

    public static class B {
        A a;
    }

    private void runGC() {
        for (int i = 0; i < 3; i++) {
            System.gc();
            ThreadUtils.sleepOneSecond();
        }
    }
    
}
