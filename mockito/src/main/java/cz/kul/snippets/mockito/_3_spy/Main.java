package cz.kul.snippets.mockito._3_spy;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;

/**
 * You can use Spy to mock existing objects.
 * 
 * Be aware spy create copy of existing object so it does not work with
 * original object.
 * 
 * @author kulhalad
 * @since 7.4
 *
 */
public class Main {

    public static void main(String [] args) {
          List<String> list = new ArrayList<>(); 
          List<String> spy = Mockito.spy(list);
  
          //optionally, you can stub out some methods:
          when(spy.size()).thenReturn(100);
  
          //using the spy calls *real* methods
          spy.add("one");
          spy.add("two");
  
          //prints "one" - the first element of a list
          System.out.println("spy.get(0): " + spy.get(0));
  
          //size() method was stubbed - 100 is printed
          System.out.println("spy.size(): " + spy.size());
          
          //original object is unchenged, it is still empty
          System.out.println("list.size(): " + list.size());
    }

}
