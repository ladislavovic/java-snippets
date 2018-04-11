package cz.kul.snippets.java._06_generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Generics {

    public static void main(String[] args) {
        howToCreateGenericType();
        inheritance();
        genericMethods();
        retyping();
        wildcards();
    }
    
    /**
     * Create your own generic type is easy. Add sharp brackets after name
     * of class or interface.
     */
    private static void howToCreateGenericType() {
        class Foo<E> {
            public E field;
            public String toString() {
                return field.toString();
            }
        }
        Foo<String> a = new Foo<>();
    }
    
    /**
     * You can inherit generic type from non generic and also non generic type
     * from generic.
     */
    private static void inheritance() {
        // non-generic from generic
        class ListStr extends ArrayList<String> {
            private static final long serialVersionUID = 1L;
        }
        ListStr a = new ListStr();
        
        // Assignability
        Collection<Number> foo  = new ArrayList<Number>();
        Collection<?> foo2 = new ArrayList<Number>();
        Collection<? extends CharSequence> foo3 = new ArrayList<String>();
        Collection<? super String> foo4 = new ArrayList<CharSequence>();
        Map<? extends CharSequence, ? extends List<? extends Number>> foo5 = new HashMap<String, List<Integer>>();
    }
    
    private static void genericMethods() {
        // Usually you can call it normally. Compiler has enough information.
        bar("Hi");
        
        // But you can explicitely define type during the call
        Generics.<Object>bar("Hi");
    }
    private static <E> void bar(E inst) {
    }
    
    /**
     * List<Number> is NOT ancestor of List<Integer>. If you want to cast, you
     * must do it this way:
     */
    private static void retyping() {
        List<Integer> a = new ArrayList<>();
        @SuppressWarnings({ "unchecked" })
        List<Number> b = (List<Number>)(List<?>) a;
        
//        It is not possible because of this:
//        
//        List<Number> a = new ArrayList<>();
//        List<Integer> b = new ArrayList<>();
        //        a = b; // this row would be the compilation ERROR
//        a.add(new Double(10)); // double is added to integer list
    }
    
    /**
     * Wildcard determine group of parametrized types.
     */
    private static void wildcards() {
        List<? extends Number> a = new ArrayList<Integer>(Arrays.asList(10, 20));
        a = new ArrayList<Number>();
        a = new ArrayList<Integer>();
        
        // "? extends Number" collections (Read only) 
        // reading: you can read fromt this coll. The retunr type is "Number"
        // writing: You can not write because you do no know the real type of
        // collection.
        Number num = a.get(0); // this is OK
        // a.add(10); // This is wrong!
        
        // "? super Integer" collections (Write only)
        // reading: you can read but you can getAppender only "Object type"
        // writing: you can write "Integer" types into coll like this. This
        // the only type, which is secure to write.
        List<? super Integer> b = new ArrayList<Number>(Arrays.asList(10, 20));
        Object o = b.get(0); // correct but not very usefull
        b.add(10); // correct, you can do it for List<Integer>, List<Number>,
                   // List<Object>. So it is ok.
    }

}
