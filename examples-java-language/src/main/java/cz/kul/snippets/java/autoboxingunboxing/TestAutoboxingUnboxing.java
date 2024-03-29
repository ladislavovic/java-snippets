package cz.kul.snippets.java.autoboxingunboxing;

import org.junit.Assert;
import org.junit.Test;

public class TestAutoboxingUnboxing {

    private Integer i = new Integer(12345);
    private Integer j = new Integer(12345);

    @Test
    public void test_Integer_Integer() {
        Assert.assertFalse(i == j); // it is not equal !!! There is no Unboxing here.
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
        // you can really compare int and Integer by == operator, you does not have to manually
        // convert Integer to int. Integer is always unboxed to primitive.
        // see https://stackoverflow.com/questions/7672317/integer-int-allowed-in-java
        Assert.assertTrue(i.intValue() == j); // Unbox to primitive
        Assert.assertTrue(i == j.intValue()); // Unbox to primitive
    }

    @Test
    public void test_int_int() {
        Assert.assertTrue(i.intValue() == j.intValue()); // no autoboxing, both are primitives
    }

}
