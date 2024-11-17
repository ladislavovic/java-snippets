package cz.kul.snippets.general.stringReplaceBenchmark;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

public class StringReplaceBenchmarkByReplace
{

    private static String NULL_CHAR = "\u0000";

    // CONCLUSION:
    // You should not cech if the string contains the char before the replacing, it is not faster. Or
    // in other words, String.contains() takes the same time as String.replace() when there is no occurrence of the character.
    //
    // result in Zbook2024: 3,5 GB/s

    public static void main(String[] args)
    {
        List<String> strings = createRandomStrings(1000, 100_000);

        {
            long start = System.currentTimeMillis();
            for (String string : strings) {
                if (string.contains(NULL_CHAR)) {
                    string.replace(NULL_CHAR, "");
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("replacing witch check before: " + (end - start) + "ms");
        }

        {
            long start = System.currentTimeMillis();
            for (String string : strings) {
                string.replace(NULL_CHAR, "");
            }
            long end = System.currentTimeMillis();
            System.out.println("pure replacing: " + (end - start) + "ms");
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
