package cz.kul.snippets.concurrency.example10_WeakReference;

import cz.kul.snippets.concurrency.commons.ThreadUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class TestWeakReference {
    
    @Test
    public void gcRemovesObjectFromWekReference() {
        WeakReference<Object> ref = new WeakReference<>(new Object());
        Assert.assertNotNull(ref.get());
        
        runGC();
        
        Assert.assertNull(ref.get());
    }
    
    @Test
    public void weakHashMap_variableSize() {
        Map<Integer, String> map = new WeakHashMap<>();
        map.put(new Integer(1), "one");
        map.put(new Integer(2), "two");
        map.put(new Integer(3), "three");
        
        Assert.assertEquals(3, map.size());
        runGC();
        Assert.assertEquals(0, map.size());
    }

    @Test
    public void weakHashMap_iterate() {
        // You can safelly iterate through WeakHashMap, there is no
        // risk of concurrent access.
        // Iterator holds strong reference to next element so it can not be
        // removed by GC.
        
        Map<Integer, String> map = new WeakHashMap<>();
        map.put(new Integer(1), "one");
        map.put(new Integer(2), "two");
        map.put(new Integer(3), "three");

        int iterCount = 0;
        Iterator<Integer> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            runGC();
            iter.next();
            iterCount++;
        }
        Assert.assertEquals(1, iterCount);
    }
    
    private void runGC() {
        for (int i = 0; i < 3; i++) {
            System.gc();
            ThreadUtils.sleepOneSecond();
        }
    }
    
    @Test
    public void test() {
        WeakReference<A> ref = new WeakReference<>(new A(new B()));
        System.out.println(ref.get());
        runGC();
        System.out.println(ref.get());
    }
    
    public static class A {
        B b;

        public A(B b) {
            this.b = b;
        }
    }
    
    public static class B {
        A a;
    }
}
