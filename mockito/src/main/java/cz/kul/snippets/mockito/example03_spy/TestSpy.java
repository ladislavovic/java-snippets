package cz.kul.snippets.mockito.example03_spy;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * You can use Spy to mock existing objects.
 * <p>
 * Be aware spy create copy of existing object so it does not work with
 * original object.
 */
public class TestSpy {

    @Test
    public void test() {
        List<String> list = new ArrayList<>();
        List<String> spy = Mockito.spy(list);

        when(spy.size()).thenReturn(100);
        Assert.assertEquals(0, list.size());
        Assert.assertEquals(100, spy.size());

        spy.add("one");
        spy.add("two");
        Assert.assertEquals(100, spy.size());

    }

}
