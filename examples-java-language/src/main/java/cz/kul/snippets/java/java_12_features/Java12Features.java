package cz.kul.snippets.java.java_12_features;

import org.apache.http.util.Asserts;
import org.junit.Assert;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class Java12Features {

  @Test
  public void indentMethod() {

    // indent string
    String info = "OS: Linux\nArch: x86\nCPUs: 8\n";
    String indentedInfo4 = info.indent(4);// Add 4 spaces to the beginning of the each line
    Assert.assertEquals("    OS: Linux\n    Arch: x86\n    CPUs: 8\n", indentedInfo4);

    // change indentation to 2 spaces
    String indentedInfo2 = indentedInfo4.indent(-2);
    Assert.assertEquals("  OS: Linux\n  Arch: x86\n  CPUs: 8\n", indentedInfo2);
  }

  @Test
  public void switchStatements() {
    // It is only an preview in Java 12, some features were introduced in later Java versions

    DayOfWeek dayOfWeek = DayOfWeek.WEDNESDAY;

    String dayType = switch (dayOfWeek) {
      case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Working day";   // does not have to call "break"
      case SATURDAY, SUNDAY -> {
        Assert.fail("It should not go here ...");
        yield "Day Off";                                                    // for blocks use "yield" to return a value
      }
    };

    Assert.assertEquals("Working day", dayType);
  }

  @Test
  public void automaticCastWhenInstanceof() {
    Object obj = "hi";
    if (obj instanceof String s) {
      // I do not need to cast manually here anymore !!!
      int length = s.length();
    }
  }



}
