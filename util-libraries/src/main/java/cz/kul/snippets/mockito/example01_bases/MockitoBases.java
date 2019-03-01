package cz.kul.snippets.mockito.example01_bases;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * How to create basic mock.
 * 
 * @author Ladislav Kulhanek
 *
 */
public class MockitoBases {

    @Test
    public void mockCreating() {
        Foo mock = Mockito.mock(Foo.class);
        assertTrue(mock != null);
        assertTrue(mock instanceof Foo);
        assertNotEquals(Foo.class, mock.getClass());
    }

    @Test
    public void constructorIsNotCalled() {
        // It does not call class constructor. It create it by cglib. So if there is
        // a logic in constructor, it is not called.

        Foo.setProperty(false);
        assertFalse(Foo.getProperty());
        new Foo();
        assertTrue(Foo.getProperty());

        Foo.setProperty(false);
        Mockito.mock(Foo.class);
        assertFalse(Foo.getProperty());
    }

    @Test
    public void staticMockOfReturnValue() {
        Foo mock = Mockito.mock(Foo.class);
        doReturn(50).when(mock).intMethod();
        assertEquals(50, mock.intMethod());
    }

    @Test
    public void dynamicMockOfReturnValue() {
        Foo mock = Mockito.mock(Foo.class);
        Mockito.doAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return ((String) args[0]) + ((String) args[0]);
            }
        }).when(mock).methodWithArg(Mockito.anyString());
        assertEquals("foofoo", mock.methodWithArg("foo"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void mockOfExceptionThrowing() {
        Foo mock = Mockito.mock(Foo.class);
        Mockito.doThrow(IllegalArgumentException.class).when(mock).strMethod();
        mock.strMethod();
    }

    @Test()
    public void capturing() {
        Foo mock = Mockito.mock(Foo.class);
        mock.methodWithArg("foo");
        mock.methodWithArg("bar");
        mock.methodWithArg("baz");

        ArgumentCaptor<String> strCaptor = ArgumentCaptor.forClass(String.class);
        verify(mock, atLeastOnce()).methodWithArg(strCaptor.capture());

        assertEquals("foo", strCaptor.getAllValues().get(0));
        assertEquals("bar", strCaptor.getAllValues().get(1));
        assertEquals("baz", strCaptor.getAllValues().get(2));
    }

    @Test()
    public void veryfying() {
        Foo mock = Mockito.mock(Foo.class);
        mock.intMethod();
        verify(mock).intMethod(); // It means it was called exactly one

        mock.methodWithArg("foo");
        mock.methodWithArg("bar");
        verify(mock, times(2)).methodWithArg(Mockito.any(String.class)); // It means it was called exactly two times

        verify(mock, atLeastOnce()).methodWithArg(Mockito.any(String.class));
        verify(mock, never()).booleanMethod();
        verify(mock, atMost(10)).methodWithArg(Mockito.any(String.class));
        verify(mock, Mockito.atMost(10)).methodWithArg(Mockito.any(String.class));
    }

    public static class Foo {

        private static final String PROP = Foo.class.getName();

        public Foo() {
            setProperty(true);
        }

        public static void setProperty(boolean value) {
            System.setProperty(PROP, Boolean.toString(value));
        }

        public static boolean getProperty() {
            return Boolean.parseBoolean(System.getProperty(PROP));
        }

        public String strMethod() {
            return "STR";
        }

        public String methodWithArg(String str) {
            return str;
        }

        public int intMethod() {
            return 10;
        }

        public boolean booleanMethod() {
            return true;
        }

    }

}
