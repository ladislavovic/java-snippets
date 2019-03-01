package cz.kul.snippets.guava.example01_range;

import com.google.common.collect.Range;
import org.junit.Assert;
import org.junit.Test;

/**
 * Ranges represents mathematical intervals:
 *   (1, 5)
 *   <0, 1000)
 *   <0, inf)
 */
public class Ranges 
{
    
    @Test
    public void openedInterval() {
        Range<Integer> r = Range.open(0, 10);
        Assert.assertTrue(r.contains(5));
        Assert.assertFalse(r.contains(0));
        Assert.assertFalse(r.contains(10));
    }
    
    @Test
    public void closedInterval() {
        Range<Integer> r = Range.closed(0, 10);
        Assert.assertTrue(r.contains(5));
        Assert.assertTrue(r.contains(0));
        Assert.assertTrue(r.contains(10));
    }
    
    @Test
    public void intersection() {
        Range<Integer> r1 = Range.open(5, 10);
        Range<Integer> r2 = Range.closed(7, 12);
        Range<Integer> intersection = r1.intersection(r2);
        Assert.assertTrue(intersection.contains(7));
        Assert.assertTrue(intersection.contains(9));
        Assert.assertFalse(intersection.contains(10));
    }
    
}
