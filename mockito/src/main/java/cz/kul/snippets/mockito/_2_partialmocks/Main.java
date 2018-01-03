package cz.kul.snippets.mockito._2_partialmocks;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Answers;
import org.mockito.Mockito;

/**
 * You can create mock, which call original method, if the method
 * is not specified on mock by "when".
 * 
 * @author Ladislav Kulhanek
 */
public class Main {

    public static void main(String[] args) {
        Person p = mock(Person.class, Answers.CALLS_REAL_METHODS.get());
        when(p.getAge()).thenReturn("25");

        p.setName("Jane"); // original method is called
        System.out.println(p.getName()); // original method is called

        p.setAge("50"); // original method is called
        System.out.println(p.getAge()); // MOCK method is called. It returns "25"
    }

    public static class Person {
        String name;
        String age;

        public Person() {
            System.out.println("Person()");
        }

        public String getName() {
            System.out.println("getName()");
            return name;
        }

        public void setName(String name) {
            System.out.println("setName()");
            this.name = name;
        }

        public String getAge() {
            System.out.println("getAge()");
            return age;
        }

        public void setAge(String age) {
            System.out.println("setAge()");
            this.age = age;
        }
    }
}
