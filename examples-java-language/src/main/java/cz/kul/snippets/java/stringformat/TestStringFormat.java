package cz.kul.snippets.java.stringformat;

import org.junit.Assert;
import org.junit.Test;

public class TestStringFormat {

    @Test
    public void test() {
        // the format has the following syntax: 
        // %[argument_index$][flags][width][.precision]conversion
        
        Assert.assertEquals("b", String.format("%2$s", "a", "b"));
        Assert.assertEquals("  b", String.format("%2$3s", "a", "b"));
        Assert.assertEquals("b  ", String.format("%2$-3s", "a", "b")); // flag '-' beans left justified
        
        Assert.assertEquals("10", String.format("%d", 10));
        Assert.assertEquals("10.000", String.format("%.3f", 10.0));
        Assert.assertEquals("  10.000", String.format("%8.3f", 10.0));
        Assert.assertEquals("0010.000", String.format("%08.3f", 10.0));
    }

}
