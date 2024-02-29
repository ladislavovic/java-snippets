package cz.kul.snippets.java.java_13_features;

import jdk.jshell.spi.ExecutionControl;
import org.junit.Assert;
import org.junit.Test;

public class Java13Features {


  @Test
  public void textBlocks() {

    // There has to be new line after opening """
    //
    // The metod stripIndent() is called to reduce indentaion. If you want some indentation for all
    // linex, move the closing """ to the left. See the next examples
    String json = """
        {
          "firstName" : "Monica",
          "secondName" : "Geller"
        }
        """;

    {
      String s = """
          aaa""";
      Assert.assertEquals("aaa", s);
    }

    {
      String s = """
          aaa
          """;
      Assert.assertEquals("aaa\n", s);
    }

    {
      String s = """
            aaa
          """;
      Assert.assertEquals("  aaa\n", s);
    }

    // You can separate line by backslash. Then it does NOT add the new line character.
    {
      String s = """
          aaa\
          bbb""";
      Assert.assertEquals("aaabbb", s);
    }

  }

}
