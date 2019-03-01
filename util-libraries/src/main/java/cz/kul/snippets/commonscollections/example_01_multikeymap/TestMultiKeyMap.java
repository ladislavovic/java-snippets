package cz.kul.snippets.commonscollections.example_01_multikeymap;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.junit.Assert;
import org.junit.Test;

/**
 * With this class you can map values according to combinations of keys.
 *
 * The limitation is all keys have to have the same type.
 *
 */
public class TestMultiKeyMap {

    @Test
    public void testMultiKey() {
        MultiKeyMap<String, Integer> m = new MultiKeyMap<>();
        m.put("A", "A", 1);
        m.put("A", "A", "A", "A", 2);
        Assert.assertEquals(1, (int) m.get("A", "A"));
        Assert.assertEquals(2, (int) m.get("A", "A", "A", "A"));
    }
    
    @Test
    public void testNullAsKey() {
        MultiKeyMap<String, Integer> m = new MultiKeyMap<>();
        m.put(null, "A", 1);
        Assert.assertEquals(1, (int) m.get(null, "A"));
    }
    
}
