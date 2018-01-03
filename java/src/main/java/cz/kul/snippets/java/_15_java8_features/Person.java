/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java._15_java8_features;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

/**
 *
 * @author kulhalad
 */
public class Person {

    public enum Sex {
        MALE, FEMALE
    }
    
    String name;
    LocalDate birthday = LocalDate.MIN;
    Sex gender;
    String emailAddress;

    public Person() {
    }
    
    public Person(LocalDate birthday) {
        this.birthday = birthday;
    }
    
    public int getAge() {
        return 42;
    }
    
    public LocalDate getBirthday() {
        return birthday;
    }    

    public static int compareByAge(Person a, Person b) {
        return a.birthday.compareTo(b.birthday);
    }

    public String getName() {
        return name;
    }
    
}
