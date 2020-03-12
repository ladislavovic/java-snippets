package cz.kul.snippets.java.example21_regex;

import cz.kul.snippets.SnippetsTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex extends SnippetsTest {

    @Test
    public void replaceFirst() {
        String input = "aaa STARTvalEND";
        String regex = "START(.*)END";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        String output = matcher.replaceFirst("_$1_");
        Assert.assertEquals("aaa _val_", output);
    }
    
    @Test
    public void replaceAllByFunction() {
        String input = "_START_1_END_ _START_2_END_";
        BiFunction<Matcher, Integer, String> fnc = (matcher, inputNum) -> {
            int strNum = Integer.parseInt(matcher.group(1));
            return "_S_" + Integer.toString(strNum + inputNum) + "_E_";
        };
        String regex = "_START_(.*?)_END_";
        Pattern pattern = Pattern.compile(regex);
        String output = input;
        while (true) {
            Matcher matcher = pattern.matcher(output);
            if (!matcher.find()) {
                break;
            }
            String replacement = fnc.apply(matcher, 10);
            output = matcher.replaceFirst(replacement);
            MatchResult matchResult = matcher.toMatchResult();
        }
        Assert.assertEquals("_S_11_E_ _S_12_E_", output);
    }
    
    
    @Test
    public void testNonCapturing() {
//        assertNotMatches("((?!prefix).)*STR", "prefixSTR");
//        assertMatches("((?!prefix).)*STR", "aaaSTR");
    }



}
