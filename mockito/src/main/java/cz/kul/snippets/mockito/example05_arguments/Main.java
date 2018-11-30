package cz.kul.snippets.mockito.example05_arguments;

import static org.mockito.Mockito.doReturn;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Objects;

/**
 * How to specify return value for particular argument.
 *
 * @author kulhalad
 */
public class Main {

    @Test
    public void returnValueAccordingToAngument() {
        // NOTE: Here the mockito uses .equals() method to determine if arguments are quals or not
        Person m = Mockito.mock(Person.class);
        doReturn(true).when(m).isTheSameName("Jane");
        Assert.assertTrue(m.isTheSameName("Jane"));
        Assert.assertFalse(m.isTheSameName("Monica"));
    }

    @Test
    public void testArgumentMatcher() {
        // TODO
    }

    public static class Person {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isTheSameName(String name) {
            return Objects.equals(this.name, name);
        }

    }

}
