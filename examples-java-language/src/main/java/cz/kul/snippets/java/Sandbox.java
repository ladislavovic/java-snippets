package cz.kul.snippets.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
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

    public static void main(String[] args)
    {

//        System.out.println(ZonedDateTime.parse("2025-11-04T00:00:00+05:00"));

        System.out.println(Stream.of("foo", null, "bar"));

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
