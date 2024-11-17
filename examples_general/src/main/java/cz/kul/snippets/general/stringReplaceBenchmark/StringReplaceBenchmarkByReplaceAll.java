package cz.kul.snippets.general.stringReplaceBenchmark;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StringReplaceBenchmarkByReplaceAll
{

    // \u0000, which does not have another \ before it
    private static final Pattern JSON_NULL_CHARACTER = Pattern.compile("(?<!\\\\)\\\\u0000");

    private static final String EMPTY_STRING = "";

    // CONCLUSION:
    // You should not cech if the string contains the char before the replacing, it is not faster. Or
    // in other words, String.contains() takes the same time as String.replace() when there is no occurrence of the character.
    //
    // result in zbook2024: 100 MB/s

    public static void main(String[] args)
    {
        List<String> strings = createRandomStrings(1000, 100_000);

        {
            long start = System.currentTimeMillis();
            for (String string : strings) {
                JSON_NULL_CHARACTER.matcher(string).replaceAll(EMPTY_STRING);
            }
            long end = System.currentTimeMillis();
            System.out.println("replacing: " + (end - start) + "ms");
        }

    }

    private static List<String> createRandomStrings(int noOfString, int noOfCharacters)
    {
        List<String> strings = new ArrayList<>(noOfString);
        for (int i = 0; i < noOfString; i++) {
            strings.add(RandomStringUtils.randomAlphanumeric(noOfCharacters));
        }
        return strings;
    }

}
