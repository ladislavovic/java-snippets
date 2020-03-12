package cz.kul.snippets.mapdb.example01_bases;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class TestExample01 {
    
    @Test
    public void compare_speed() {
        
        // insert into mapdb
//        DB db = DBMaker.memoryDB().make();
        DB db = DBMaker.memoryDirectDB().make();
        ConcurrentMap map = db.hashMap("map", Serializer.LONG, Serializer.LONG).createOrOpen();


        long time = System.currentTimeMillis();
        for (long i = 0; i < 1_000_000; i++) {
            map.put(i, i);
        }
        time = System.currentTimeMillis() - time;
        System.out.println("inserting into mapdb: " + time);
        
        // insert into common map
        HashMap<Long, Long> commonMap = new HashMap<>();
        time = System.currentTimeMillis();
        for (int i = 0; i < 1_000_000; i++) {
            Long num = new Long(i);
            commonMap.put(num, num);
        }
        time = System.currentTimeMillis() - time;
        System.out.println("inserting into commonmap: " + time);


        Random random = new Random();
        ArrayList<Long> keys = new ArrayList<>(10_000);
        for (int i = 0; i < 10_000; i++) {
            keys.add((long) random.nextInt(1_000_000));
        }
        
        // get from mapdb
        long sum = 0;
        time = System.currentTimeMillis();
        for (Long key : keys) {
            sum = sum + (Long) map.get(key);
        }
        time = System.currentTimeMillis() - time;
        System.out.println(sum);
        System.out.println("mapdb get: " + time);
        
        // get from common map
        sum = 0;
        time = System.currentTimeMillis();
        for (Long key : keys) {
            sum = sum + commonMap.get(key);
            commonMap.get(key);
        }
        time = System.currentTimeMillis() - time;
        System.out.println(sum);
        System.out.println("mapdb get: " + time);
    }
    
    @Test
    public void getALotMemmory() {
//        commonMap();
        mapdb();        
    }
    
    private void commonMap() {
        Map<Person, Person> map = new HashMap<>();
        for (int i = 0; i < 1_000_000; i++) {
            Person person = new Person(RandomStringUtils.randomAlphabetic(32), RandomStringUtils.randomAlphabetic(32));
            map.put(person, person);
        }
        System.out.println(map.size());        
    }
    
    private void mapdb() {
        DB db = DBMaker.memoryDB().make();
        ConcurrentMap map = db.hashMap("map").createOrOpen();
        for (int i = 0; i < 1_000_000; i++) {
            Person person = new Person(RandomStringUtils.randomAlphabetic(32), RandomStringUtils.randomAlphabetic(32));
            map.put(person, person);
        }
        System.out.println(map.size());        
    }
    
    private static class Person implements Serializable {
        String firstName;
        String secondName;

        public Person(String firstName, String secondName) {
            this.firstName = firstName;
            this.secondName = secondName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return Objects.equals(firstName, person.firstName) &&
                    Objects.equals(secondName, person.secondName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(firstName, secondName);
        }
    }
    
}
