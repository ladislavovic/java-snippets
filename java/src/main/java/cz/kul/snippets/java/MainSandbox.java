package cz.kul.snippets.java;

import org.junit.Assert;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainSandbox {
    
    public static void main(String[] args) {

        List<Integer> integers = Arrays.asList(1, 2, 3);
        System.out.println(integers.subList(3, 3));

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
