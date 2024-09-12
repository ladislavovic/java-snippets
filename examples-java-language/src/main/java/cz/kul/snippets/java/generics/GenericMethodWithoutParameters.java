package cz.kul.snippets.java.generics;

import org.junit.Test;

import java.util.NoSuchElementException;

public class GenericMethodWithoutParameters
{

    // This method does not have typed parameter and can return generic type according to the assignment left side
    private static <T> MutableOptional<T> createEmptyOptional() {
        return new MutableOptional<T>(null);
    }

    // This method need to know type in runtime.
    // In Java it is not possible to get it from T (there are some ways by reflection, but do not know if they are reliable).
    // The common workaround is to add Class<T> parameter
    @SuppressWarnings("unchecked")
    private static <T> MutableOptional<T> createDefaultOptional(Class<T> clazz)
    {
        if (clazz.equals(String.class)) {
            MutableOptional<String> stringMutableOptional = new MutableOptional<>("");
            return (MutableOptional<T>) stringMutableOptional;
        } else if (clazz.equals(Integer.class)) {
            MutableOptional<Integer> integerMutableOptional = new MutableOptional<>(0);
            return (MutableOptional<T>) integerMutableOptional;
        } else {
            return new MutableOptional<>(null);
        }
    }

    @Test
    public void testGenericMethodWithoutParameters()
    {
        // In this example I've created an instance of MutableOptional with Integer generics type.
        // The compiler takes the information about the type from the left side of the assignment.
        MutableOptional<Integer> optionalInteger = createEmptyOptional();
        optionalInteger.set(10);

        // Similar, but with MutableOptional<String>
        MutableOptional<String> optionalString = createEmptyOptional();
        optionalString.set("Hello");

        // Here the compiller does not have the type information, so it creates MutableOptional<Object>
        var optionalVar = createEmptyOptional();
    }

    public static class MutableOptional<T> {

        private T value;

        public MutableOptional(T value)
        {
            this.value = value;
        }

        public T get() {
            if (value == null) {
                throw new NoSuchElementException();
            } else {
                return value;
            }
        }

        public void set(T value)
        {
            this.value = value;
        }

        public boolean isPresent() {
            return value != null;
        }

    }

}
