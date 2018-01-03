package cz.kul.snippets.mockito._1_mockcreating;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    public static void main(String[] args) {
        {
            //
            // Mock creating
            //

            // It does not call class constructor. It create it by cglib. So if there is
            // a logic in constructor, it is not called.
            Foo mock = Mockito.mock(Foo.class);
            assertTrue(mock != null);
            assertTrue(mock instanceof Foo);
            assertNotEquals(Foo.class, mock.getClass());
        }

        {
            //
            // Setting of return value 
            //

            Foo mock = Mockito.mock(Foo.class);
            // Not mocked methods returns null when the return type
            // is not primitive and zero whet it is primitive
            assertEquals(null, mock.strMethod());
            assertEquals(0, mock.intMethod());
            assertEquals(false, mock.booleanMethod());

            // Mocking return values
            Mockito.doReturn(50).when(mock).intMethod();
            assertEquals(50, mock.intMethod());

            // You can also mock return value dynamically
            Mockito.doAnswer(new Answer<String>() {
                @Override
                public String answer(InvocationOnMock invocation) throws Throwable {
                    Object[] args = invocation.getArguments();
                    return ((String) args[0]) + ((String) args[0]);
                }
            }).when(mock).methodWithArg(Mockito.anyString());
            assertEquals("foofoo", mock.methodWithArg("foo"));
        }

        {
            //    
            // Setting of exception throwing
            //
            Foo mock = Mockito.mock(Foo.class);
            Mockito.doThrow(IllegalArgumentException.class).when(mock).strMethod();
            try {
                mock.strMethod();
                fail("It should not get here");
            } catch (IllegalArgumentException e) {
            }
        }

        {
            //
            // Capturing of methods parameters
            //
            // It is done by ArgumentCaptor. Verify method must be called after mock method calling.
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

        {
            //
            // Veryfying numbers of method calling
            //
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

    }

}
