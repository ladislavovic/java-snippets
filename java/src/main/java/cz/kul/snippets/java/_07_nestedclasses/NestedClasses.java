package cz.kul.snippets.java._07_nestedclasses;


/**
 * - nested classes are considered members of their enclosing class. So you can
 *   use any access modifier (private, protected, ...)
 * - nested classes have an access to the private members of the enclosing class
 *   and vice versa
 * 
 */
public class NestedClasses {
    
    private static String foo = "FOO";
    private String bar = "BAR";

    public static void main(String[] args) {
        NestedClasses enclosing = new NestedClasses();
        
        StaticNestedClass staticNestedClass = new StaticNestedClass();
        staticNestedClass.doIt(enclosing);
        staticNestedClass.baz = "baz";
        System.out.println("Enclosing class has access to nested class private members.");
     
        NestedClasses encl1 = new NestedClasses();
        NestedClasses encl2 = new NestedClasses();
        encl2.bar = "encl2Bar";
        InnerClass innerClass = encl1.new InnerClass();
        innerClass.doIt(encl2);
        innerClass.baz = "baz";
        System.out.println("Enclosing class has access to Inner Class private members.");

    }
    
    /**
     * Static nested class
     */
    private static class StaticNestedClass {
        private String baz;
        private void doIt(NestedClasses enclosing) {
            System.out.println("Static Nested Class");
            System.out.println("-------------------");
            System.out.println("Static nested classes have access to private enclosing class members:");
            System.out.println("Static: " + NestedClasses.foo + " Non static: " + enclosing.bar);
        }        
    }
    
    /**
     * Non-static nested class (Inner class)
     */
    private class InnerClass {
        private String baz;
        private void doIt(NestedClasses outer) {
            System.out.println("Inner class");
            System.out.println("------------");
            System.out.println("Inner class has access to its enclosing class private members:");
            System.out.println("Static: " + NestedClasses.foo + " Non static: " + bar);
            System.out.println("Inner class has also access to other encosing class instances private members:");
            System.out.println("Static: " + NestedClasses.foo + " Non static: " + outer.bar);
        }  
        
    }

}
