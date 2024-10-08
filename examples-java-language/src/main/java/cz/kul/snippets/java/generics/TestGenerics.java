package cz.kul.snippets.java.generics;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class TestGenerics {

    @Test
    public void genericMethods() {
        // Usually you can call it like any other method. Compiler has enough information.
        List<Integer> integers = Arrays.asList(10, 20, 30);

        // But you can explicitely define type during the call
        List<Object> objects = Arrays.<Object>asList(10, 20, 30);

        List<? extends Number> list = Arrays.asList(10, 10.5, 20.5);
    }

    @Test
    public void retyping() {
        // List<Number> is NOT ancestor of List<Integer>.
        // It is not possible because of this:
        //
        // List<Number> a = new ArrayList<>();
        // List<Integer> b = new ArrayList<>();
        // a = b; // this row would be the compilation ERROR
        // a.add(new Double(10)); // double is added to integer list

        // If you want to retype you must do it this way
        // It is unchecked of course
        List<Integer> a = new ArrayList<>();
        @SuppressWarnings({ "unchecked" })
        List<Number> b = (List<Number>)(List<?>) a;
    }

    @Test
    public void wildcards() {
        // Wildcard determine group of parametrized types.
        List<? extends Number> a;
        a = new ArrayList<Number>();
        a = new ArrayList<Integer>();
        
        // "? extends Number" collections (Read only) 
        // reading: you can read from this coll. The returned type is "Number"
        // writing: You can not write because you do not know the real type of the
        // collection.
        // a.get(0); // this is OK
        // a.add(10); // This is wrong! Because you are inserting Integer, but the collection can be List<Short> for example
        
        // "? super Integer" collections (Write only)
        // reading: you can read, but you can getAppender only "Object type"
        // writing: you can write "Integer" types into coll like this. This
        // the only type, which is secure to write.
        List<? super Integer> b = new ArrayList<Number>(Arrays.asList(10, 20));
        Object o = b.get(0); // correct but not very useful
        b.add(10); // correct, you can do it for List<Integer>, List<Number>,
                   // List<Object>. So it is ok.
    }
    
}
