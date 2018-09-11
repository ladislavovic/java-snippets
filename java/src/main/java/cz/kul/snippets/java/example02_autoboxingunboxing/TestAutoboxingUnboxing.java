package cz.kul.snippets.java.example02_autoboxingunboxing;

import org.junit.Assert;
import org.junit.Test;

public class TestAutoboxingUnboxing {

    private Integer i = new Integer(10);
    private Integer j = new Integer(10);

    @Test
    public void test_Integer_Integer() {
        Assert.assertFalse(i == j); // IT is not equal !!! There is no Unboxing here.
    }

    @Test
    public void test_Integer_Integer_forSmallNumbersIsEqualsDueToJavaOptimization() {
        // small integers are "cached" so the Java use the same instance for a and b
        Integer a = 10;
        Integer b = 10;
        Assert.assertTrue(a == b);
    }

    @Test
    public void test_int_Integer() {
        Assert.assertTrue(i.intValue() == j); // Unbox to primitive
        Assert.assertTrue(i == j.intValue()); // Unbox to primitive
    }

    @Test
    public void test_int_int() {
        Assert.assertTrue(i.intValue() == j.intValue()); // no autoboxing, both are primitives
    }

}
