/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java.example14_exceptions;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Null;

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

    @Test
    public void exceptionInCatchDiscardExceptionInTry() {
        try {
            try {
                throw new IllegalArgumentException();
            } catch (Exception e) {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NullPointerException);
            Assert.assertTrue(e.getSuppressed().length == 0);
        }
    }
    
    @Test
    public void exceptionInFinalDiscardExceptionInTry() {
        try {
            try {
                throw new IllegalArgumentException();
            } finally {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NullPointerException);
            Assert.assertTrue(e.getSuppressed().length == 0);
        }
    }
    
    @Test
    public void exceptionInFinalDiscardExceptionInCatch() {
        try {
            try {
                throw new IllegalArgumentException();
            } catch (Exception e) {
                throw new IllegalStateException();
            } finally {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NullPointerException);
            Assert.assertTrue(e.getSuppressed().length == 0);
        }
    }
}
