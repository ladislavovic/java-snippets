/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java.example14_exceptions;

import org.junit.Assert;
import org.junit.Test;

public class TestExceptions {

    @Test
    public void stacktrace() {
        // The satacktrace of the exception is the stacktrace, where
        // the exception was created. NOT THROWEN!
        Exception ex = createException();
        StackTraceElement[] stackTrace = ex.getStackTrace();
        Assert.assertEquals("createException", stackTrace[0].getMethodName());
        Assert.assertEquals(24, stackTrace[0].getLineNumber());
    }

    private Exception createException() {
        return new Exception("My test exception");
    }
    
}
