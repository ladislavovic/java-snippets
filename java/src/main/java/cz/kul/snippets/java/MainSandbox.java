package cz.kul.snippets.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.net.MediaType;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import jdk.dynalink.beans.StaticClass;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.spel.support.ReflectionHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
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

    public static abstract class AA {

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

    public static class Parent {

        private Class<?> clazz = this.getClass();

        public Class<?> getClazz() {
            return clazz;
        }

    }

    public static class Child extends Parent {
        int a;
        int b;

        public Child() {
            a = 10;
            b = 20;
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
    }

    public static void main(String[] args) throws Exception {

        HashMap<String, Object> map = new HashMap<>();
        map.put("key1", "cus");
        map.put("key2", new Date());
        System.out.println(map);
        if (true) return;



        String s = "\u0001\u0001\u0000\u0000\u0000ö\r\f&I\u001biÁ¥îú@\u0017ËNA";
        String s1 = Hex.encodeHexString(s.getBytes(StandardCharsets.UTF_8));
        System.out.println(s1);
        if (true) return;


        if (true) {

            String str = "Švéds\"\\";

//            String s = StringEscapeUtils.escapeJson(str);
//            System.out.println(s);

            JsonElement jsonTree = new Gson().toJsonTree(str);
            System.out.println(jsonTree);
            System.out.println(jsonTree.getAsString());
            System.out.println(jsonTree.getAsJsonPrimitive().getAsString());

            String strGson = (new Gson()).toJson(str);
            System.out.println("gson: " + strGson);


//            new ObjectMapper().writeValueAsString()


            System.out.println(new ObjectMapper().writeValueAsString(str));

            if (true) return;
        }


//        String data = "\u0001\u0001\u0000\u0000\u0000ú\u0015¾  þiÁõP°Å°YQA";
//        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
//        String base64 = Base64.getEncoder().encodeToString(bytes);
//        System.out.println(base64);
//        if(true) return;

        byte[] bytes = new byte[] {1, 1, 0, 0, 0, 61, 97, 71, -60, -84, 70, 105, -63, 50, -119, -88, -121, -55, 23, 80, 65};


        String b64 = "AQEAAAD6Fb4gIP5pwfVQsMWwWVFB";






//        byte[] bytes = Base64.getDecoder().decode(b64);
        for (int i = 0; i < bytes.length; i++) {
            byte aByte = bytes[i];
            System.out.print(String.format("%02X", aByte));
        }


        if (true) return;


        Set<Set<Integer>> sets = Set.of(
            Set.of(1, 2, 3),
            Set.of(5, 6));

        List<Integer> collect = sets.stream().flatMap(Set::stream).collect(Collectors.toList());
        System.out.println(collect);


        ArrayList<Object> arr = new ArrayList<>();
        arr.add("hi");
        arr.add("hello");
        arr.add("bye");
        System.out.println(arr);
        System.out.println(arr.size());
        arr.remove(0);
        System.out.println(arr);
        System.out.println(arr.size());




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
