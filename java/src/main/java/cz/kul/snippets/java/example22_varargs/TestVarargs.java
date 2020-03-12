package cz.kul.snippets.java.example22_varargs;

import org.junit.Assert;
import org.junit.Test;

public class TestVarargs {
    
    @Test
    public void testNullAndEmptyArray() {
        // just return what expected, there is no pitfall there
        Assert.assertArrayEquals(new String [] {"foo", "bar"}, varargsMethod("foo", "bar"));
        
        // when you give no parameter the empty array is returned
        Assert.assertArrayEquals(new String[0], varargsMethod());
        
        // when you call it with two null, then it returns array with two null, still no pitfall...
        Assert.assertArrayEquals(new String[] {null, null}, varargsMethod(null, null));
        
        // but when you call it with onew null, it returns null! No an array with null! Be carafeull of that!
        // The issue is that when you use the literal null, Java doesn't know what type it is supposed to be.
        // It could be a null Object, or it could be a null Object array. For a single argument it assumes the latter.
        Assert.assertEquals((Object) null, varargsMethod(null));
        
        // when you retype the null, it behaves as expected
        Assert.assertArrayEquals(new String[] {null}, varargsMethod((String) null));
        
        // or you can write it without Java 5 sugar
        Assert.assertArrayEquals(new String[] {null}, varargsMethod(new String [] {null}));
    }
    
    public String[] varargsMethod(String... strings) {
        return strings;
    }
    
}
