package cz.kul.snippets.java.example01_enums;

import org.junit.Assert;
import org.junit.Test;


public class TestEnums {

    public static enum DIRECTION {
        FORWARD, BACKWARD
    }

    /**
     * Ordinal returns the order of the enum. It ca not
     * be changed programaticaly. It is determined only
     * by order in source code.
     */
    @Test
    public void testOrdinal() {
        Assert.assertEquals(0, DIRECTION.FORWARD.ordinal());
        Assert.assertEquals(1, DIRECTION.BACKWARD.ordinal());

    }

}
