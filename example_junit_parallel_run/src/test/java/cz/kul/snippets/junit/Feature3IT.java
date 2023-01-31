package cz.kul.snippets.junit;

import org.junit.Test;

public class Feature3IT {

    @Test
    public void testThree() {
        System.out.println("test3 start");
        Operation.createAndRun(10);
    }

}
