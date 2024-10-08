package cz.kul.snippets.java.collections;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

import static org.junit.Assert.*;

public class TestCollections {

    @Test
    public void treeMap() {
        // - map with defined keys order
        // - implemented by red-black tree
        // - order is defined by Comparator or campareTo method
        // - log(n) speed of getAppender, put and remove (so slower than HashMap)
        NavigableMap<String, String> map = new TreeMap<String, String>();
        map.put("b", "foo");
        map.put("c", "bar");
        map.put("a", "baz");
        assertEquals("a", map.firstKey());
        assertEquals("c", map.lastKey());
    }

    @Test
    public void testTreeSetWhenEqualsAndComparableAreNotConsistent() {
        // It is higly recommended to have comparation consistent with equals (see Comparable interface)
        // Otherwise you have following behaviour with TreeSet

        // Two BigDecimal instaces are equal when they have the same value and the same number of decimals.
        // But they are compared only by value. So
        BigDecimal num1 = new BigDecimal("2.0");
        BigDecimal num2 = new BigDecimal("2.00");
        assertNotEquals(num1, num2);
        assertEquals(0, num1.compareTo(num2));

        // an example of weird behaviour when compareTo is not consistent with equals
        Set<BigDecimal> treeSet = new TreeSet<BigDecimal>();
        treeSet.add(num1);
        treeSet.add(num2); // not equals to "2.00" but not inserted! Because it compares to "2.0"

        assertEquals(1, treeSet.size());
        assertEquals(num1, treeSet.iterator().next());
    }

    @Test
    public void linkedHashMap() {
        // - it is like HashMap but it is ordered. The order is according to order of
        //   insertions into the map (insertion-order). Internally it uses linded list
        //   for that.
        // - const speed. Almost as quick as HashMap, only a little bit slower.
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("b", "foo");
        map.put("c", "bar");
        map.put("a", "baz");
        Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
        assertEquals("foo", iterator.next().getValue());
        assertEquals("bar", iterator.next().getValue());
        assertEquals("baz", iterator.next().getValue());

        // - reinsertion does not change the order
        map.put("b", "foo2"); // reinsertion
        assertEquals("foo2", map.entrySet().iterator().next().getValue());
    }

    @Test
    public void identityHashMap() {
        // * it uses reference equality (==) on key search operations. So it violates the Map interface contract.
        // * it calls System.identityHashCode() for hash code, so it completely ignore key's hashCode() method
        // * key and value can be null
        // * use cases:
        //    * map with metadata

        var obj1 = new Object();
        var obj2 = new Object();
        var metadataMap = new IdentityHashMap<Object, List<String>>();
        metadataMap.put(obj1, List.of("value1", "value2"));
        metadataMap.put(obj2, List.of("value1", "value2", "value3"));
        assertEquals(List.of("value1", "value2"), metadataMap.get(obj1));
    }

    @Test
    public void testNullInList() {
        List<Integer> list = new ArrayList<>();
        list.add(null);
        list.add(null);
        list.add(null);
        assertEquals(3, list.size());
    }

    @Test
    public void testNullInSet() {
        Set<Integer> set = new HashSet<>();
        set.add(null);
        set.add(null);
        set.add(null);
        assertEquals(1, set.size());
        assertTrue(set.contains(null));
    }

    @Test
    public void nullInMap() {
        Map<String, String> map = new HashMap<>();
        map.put("keyToNullValue", null);
        map.put(null, "valueWithNullKey");
        assertEquals(2, map.size());
        assertEquals(null, map.get("keyToNullValue"));
        assertEquals("valueWithNullKey", map.get(null));
    }
    
    @Test
    public void arrayListEqualsAndHashCode() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);
        Assert.assertTrue(list1.equals(list2));
        Assert.assertTrue(list1.hashCode() == list2.hashCode());
    }

}
