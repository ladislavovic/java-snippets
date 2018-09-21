package cz.kul.snippets.java.example17_common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.time.DayOfWeek;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

public class MainJavaCommon {

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
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // does NOT give a slash at the beginning
        assertNull(classLoader.getResourceAsStream("/res1.txt"));

        // correct is to start WITHOUT slash
        assertNotNull(classLoader.getResourceAsStream("res1.txt"));

        // if you go to directory, use a slash
        assertNotNull(classLoader.getResourceAsStream("dir/res2.txt"));

    }

}
