package cz.kul.snippets.guava.example02_multimap;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

/**
 * Multimap is a map, which can have more values for one key. Effectively it is
 * more handy version of Map<K, Collection<V>>.
 */
public class TestMultimap {

    @Test
    public void test() {
        Multimap<String, Integer> mm = HashMultimap.create();
        mm.put("a", 1);
        mm.put("a", 2);
        mm.put("a", 3);
        Assert.assertEquals(Sets.newHashSet(1, 2, 3), mm.get("a"));
    }

}
