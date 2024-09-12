package cz.kul.snippets.java.java_12_features;

import org.apache.http.util.Asserts;
import org.junit.Assert;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class Java12Features
{

    @Test
    public void indentMethod()
    {

        // indent string
        String info = "OS: Linux\nArch: x86\nCPUs: 8\n";
        String indentedInfo4 = info.indent(4);// Add 4 spaces to the beginning of the each line
        Assert.assertEquals("    OS: Linux\n    Arch: x86\n    CPUs: 8\n", indentedInfo4);

        // change indentation to 2 spaces
        String indentedInfo2 = indentedInfo4.indent(-2);
        Assert.assertEquals("  OS: Linux\n  Arch: x86\n  CPUs: 8\n", indentedInfo2);
    }

    @Test
    public void swithStatements2()
    {
        // It is only an preview in Java 12, some features were introduced in later Java versions

        List<DayOfWeek> days = List.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY);
        List<String> expected = List.of("firstDay", "normalDay", "weekEnd");

        // * you does not have to call "break". Other cases are not executed.
        // * if you use a block, you have to use "yield" to return a value from the block
        // * switch must cover all possible input values. By "case" parts, or you can use "default"

        List<String> actual = days.stream()
            .map(day -> switch (day) {
                case MONDAY -> {
                    System.out.println("aaa");
                    String a = "firstDay";
                    yield a;
                }
                case SATURDAY, SUNDAY -> "weekEnd";
                default -> "normalDay";
            })
            .toList();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void automaticCastWhenInstanceof()
    {
        Object obj = "hi";
        if (obj instanceof String s) {
            // I do not need to cast manually here anymore !!!
            int length = s.length();
        }
    }

}
