package cz.kul.snippets.junit;


import org.junit.jupiter.api.Test;

public class Feature2IT {

    @Test
    public void testOne() {
        System.out.println("test2 start");
        Operation.createAndRun(10);
    }

}
