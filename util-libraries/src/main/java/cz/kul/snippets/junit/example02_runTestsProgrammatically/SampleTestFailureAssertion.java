package cz.kul.snippets.junit.example02_runTestsProgrammatically;

import org.junit.Assert;
import org.junit.Test;

public class SampleTestFailureAssertion {
    
    @Test
    public void testWithAssertionFault() {
        Assert.assertEquals(1, 2);
    }
    
}
