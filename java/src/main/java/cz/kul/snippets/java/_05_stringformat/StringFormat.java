package cz.kul.snippets.java._05_stringformat;

public class StringFormat {

    public static void main(String[] args) {

        //
        // Basic examples
        //
        System.out.println(String.format("This print an integer: %d", 10));
        System.out.println(String.format("This print an short: %d", (short)11));
        System.out.println(String.format("This print a string: %s", "STR"));
        System.out.println(String.format("This print a decimal number: %f", Math.PI));
        
        // 
        // complex examples
        //
        // The full pattern specification:
        // %[argument_index$][flags][width][.precision]conversion

        // Pring number which takes at least 3 places and with leading zeros
        System.out.println(String.format("leading zeros: %01d", 10));

        String formatPattern = "%0" + 3 + "d";
        String idStr = String.format(formatPattern, 12345);
        String shardingKey = idStr.substring(idStr.length() - 3, idStr.length());
        System.out.println(shardingKey);
    }

}
