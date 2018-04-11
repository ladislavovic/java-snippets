package cz.kul.snippets;

import java.util.regex.Pattern;

public class Assert {

    public static void assertRegexMatches(String regex, CharSequence input) {
        boolean b = Pattern.matches(regex, input);
        if (!b) {
            throw new AssertionError(String.format("The input \"%s\" does matches to regex \"%s\".", input, regex));
        }
    }

    public static void assertRegexNotMatches(String regex, CharSequence input) {
        try {
            assertRegexMatches(regex, input);
            throw new AssertionError(String.format("The input \"%s\" matches to regex \"%s\". It should not.", input, regex));
        } catch (AssertionError err) {
            // It is OK, it should fails
        }
    }
}
