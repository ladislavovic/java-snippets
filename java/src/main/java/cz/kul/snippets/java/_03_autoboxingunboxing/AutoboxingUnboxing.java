package cz.kul.snippets.java._03_autoboxingunboxing;

public class AutoboxingUnboxing {

    /**
     * Java compiler perform Autoboxing (primitive -> Object) and Unboxing (Object ->
     * primitive). It is quite simple, but there can be unexpected behaviour, when you
     * compare two numbers:
     */
    private static void autoboxingAndUnboxing() {
        Integer i = 12344321;
        Integer j = 12344321;
        System.out.println("Integer == Integer: " + (i == j)); // IT is not equal !!! There is no Unboxing here.
        System.out.println("int == Integer: " + (i.intValue() == j)); // Unbox to primitive
        System.out.println("Integer == int: " + (i == j.intValue())); // Unbox to prmitive
        System.out.println("int == int: " + (i.intValue() == j.intValue())); // No Autoboxing
    }

    public static void main(String[] args) {
        autoboxingAndUnboxing();
    }
}
