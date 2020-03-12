package cz.kul.snippets.java.example23_suppressed_exception;

import org.junit.Assert;
import org.junit.Test;

import java.io.Closeable;

public class TestSuppressed {

    @Test
    public void suppressedExceptionWithResourceTry() {
        try (MyCloseable ioManip = new MyCloseable();
             MyCloseable ioManip2 = new MyCloseable()) {
            throw new RuntimeException("from try");
        } catch (RuntimeException e) {
            Assert.assertEquals("from try", e.getMessage());
            Assert.assertEquals(2, e.getSuppressed().length);
            Assert.assertEquals("from MyCloseable", e.getSuppressed()[0].getMessage());
            Assert.assertEquals("from MyCloseable", e.getSuppressed()[1].getMessage());
        }
    }
    
    @Test
    public void normalTryCatchDoesNotAddSuppressedExceptionAutomatically() {
        try {
            try {
                throw new RuntimeException("try");
            } finally {
                throw new RuntimeException("finally");
            }
        } catch (RuntimeException e) {
            Assert.assertEquals("finally", e.getMessage());
            Assert.assertEquals(0, e.getSuppressed().length);
        }
    }

    public static class MyCloseable implements Closeable {
        @Override
        public void close() {
            throw new RuntimeException("from MyCloseable");
        }
    }

}
