package cz.kul.snippets.guava.example04_sets;

import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class SetsExample
{


    @Test
    public void testDifference() {

        Set<Integer> a = Set.of(1, 2, 3);
        Set<Integer> b = Set.of(2, 3, 4);

        Assert.assertEquals(Set.of(1), Sets.difference(a, b));
        Assert.assertEquals(Set.of(4), Sets.difference(b, a));
    }

}
