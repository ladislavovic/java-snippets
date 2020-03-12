package cz.kul.snippets.java.example17_common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.time.DayOfWeek;
import java.util.*;

import org.junit.Assert;
import org.junit.Test;

public class TestJavaCommon {
    
    /**
     * NOTE1: It is not allowed to have duplicate keys. The behaviour is undefined than.
     * NOTE2: White characters around '=' char are trimmed, they are added neither the key
     * nor the value. NOTE3: White characters at the end of line are not trimmed! They are
     * added to the value.
     */
    @Test
    public void loadProperties() throws IOException {
        StringBuilder p = new StringBuilder();
        p.append("foo=val1\n");
        p.append("bar =     val2\n");
        p.append("baz=val3 \n");
        Properties properties = new Properties();
        properties.load(new StringReader(p.toString()));
        assertEquals("val1", properties.getProperty("foo"));
        assertEquals("val2", properties.getProperty("bar"));
        assertEquals("val3 ", properties.getProperty("baz"));
        assertEquals(null, properties.getProperty("aaa"));
    }

    /** You can not use null value in switch */
    @Test
    public void NPEInSwitch() {
        try {
            DayOfWeek day = true ? null : DayOfWeek.MONDAY;
            switch (day) {
            default:
                break;
            }
            Assert.fail("NPE should be throwen");
        } catch (NullPointerException e) {

        }
    }

    /**
     * Drawback of Vector is all operations are synchronized so it is slower.
     * 
     * Generally you want to synchronize a whole sequence of operations. Synchronizing
     * individual operations is both less safe (if you iterate over a Vector, for
     * instance, you still need to take out a lock to avoid anyone else changing the
     * collection at the same time, which would cause a ConcurrentModificationException in
     * the iterating thread) but also slower (why take out a lock repeatedly when once
     * will be enough)?
     * 
     * Verctor is there from Java 1.0, ArrayList was introduced with Collection API in
     * Java 1.2.
     * 
     * You should always prefer ArrayList.
     */
    @Test
    public void arrayList_VS_vector() {
    }

    /**
     * When you create stirng by constructor you just create a new instance.
     * 
     * When you create string by literal, you create new instace or getAppender the instance from
     * string pool if it is there already. If the instance is not there it is added there
     * automatically.
     * 
     * This approach reduce memory consumption. You usually do not need to create string
     * by constructor, it is higly unpreffered way.
     * 
     * This is called "String interning" and it is implemented by many platforms. It is
     * implementation of flyweight design pattern.
     */
    @Test
    public void stringPool() {
        String s1 = "foo"; // getAppender from string pool or added there
        String s2 = "foo"; // getAppender from string pool or added there
        assertTrue(s1 == s2);

        String s3 = new String("foo"); // new instance created
        assertFalse(s1 == s3);
    }

    @Test
    public void howtoCreateArray() {
        // You can create an array by declaration
        int[] arr1 = new int[] { 1, 2, 3 };
        Object[] arr2 = new Object[] { new Object(), new Object() };

        // Or you can use array literal
        int[] arr3 = { 1, 2, 3 };
        Object[] arr4 = { new Object(), new Object() };

        // But literal can  be used only when you declare a variable.
        // For reassigning, functional call etc. it is not possible
        int[] arr5 = { 1, 2, 4 };
        // arr5 = {4, 5, 6}; // ERROR: Array constants can only be used in initializers
    }

    /**
     * <p>
     * The method isAssignableFrom is dynamic form of instanceof. It check, if the
     * argument of the method is the same type or subtype of the checked class
     * <p>
     * 
     * <pre>
     * Comparison to instaceof:
     * Lets use this syntax: Object > String - object is supertype to String 
     *   Foo.class.isAsignableFrom(Bar.class) WHEN Foo >= Bar
     *   foo instanceof Bar                   WHEN Foo <= Bar 
     * So in comparison with instanceof, you have to swich order of the arguments.
     * </pre>
     */
    @Test
    public void assignableFrom() {
        Object obj = new Object();
        String str = "str";

        assertTrue(Object.class.isAssignableFrom(String.class));
        assertFalse(obj instanceof String);

        assertFalse(String.class.isAssignableFrom(Object.class));
        assertTrue(str instanceof Object);
    }

    /**
     * By this method you can getAppender resource, which is on the classpath.
     */
    @Test
    public void getResourceAsStream() {
        // You need to use classloader for that.
        // You must use relative path, it can not start with slash
        ClassLoader classLoader = getClass().getClassLoader();
        assertTrue(classLoader.getResource("/example17/res1.txt") == null);
        assertTrue(classLoader.getResource("example17/res1.txt") != null);
        
        // You can also use Class object for that.
        // Then it depends if you call it with absolute or relative name. If you use relative name,
        // the name is converted to class_package + your_path. If you use absolute path, the first
        // slash is removed and it is delegated to classloader.
        assertTrue(getClass().getResource("res2.txt") != null);
        assertTrue(getClass().getResource("/res2.txt") == null);
        assertTrue(getClass().getResource("/example17/res1.txt") != null);
    }

    @Test
    public void arrayRetyping() {
        Long[] arr = (Long[]) new Object[10];
        arr[0] = 10L;
        System.out.println(arr[0]);
    }

    private B bInstance = new B();
    
    @Test
    public void myTest() {
        A a = new A();
        B b = new B();
        bInstance.as.add(a);
        b.as.add(a);
        b.a = a;
        System.out.println("a");
    }
    
    @Test
    public void perfUT() {
        Scanner scanner = new Scanner(System.in);
        String myString = scanner.next();
        scanner.close();
        
        perfTest();
    }
    
    @Test
    public void arrayListMaxSizeIsLimitedByArrayMaxLength() {
        // ArrayList can not be larger than max array length
        // This test ends with
        //
        // java.lang.OutOfMemoryError: Requested array size exceeds VM limit
        //
        // when you reach Integer.MAX_VALUE - 2. You can not allocate
        // larger array.
        ArrayList<Integer> list = new ArrayList<>(Integer.MAX_VALUE - 2);
        for (long i = 0; i < 3_000_000_000L ; i++) {
            list.add(1);
            if (i % 10_000_000 == 0) {
                System.out.println(i);
            }
        }
    }
    
    @Test
    public void linkedListCanBeLargerThanArrayMaxLength() {
        // Linked list coud be theoretically larger than
        // Integer.MAX_VALUE, because it is not backed by
        // array. 
        //
        // But it internally save its size and it is stored as int, it
        // coud cause problems.
        // 
        // But I was able to reach only about 1^9 items, than the JVM
        // crashed, maybe wrong memmory module?
        LinkedList<Integer> list = new LinkedList<>();
        for (long i = 0; i < 3_000_000_000L ; i++) {
            list.add((int) (i % 100));
            if (i % 10_000_000 == 0) {
                System.out.println(i);
            }
        }
    }
    
    public static void main(String [] args) {
        Object o = new Object();
        ArrayList<Long> list = new ArrayList<>(11_000_000);
        Set<Long> set = new HashSet<>();
        long[] arr = new long[10_000_000];
        Long[] arr2 = new Long[10_000_000];

        long t = System.currentTimeMillis();
        for (long i = 0; i < 10_000_000; i++) {
//            list.add(i);
//            set.add(i);
            arr[(int)i] = i;
//            arr2[(int)i] = i;
        }
        t = System.currentTimeMillis() - t;
        System.out.println("Time: " + t);
        
        
//        Scanner scanner = new Scanner(System.in);
//        String myString = scanner.next();
//        scanner.close();
//
//        TestJavaCommon testJavaCommon = new TestJavaCommon();
//        testJavaCommon.perfTest();
    }
    
    
    public void perfTest() {
        for (int i = 0; i < 100; i++) {
            m1(i);
        }
    }
    
    private void m1(int i) {
        m2(i);
    }
    
    private void m2(int i) {
        System.out.println("i: " + i);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    
    

}
