package cz.kul.snippets.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Sandbox {

  public static void main(String[] args) {


    String str1 = new String("hi");
    String str2 = new String("hi");

    if (str1 == str2) {
      System.out.println("jsou identicke");
    } else {
      System.out.println("nejsou identicke");
    }

    if (str1.equals(str2)) {
      System.out.println("jsou rovny");
    } else {
      System.out.println("nejsou rovny");
    }


  }

  private static void foo() {
      throw new IllegalStateException();
  }


  private String str;



}
