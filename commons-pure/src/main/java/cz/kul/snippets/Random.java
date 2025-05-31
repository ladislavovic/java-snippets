package cz.kul.snippets;

public class Random
{
    public static String createRandomString(int length)
    {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        String chars = "abcdefghijklmnopqrstuvwxz";
        var random = new java.util.Random();

        var result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(chars.length());
            result.append(chars.charAt(randomIndex));
        }

        return result.toString();
    }


}
