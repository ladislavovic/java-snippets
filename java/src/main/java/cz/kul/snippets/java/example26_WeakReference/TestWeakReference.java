package cz.kul.snippets.java.example26_WeakReference;

import cz.kul.snippets.ThreadUtils;
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
    public void weakHashMap_anEntryIsRemovedWhenTheKeyIsRemovedByGC() {
        /*
          From Javadoc:
          Hash table based implementation of the Map interface, with
          weak keys.
          An entry in a WeakHashMap will automatically be removed when
          its key is no longer in ordinary use.
        */

        Map<Integer, String> map = new WeakHashMap<>();
        map.put(new Integer(1), "one");
        Integer keyTwo = new Integer(2);
        map.put(keyTwo, "two");
        map.put(new Integer(3), "three");
        
        Assert.assertEquals(3, map.size());
        runGC();
        Assert.assertEquals(1, map.size());
        Assert.assertEquals("two", map.get(keyTwo));
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

        int numberOfIterations = 0;
        Iterator<Integer> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            runGC();
            iter.next();
            numberOfIterations++;
        }
        Assert.assertEquals(1, numberOfIterations);
    }
    
    private void runGC() {
        for (int i = 0; i < 3; i++) {
            System.gc();
            ThreadUtils.sleepOneSecond();
        }
    }
    
}
