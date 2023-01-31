package cz.kul.snippets.junit;

import org.junit.Test;

public class Feature1IT {

    @Test
    public void testOne() {
        System.out.println("test1 start");
        Operation.createAndRun(10);
    }

}
