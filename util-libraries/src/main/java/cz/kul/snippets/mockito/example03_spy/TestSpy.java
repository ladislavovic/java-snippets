package cz.kul.snippets.mockito.example03_spy;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

/**
 * You can use Spy to mock existing objects.
 * <p>
 * Be aware spy create copy of existing object so it does not work with
 * original object.
 */
public class TestSpy {

    @Test
    public void test() {
        // Original object
        List<String> list = new ArrayList<>();

        // Spy
        List<String> spy = Mockito.spy(list);
        when(spy.size()).thenReturn(100);
        Assert.assertNotSame("Spy does not modify the original object, it create a new one", list, spy);

        // Test size()
        Assert.assertEquals(0, list.size());
        Assert.assertEquals(100, spy.size());

        // Add items and test size again
        spy.add("one");
        spy.add("two");
        Assert.assertEquals(2, list.size());
        Assert.assertEquals(100, spy.size());

    }

}
