package cz.kul.snippets.java;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.net.MediaType;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.spel.support.ReflectionHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainSandbox {

    private final static Logger LOGGER = LoggerFactory.getLogger(MainSandbox.class);

private Set<Object> customCriteria;


    public<T> List<T> getAllCustomCriteria(Class<T> customCriteriaClass) {
        return (List<T>) customCriteria.stream()
            .filter(x -> customCriteriaClass.isAssignableFrom(x.getClass()))
            .collect(Collectors.toList());
    }

    public static final class F {

        private F() {

        }

    }

    public static class A {
        int a;
        String b;
        String[] c;

        public A(int a, String b) {
            this.a = a;
            this.b = b;
        }

        public int getA() {
            return a;
        }

        public String getB() {
            return b;
        }

        public String[] getC() {
            return c;
        }

        @Override
        public String toString() {
            return "A{" +
                "a=" + a +
                ", b='" + b + '\'' +
                '}';
        }
    }

    public static void main(String[] args) throws Exception {

        String foo = "Ostrava";
        byte[] bytes = foo.getBytes(StandardCharsets.UTF_8);
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        System.out.println(bb.position());
        System.out.println(bb.limit());
        System.out.println(bb.capacity());

        int LIMIT = 50;
        int actualLimit = Math.min(bb.capacity(), LIMIT);


        bb.limit(actualLimit);
        ByteBuffer bb2 = bb.slice();
        System.out.println(bb2.capacity());

        System.out.println(StandardCharsets.UTF_8.decode(bb2));





//        long from = 12_500_000;
//        long to = 22_500_000;
//
//        HttpClient httpClient = HttpClient.newHttpClient();
//        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(300);
//        for (long i = from; i < to; i++) {
//            pool.submit(new AddDoc(i, httpClient));
//        }
//        pool.shutdown();
    }

    public static class AddDoc implements Runnable {

        private long id;

        private HttpClient httpClient;

        public AddDoc(long id, HttpClient httpClient) {
            this.id = id;
            this.httpClient = httpClient;
        }

        @Override
        public void run() {
            String crossId = RandomStringUtils.randomAlphabetic(14);
            String name = RandomStringUtils.randomAlphabetic(20);
            String nodeType = RandomStringUtils.randomAlphabetic(10);

            String payload = String.format(
                "{\"_entity_type\": \"Node\", \"crossId\": \"%s\", \"name\":\"%s\", \"nodeTypes\":\"%s\"}",
                crossId,
                name,
                nodeType);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9200/cross_node-000001/_doc/" + id))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

            try {
                HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
                if (id % 10000 == 0) {
                    System.out.println("DONE " + id);
                }
//                Thread.sleep(10);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static class Foo {

        public String getRandomString(int length) {
            return RandomStringUtils.randomAlphabetic(length);
        }

    }
    
    
    
    public static void probeDataGenerator() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(String.format("NE:BUILDING_Ostrava/NE%1$s:  :1:FE::  :Network Element %1$s:10.0.0.1:255.0.0.0\n\n", i));
            
        }
    }
    
    public static void voucherCodeGenerator() {

        SecureRandom sr = new SecureRandom();
        Set<Integer> voucherNumbers = new HashSet<>();
        int count = 5000;
        while (voucherNumbers.size() < count) {
            byte bytes[] = new byte[4];
            sr.nextBytes(bytes);
            ByteBuffer wrapped = ByteBuffer.wrap(bytes);
            int num = wrapped.getInt();
            num = num % 1_000_000;
            num = Math.abs(num);
            if (num > 100_000) {
                voucherNumbers.add(num);
            }
        }
        voucherNumbers.forEach(x -> System.out.println(x));
    }    
    
}
