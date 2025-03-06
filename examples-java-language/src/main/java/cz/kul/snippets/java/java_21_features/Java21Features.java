package cz.kul.snippets.java.java_21_features;

import org.junit.Test;

public class Java21Features
{

    public record Point(int x, int y)
    {

    }

    @Test
    public void patternMatchingForSwitch()
    {
        String o = null;
//        Object result = switch (o) {
//            case String s -> "string";
////            default -> "unknown";
//        };

        Object result = switch (o) {
            case String s when s.length() > 10 -> "long string";
            default -> "unknown";
        };

        Integer i = 10;
        result = switch (i) {
            case Integer num when num > 10 -> "big number";
            default -> "unknown";
        };



    }

}
