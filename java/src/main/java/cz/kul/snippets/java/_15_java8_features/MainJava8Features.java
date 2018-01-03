/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java._15_java8_features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;

/**
 * Java 8 brings a lot of new features. Some of them are covered by following
 * snippets but some not. Here is the list of not followed features: - you can
 * repeat an annotation more time on the same place (@Repeatable) - better type
 * inference - extends context where annotations can be used - local variables,
 * generic type, exception in function declaration, ... an many others
 *
 * @author kulhalad
 */
public class MainJava8Features {

    public static void main(String[] args) {
        lambda();
        methodReferences();
        functionalInterface();
        interfaceWithStaticMethod();
        interfaceWithDefaultMethod();
        optional();
        streams();
        date();
    }

    private static void functionalInterface() {
        // In pre Java8 there are SAM Interfaces (Single Method Interface) - for example Comparator, ActionListener, ...
        // In Java8 the concept is enhanced and FunctionalInterfaces are introduced
        //
        // There is a lot of Functional Interfaces in JDK and you can also create your own.

        Predicate<String> longerThanFive = s -> s != null && s.length() > 5;
        assertFalse(longerThanFive.test("ahoj"));
        assertTrue(longerThanFive.test("nazdar"));

        Function<Object, String> toString = Object::toString;
        assertEquals("10", toString.apply(new Integer(10)));

        IntBinaryOperator power = (left, right) -> (int) Math.pow(left, right);
        assertEquals(1000, power.applyAsInt(10, 3));
    }

    private static void date() {
        // Advantages in comparison with old API
        // * new API is immutable - no setters. So it is thread safe.
        // * old API has poor design - month starts from 1, days starts from 0
        // * much easier timezone handling

        {
            // Local Date and time
            //
            // Use this when you do not need to handle timezone.
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();

            LocalDate particularDate = LocalDate.of(2017, Month.JANUARY, 10);
            Assert.assertEquals(1, particularDate.getMonthValue());

            LocalDate first = particularDate.withDayOfMonth(1);
            Assert.assertEquals(1, first.getDayOfMonth());

            LocalDate parsedDate = LocalDate.parse("2017-01-02");
            Assert.assertEquals(2017, parsedDate.getYear());
            Assert.assertEquals(1, parsedDate.getMonthValue());
            Assert.assertEquals(2, parsedDate.getDayOfMonth());

            LocalTime parsedTime = LocalTime.parse("16:10:22");
            Assert.assertEquals(16, parsedTime.getHour());
            Assert.assertEquals(10, parsedTime.getMinute());
            Assert.assertEquals(22, parsedTime.getSecond());
        }

        {
            // Instant
            //
            // It is just point on the timeline. It has different local time in every
            // timezone.
            // But this instance does not know nothing about timezone or offset.
            Instant startOfEpoch = Instant.ofEpochSecond(0);

            // OffsetDateTime
            //
            // It adds to instance an offset from UTC/Greenwich. You can get local
            // date and time from it.
            OffsetDateTime offsetPlusFive = startOfEpoch.atOffset(ZoneOffset.of("+5"));
            assertTrue(startOfEpoch.equals(offsetPlusFive.toInstant()));
            assertEquals(5, offsetPlusFive.getHour());
            LocalDateTime local = offsetPlusFive.toLocalDateTime();
            assertEquals(5, local.getHour());

            // ZonedDateTime
            //
            // Add complete zone information (daylight saving, ... not the offset only)
            // to instance
            ZonedDateTime newYork = startOfEpoch.atZone(ZoneId.of("America/New_York"));
            assertTrue(startOfEpoch.equals(newYork.toInstant()));
            assertEquals(1969, newYork.toLocalDate().getYear());
        }

    }

    /**
     * Lambda is just piece of the function which can be used anywhere in the
     * program.
     */
    private static void lambda() {
        // Simple lambda
        // - note lambdac can work with local params
        StringBuilder sb = new StringBuilder();
        Arrays.asList("a", "b", "c").forEach(e -> sb.append(e));
        Assert.assertEquals("abc", sb.toString());

        // Full syntax lambda
        // - you can ommit many parts of lambda syntax
        // - parentheses around parameters - optional if you have only one param
        // - type of input params - can be often determined from surrounding code
        // - curly bracket - when you have only one expression, you can ommit
        //   then it is also return value. If you write curly brackets, you 
        //   have to write return statement if you want returns something.
        Arrays.asList(1, 2, 3).forEach((Integer e) -> {
            System.setProperty("foobar", e.toString());
            return;
        });

    }

    /**
     * Method references allow refer to particular function.
     *
     * We can use it instead of lambda expressions. Referenced function must
     * have of course required syntax.
     */
    private static void methodReferences() {
        {
            // reference to static method
            List<Person> list = Arrays.asList(new Person(), new Person(), new Person());
            Comparator<Person> comp = Person::compareByAge;
            list.sort(Person::compareByAge);
        }

        {
            // reference to instance method of a particular object
            List<Person> list = Arrays.asList(new Person(), new Person(), new Person());
            ComparisonProvider provider = new ComparisonProvider();
            list.sort(provider::compareByAge);
        }

        {
            // Reference to an Instance Method of an Arbitrary Object of a Particular Type
            // (it means it refers to "normal" instance method, but not of particular instance)
            String[] stringArray = {"Barbara", "James", "Mary", "John",
                "Patricia", "Robert", "Michael", "Linda"};
            Arrays.sort(stringArray, String::compareToIgnoreCase);
        }

        {
            // Reference a constructor
            // TODO
        }
    }

    /**
     * You can now create static methods also in interfaces, not in the classes
     * only.
     */
    private static void interfaceWithStaticMethod() {
        InterfaceWithStatic.staticMethod();
    }

    /**
     * Very interesting feature.
     *
     * You can create default method in interface and descendants does not have
     * to implement it.
     *
     * It is useful when you need to add method into existing interface. You
     * does not have to add implementation into all existing descendants (which
     * is often not possible). It just not break binary compatibility.
     */
    private static void interfaceWithDefaultMethod() {
        InterfaceWithDefaultImpl inst = new InterfaceWithDefaultImpl();
        inst.defaultMethod();
    }

    /**
     * - Optional type helps you to avoid NPE. - use it as return value of
     * service. It is very well self documenting feature. - do not use it in
     * domain model, it is not serializable - you can check null more
     * effectively with it because of Java 8 new syntax possibilities
     */
    private static void optional() {
        {
            // basic usage
            Optional<String> optional = Optional.ofNullable("hi");
            if (optional.isPresent()) {
                String obj = optional.get();
                Assert.assertTrue(obj.equals("hi"));
            }
        }

        {
            // map()
            // By map() function you can get through object hierarchy without
            // explicit null check. Null check is done internally in map function.
            Optional<Level1> optional = Optional.ofNullable(new Level1());
            String value = optional
                    .map(Level1::getLevel2)
                    .map(Level2::getLevel3)
                    .map(Level3::getValue).orElse("default");
            Assert.assertEquals("default", value);
        }
        
        {
            // filter()
            Optional<String> optional = Optional.of("hi");
            Optional<String> optional2 = optional.filter(Thread::holdsLock);
            Assert.assertFalse(optional2.isPresent());
        }

    }

    private static void streams() {
        {
            // stream creation
            
            // from collection
            Stream<String> stream1 = Arrays.asList("a", "b", "c").stream(); 
            
            // from array
            Stream<String> stream2 = Arrays.stream(new String[] {"a", "b", "c"});
        }
        
        {
            // map()
            // 
            // It allows convert items to something else. The parameter of map function is
            // implementation of Function interface.
            List<Integer> list = Arrays.asList(10, 20, 30);
            List<String> list2 = list.stream()
                    .map(i -> i.toString())
                    .collect(Collectors.toList());
            Assert.assertEquals("10", list2.get(0));
        }

        {
            // filter()
            //
            // It allows to throw some items out. The parameteer is implementation of Predicate.
            List<Integer> list = Arrays.asList(10, 20, 30);
            List<Integer> list2 = list.stream()
                    .filter(i -> i > 20)
                    .collect(Collectors.toList());
            Assert.assertEquals(1, list2.size());
            Assert.assertEquals(30, (int) list2.get(0));
        }

        {
            // reduce()
            //
            // Reduce is a terminal operation which convert whole stream into one instantion.
            // it is typically sum, avg, ...
            // Some operations are already build in (sum, avg, ...) If you need to use
            // your own
            // operation, you must create lambda with two parameters. The first param is
            // reduction of previous items and the second is actual item. It enables to
            // run it
            // paralelly.
            // There is another parameter - identity. It is somethig like "zero" item. It
            // is returned
            // when the stream is empty or it is also probably used for first item as
            // reduction of
            // previous items.

            // build in reduce operation
            int sum = Arrays.stream(new int[] { 1, 5, 8 }).sum();
            Assert.assertEquals(14, sum);

            // custom reduce operation
            String concatenation = Arrays.asList("a", "b", "c").stream()
                    .reduce("", (a, b) -> a + b);
            Assert.assertEquals("abc", concatenation);
        }

        {
            //
            // Howto group items according to some attribute
            //
            Map<String, List<String>> groups = Stream.of("Hi", "Hello", "Good by", "Welcome")
                    .collect(Collectors.groupingBy(x -> Character.toString(x.charAt(0))));
            assertEquals(3, groups.size());
            assertEquals(2, groups.get("H").size());
            assertEquals(1, groups.get("G").size());
            assertEquals(1, groups.get("W").size());
        }

    }

}
