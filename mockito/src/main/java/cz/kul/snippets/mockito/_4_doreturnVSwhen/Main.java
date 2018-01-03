package cz.kul.snippets.mockito._4_doreturnVSwhen;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;

/**
 * You have two possibilities how to stub method:
 *     1. when(...).thenReturn(...)
 *     2. doReturn(...).when(...)
 *     
 * What is differrences and wchich version to use?
 * 
 * @author kulhalad
 * @since 7.4
 *
 */
public class Main {

    public static void main(String[] args) {
        
        {
            // It is better to avoid that.
            System.out.println("1. when(...).thenReturn(...)");
            Person m = Mockito.mock(Person.class);
            // when(m.init()).thenReturn(); // you can not do that
            Person r = new Person();
            Person s = spy(r);
            when(s.getName()).thenReturn("Jane"); // real getName() is called. You do not want it usually...
        }
        
        {
            // Prefer this way. It is more powerfull.
            System.out.println("1. doReturn(...).when(...)");
            Person m = Mockito.mock(Person.class);
            doNothing().when(m).init(); // void method mock
            doReturn("Jane").when(m).getName();
            System.out.println("name: " + m.getName());
        }
        
        

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
