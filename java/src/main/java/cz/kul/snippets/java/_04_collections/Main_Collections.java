package cz.kul.snippets.java._04_collections;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Null can be in Java Collection like any other object.
 * 
 * @author kulhalad
 * @since 7.4
 */
public class Main_Collections {
    
    public static void main(String[] args) {
        nullInList();
        nullInSet();
        nullInMap();

        treeMap();
        linkedHashMap();
        treeSet();
    }

    private static void treeSet() {
        // - orderred set ordered by natural ordering or comparator 
        // - based on TreeMap
        // - higly recommended to have comparation consistent with equals (see Comparable interface)

        // an example of weird behaviour when compareTo is not consistent with equals
        Set<BigDecimal> treeSet = new TreeSet<BigDecimal>();
        treeSet.add(new BigDecimal("2.0"));
        treeSet.add(new BigDecimal("2.00")); // not equals to "2.00" but not inserted! Because it compares to "2.0"

        assertEquals(1, treeSet.size());
        assertEquals(new BigDecimal("2.0"), treeSet.iterator().next());
    }

    private static void linkedHashMap() {
        // - it is like HashMap but it is ordered. The order is according to order of
        //   insertions into the map (insertion-order). Internally it uses linded list
        //   for that.
        // - const speed. Almost as quick as HashMap, only a little bit slower.
        // - reinsertion does not change the order
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("b", "foo");
        map.put("c", "bar");
        map.put("a", "baz");
        map.put("b", "foo2"); // reinsertion
        Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
        assertEquals("foo2", iterator.next().getValue()); // from reinsertion 
        assertEquals("bar", iterator.next().getValue());
        assertEquals("baz", iterator.next().getValue());
    }

    private static void treeMap() {
        // - map with defined keys order
        // - implemented by red-black tree
        // - order is defined by Comparator or campareTo method
        // - log(n) speed of get, put and remove (so slower than HashMap)
        NavigableMap<String, String> map = new TreeMap<String, String>();
        map.put("b", "foo");
        map.put("c", "bar");
        map.put("a", "baz");
        assertEquals("a", map.firstKey());
        assertEquals("c", map.lastKey());
    }

    public static void nullInList() {
        System.out.println("\nNull in List:");
        List<Integer> a = new ArrayList<>();
        a.add(null);
        a.add(null);
        a.add(null);
        System.out.println("List: " + a);
        System.out.println("List contains null: " + a.contains(null));
    }
    
    public static void nullInSet() {
        System.out.println("\nNull in Set:");
        Set<Integer> a = new HashSet<>();
        a.add(null);
        a.add(null);
        a.add(null);
        System.out.println("Set: " + a);
        System.out.println("Set contains null: " + a.contains(null));
    }
    
    public static void nullInMap() {
        System.out.println("\nNull in Map:");
        Map<String, Integer> a = new HashMap<>();
        a.put("foo", null);
        a.put(null, 10);
        System.out.println("Map: " + a);
        System.out.println("Map contains 'null' key: " + a.containsKey(null));
        System.out.println("Value for 'null' key: " + a.get(null));
        System.out.println("Value for 'foo' key: " + a.get("foo") + " (that means when 'get()' method returns null the key/value pair still can be present)");
    }
    

}
