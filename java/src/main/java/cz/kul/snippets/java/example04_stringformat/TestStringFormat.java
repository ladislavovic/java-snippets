package cz.kul.snippets.java.example04_stringformat;

import org.junit.Assert;
import org.junit.Test;

public class TestStringFormat {

    @Test
    public void testBasics() {
        Assert.assertEquals("an integer: 10", String.format("an integer: %d", 10));
        Assert.assertEquals("an short: 10", String.format("an short: %d", (short)10));
        Assert.assertEquals("a string: foo", String.format("a string: %s", "foo"));
    }

    @Test
    public void testFloating() {
        // TODO
    }

}
