package cz.kul.snippets.java;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Assert;
import org.springframework.expression.spel.support.ReflectionHelper;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainSandbox {

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

       String str = "ěščřžýá";
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        String str2 = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(bytes, 0, 4)).toString();
        System.out.println(str2);
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
