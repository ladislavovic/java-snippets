package cz.kul.snippets.mockito.example02_partialmocks;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answers;

/**
 * You can create mock, which call original method, if the method
 * is not specified on mock by "when".
 * 
 * @author Ladislav Kulhanek
 */
public class TestPartialmock {

    @Test
    public void test() {
        Person p = mock(Person.class, Answers.CALLS_REAL_METHODS);
        p.setName("Jane"); // original method is called
        Assert.assertEquals("Jane", p.getName());
    }

    public static class Person {
        String name;

        public Person() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
