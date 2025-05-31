package cz.kul.snippets.java;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Sandbox extends SandboxSuper
{

    //    public static void main(String[] args)
    //    {
    //        int foo = 10;
    //
    //        System.out.println(null == "ahoj");
    //
    //    }

    public record Foo(String name, String secondName)
    {

    }


    public static <ObjectType, ValueType> boolean setValue(
        ObjectType object,
        ValueType newValue,
        Function<? super ObjectType, ? extends ValueType> getter,
        BiConsumer<ObjectType, ValueType> setter
    ) {
        ValueType oldValue = getter.apply(object);
        if (!Objects.equals(oldValue, newValue)) {
            setter.accept(object, newValue);
            return true;
        }
        return false;
    }




public static class Point
{

    private Long x;
    private Long y;

    public Long getX()
    {
        return x;
    }

    public void setX(final Long x)
    {
        this.x = x;
    }

    public Long getY()
    {
        return y;
    }

    public void setY(final Long y)
    {
        this.y = y;
    }

}


    public static void main(String[] args) throws Exception
    {
        Point point = new Point();
        setValue(point, 10L, Point::getX, Point::setX);

    }


    public static void aaa(String... strs)
    {
        System.out.println(strs);
    }

    public static <T> Collector<T, ?, T> exactlyOneOrNull()
    {
        return Collectors.reducing(
            null, (oldValue, newValue) -> {
                if (oldValue != null) {
                    return null;
                }

                return newValue;
            }
        );
    }

    public static void optional(String[] args)
    {
        // Example stream of Optional<String>
        Stream<Optional<String>> streamOfOptional = Stream.of(
            Optional.of(new String("String1")),
            Optional.empty(),
            Optional.of(new String("String2")),
            Optional.ofNullable(null), // This is equivalent to Optional.empty()
            Optional.of(new String("String3"))
        );

        // Convert Stream<Optional<String>> to List<String>
        List<String> listOfString = streamOfOptional
            .flatMap(Optional::stream) // Transform Optional<String> to String and filter out empty Optionals
            .collect(Collectors.toList()); // Collect the result into a list

        // Print the list
        listOfString.forEach(x -> System.out.println(x));
    }

    public void foo(long a)
    {
        return;
    }

}
