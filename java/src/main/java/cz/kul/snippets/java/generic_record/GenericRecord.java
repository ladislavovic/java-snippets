package cz.kul.snippets.java.generic_record;

import org.junit.Test;

public class GenericRecord {

  // You can just use generics for records as for any "normal" class
  record Pair<T, U>(T x, U y) {}

  @Test
  public void gettingStartedWithGenericRecord() {
    Pair<String, Integer> pair1 = new Pair<>("hi", 5);
  }

}
