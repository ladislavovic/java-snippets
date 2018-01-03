package cz.kul.snippets.designpatterns._09_builder;

import static org.junit.Assert.assertEquals;

/**
 * This pattern separates objects and their construction.
 * 
 * Components in the pattern:
 * 
 * Builder - the class which can build a product. It can have one method (build()) or more
 * methods, each can construct part of the product. Builder is an interface and you can
 * have several implementations.
 * 
 * Product - result produced by Builder
 * 
 * Director - it manages whole building process. It has a reference to particular Builder.
 * It get the reference by client or it can get it itself. Builder is usually produced by
 * factory. Director just call methods of builder to create a pdocut. Concrete product
 * type depends on Builder implementation, Director does not know that.
 * 
 * NOTE1: Comparison with Factory: It is similar to Factory, but main purpose of factory
 * is polymorphism but main purpose of builder is to construct complex object.
 * Polymorphism is a bonus.
 * 
 * NOTE2: Real world example: In CA I implemented export of customer personal data to more
 * formates: xml, pdf, xls, txt. I implemented an Exporter class for each format with
 * common interface. It was a builder pattern.
 * 
 * reference: http://www.oodesign.com/builder-pattern.html
 */
public class Main_BuilderPattern {

    public static void main(String args[]) {
        Director director = new Director(new JSONBuilder());
        String output = director.convertStringIntoGivenFormat("hi");
        assertEquals("{val: \"hi\"}", output);
    }

}

class Director {

    private Builder builder;

    public Director(Builder builder) {
        this.builder = builder;
    }

    public String convertStringIntoGivenFormat(String str) {
        return builder.build(str);
    }
}

interface Builder {
    String build(String input);
}

class JSONBuilder implements Builder {

    public String build(String input) {
        return String.format("{val: \"%s\"}", input);
    }
    
}
