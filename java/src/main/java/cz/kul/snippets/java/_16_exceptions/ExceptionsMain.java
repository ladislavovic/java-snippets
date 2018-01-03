/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java._16_exceptions;

import org.junit.Assert;

/**
 *
 * @author kulhalad
 */
public class ExceptionsMain {
    
    public static void main(String[] args) {
        stacktrace();
    }

    private static void stacktrace() {
        // The satacktrace of the exception is the stacktrace, where
        // the exception was created. NOT THROWEN!
        Exception ex = createException();
        StackTraceElement[] stackTrace = ex.getStackTrace();
        Assert.assertEquals("createException", stackTrace[0].getMethodName());
        Assert.assertEquals(30, stackTrace[0].getLineNumber());
    }

    private static Exception createException() {
        return new Exception("My test exception");
    }
    
}
