package cz.kul.snippets.mockito.example04_doReturn_VS_when;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * You have two possibilities how to stub method:
 * 1. when(...).thenReturn(...)
 * 2. doReturn(...).when(...)
 * <p>
 * The second one is preferred.
 */
public class Test_doReturn_vs_when {

    @Test
    public void test() {
        Person m = Mockito.mock(Person.class);

        // Prefer this way how to set mock behaviour. It is more powerfull
        doNothing().when(m).init(); // void method mock
        doReturn("Jane").when(m).getName();

        // It is better to avoid that.
        when(m.getName()).thenReturn("Jane");
    }

    public static class Person {
        String name;

        public void init() {
            ;
        }

        public String getName() {
            System.out.println("real getName() called");
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
