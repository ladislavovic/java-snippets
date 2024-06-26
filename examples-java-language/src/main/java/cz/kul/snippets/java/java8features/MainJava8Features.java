/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java.java8features;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import cz.kul.snippets.agent.AgentManager;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

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

    /**
     * Method references allow refer to particular function.
     *
     * We can use it instead of lambda expressions. Referenced function must
     * have of course required syntax.
     */
    @Test
    public void methodReferences() {
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

    @Test
    public void functionalInterface() {
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

    /**
     * You can now create static methods also in interfaces, not in the classes
     * only.
     */
    @Test
    public void interfaceWithStaticMethod() {
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
    @Test
    public void interfaceWithDefaultMethod() {
        InterfaceWithDefaultImpl inst = new InterfaceWithDefaultImpl();
        inst.defaultMethod();
    }

    /**
     * <ul>
     *    <li>Optional type helps you to avoid NPE</li>
     *    <li>Use it as return value of service. It is very well self documenting feature</li>
     *    <li>do not use it in domain model, it is not serializable</li>
     * </ul>
     */
    @Test
    public void optional() {
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
            //
            // If a value is present, it returns an Optional containing the result of applying the given mapping function.
            // Otherwise, it returns an empty Optional.
            //
            // By map() function you can get through object hierarchy without
            // explicit null check. Null check is done internally in map function.
            Optional<User> user = Optional.of(new User());
            String addressStr = user
                    .map(User::getInvoiceData) // Invoice data is null, but everything works without any null check
                    .map(InvoiceData::getAddress)
                    .map(Address::getAddressString)
                    .orElse("No address");
            Assert.assertEquals("No address", addressStr);
        }

        {
            // flatMap()
            //
            // The map() operation creates a new Optional<T> instance. You do not want it when the
            // method returns an Optional instance already. Then you have Optional<Optional<T>> as
            // a result type.
            //
            // In this case use flatMap(), it does not create another Optional<T> instance.
            Optional<User> user = Optional.of(new User());
            String addressStr = user
                    .flatMap(User::getInvoiceDataOpt) // map would return Optional<Optional<InvoiceData>>
                    .map(InvoiceData::getAddress)
                    .map(Address::getAddressString)
                    .orElse("No address");
            Assert.assertEquals("No address", addressStr);
        }
        
        {
            // filter()
            //
            // If a value is present, and the value matches the given predicate, returns the same Optional.
            // Otherwise, returns an empty Optional.
            //
            Optional<User> user = Optional.of(new User());
            boolean hasAddress = user
                    .flatMap(User::getInvoiceDataOpt)
                    .map(InvoiceData::getAddress)
                    .filter(addr -> {
                        String addressString = addr.getAddressString();
                        return StringUtils.isNoneBlank(addressString) && addressString.length() > 5;
                    })
                    .isPresent();
            Assert.assertFalse(hasAddress);
        }

        {
            // orElseGet()
            //
            // TODO
            //


        }

    }

    @Test
    public void streams() {
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
        
        {
            // Find out if any of the long numbers is odd
            assertTrue(LongStream.of(2L, 4L, 5L).anyMatch(x -> x % 2 == 1));
            assertFalse(LongStream.of(2L, 4L, 6L).anyMatch(x -> x % 2 == 1));
        }
        
        {
            // Stream to array
            assertArrayEquals(new String[] {"hi", "by"}, Stream.of("hi", "by").toArray(size -> new String[size]));
            assertArrayEquals(new String[] {"hi", "by"}, Stream.of("hi", "by").toArray(String[]::new));
        }

        {
            // Runtime exception throwed during stream transformations is throwen out without any wrapper
            RuntimeException ex = new RuntimeException("foo");
            try {
                Stream
                    .of("hi", "hello")
                    .map(x -> {
                        throw ex;
                    });
            } catch (Exception e) {
                Assert.assertSame(ex, e);
            }
        }
    }

    @Test
    public void simpleColToMap() {
        List<String> strings = Arrays.asList("monica", "rachel");
        Map<String, String> firstLetterToString = strings
                .stream()
                .collect(Collectors.toMap(
                        x -> x.substring(0, 1), // function which generate key
                        x -> x                  // function which generate value
                ));
        assertEquals(2, firstLetterToString.size());
        assertEquals("monica", firstLetterToString.get("m"));
        assertEquals("rachel", firstLetterToString.get("r"));
    }
    
    @Test
    public void complexColToMap() {
        // This is a complex example how to use toMap. But in this particular example it is
        // easier to use groupingBy
        HashMap<String, List<String>> map = Stream
                .of("monica", "rachel", "mia")
                .collect(Collectors.toMap(
                        x -> x.substring(0, 1), // generate key
                        Arrays::asList, // generate value
                        (List<String> x, List<String> y) -> { // merge values with the same key
                            List<String> newList = new ArrayList<>();
                            newList.addAll(x);
                            newList.addAll(y);
                            return newList;
                        },
                        HashMap::new
                ));
        assertEquals(2, map.size());
        assertEquals(2, map.get("m").size());
        assertTrue(map.get("m").contains("monica"));
        assertTrue(map.get("m").contains("mia"));
    }
    
    @Test
    public void testGroupingBy() {
        Map<String, List<String>> map = Stream
                .of("monica", "rachel", "mia")
                .collect(Collectors.groupingBy((String x) -> x.substring(0, 1)));
        
        assertEquals(2, map.size());
        assertEquals(2, map.get("m").size());
        assertTrue(map.get("m").contains("monica"));
        assertTrue(map.get("m").contains("mia"));
    }

    @Test(expected = IllegalStateException.class)
    public void colToMapDupliciteKeyError() {
        List<String> strings = Arrays.asList("monica", "rachel", "mia");
        Map<String, String> firstLetterToString = strings
                .stream()
                .collect(Collectors.toMap(
                        x -> x.substring(0, 1),
                        x -> x
                ));
    }

    @Test()
    public void colToMapDupliciteKeyMerge() {
        List<String> strings = Arrays.asList("monica", "rachel", "mia", "maddison");
        AgentManager.addAgent("colToMap", x -> x);

        Map<String, String> firstLetterToString = strings
                .stream()
                .collect(Collectors.toMap(
                        x -> x.substring(0, 1),
                        x -> x,
                        (x, y) ->  {
                            // x and y params are values from original collection which produce the same key
                            // So random pair from (monica, mia, maddison)
                            String concat = x + "," + y;
                            AgentManager.executeAgent("colToMap", concat);
                            return concat;
                        }
                ));
        assertEquals(2, firstLetterToString.size());
        assertEquals("rachel", firstLetterToString.get("r"));
        assertTrue(firstLetterToString.get("m").contains("monica"));
        assertTrue(firstLetterToString.get("m").contains("mia"));
        assertTrue(firstLetterToString.get("m").contains("maddison"));
        assertEquals(2, AgentManager.getAgentLog("colToMap").getCallCount());
    }

    @Test
    public void testExceptionsInLambda() {
        // When the exception is thrown from lambda it works like any other exception, In stacktrace you have:
        //   * lambda itself
        //   * filter method (which executed the lambda)
        //   * testExceptionsInLambda (which executed the filter method)
        //
        // But there is not an "Predicate.test()". But it is a detail.
        //

        Address address = new Address();
        InvoiceData invoiceData = new InvoiceData();
        invoiceData.setAddress(address);
        User user = new User();
        user.setInvoiceData(invoiceData);

        boolean isAddressPresent = Optional.of(user)
                .map(User::getInvoiceData)
                .map(InvoiceData::getAddress)
                .filter(addr -> {
                    return addr.getAddressString().length() > 10; // <-- an exception thrown, because no address String present
                })
                .isPresent();

    }

}
