package cz.kul.snippets.mockito.example05_arguments;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Objects;

/**
 * How to specify return value for particular argument.
 */
public class Stubbing {

    @Test
    public void returnDifferentValueForTheGivenArgument() {
        Person m = Mockito.mock(Person.class);

        // NOTE: you must not swith theese two lines. Otherwise it returns "nowhere" always.
        doReturn("nowhere").when(m).getVacationLocation(any());
        doReturn("Greece").when(m).getVacationLocation(2020);

        Assert.assertEquals("nowhere", m.getVacationLocation(2019));
        Assert.assertEquals("Greece", m.getVacationLocation(2020));
    }


    @Test
    public void returnValueAccordingToAngument() {
        // NOTE: Here the mockito uses .equals() method to determine if arguments are quals or not
        Person m = Mockito.mock(Person.class);
        doReturn(true).when(m).isTheSameName("Jane");
        Assert.assertTrue(m.isTheSameName("Jane"));
        Assert.assertFalse(m.isTheSameName("Monica"));
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

        public String getVacationLocation(Integer year) {
            return null;
        }

    }

}
