package cz.kul.snippets.java._02_enums;

public class Enums {

    public static enum DIRECTION {
        FORWARD, BACKWARD
    }
    
    public static void main(String[] args) {
        ordinal();    
    }
    
    /**
     * Ordinal returns the order of the enum. It ca not
     * be changed programaticaly. It is determined only 
     * by order of enum.
     */
    public static void ordinal() {
        System.out.println(DIRECTION.FORWARD.ordinal());
        System.out.println(DIRECTION.BACKWARD.ordinal());
    }

}
