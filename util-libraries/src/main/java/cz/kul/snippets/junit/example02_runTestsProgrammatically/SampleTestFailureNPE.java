package cz.kul.snippets.junit.example02_runTestsProgrammatically;

import org.junit.Assert;
import org.junit.Test;

public class SampleTestFailureNPE {
    
    @Test
    public void testWithNPE() {
        throw new NullPointerException("the message");
    }
    
}
