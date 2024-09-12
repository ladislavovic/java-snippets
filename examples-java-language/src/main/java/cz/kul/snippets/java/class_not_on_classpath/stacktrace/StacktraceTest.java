package cz.kul.snippets.java.class_not_on_classpath.stacktrace;

import org.junit.Assert;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StacktraceTest
{

    @Test
    public void stackTraceWithCause()
    {
        try {
            try {
                fnc1();
            } catch (Exception e) {
                throw new Exception("Exception in test", e);
            }
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);
            e.printStackTrace(writer);
            writer.close();

            Assert.assertTrue(
                stringWriter
                    .toString()
                    .matches(
                        "java.lang.Exception: Exception in test\\n" +
                          "\\s*at cz.kul.snippets.java.class_not_on_classpath.stacktrace.StacktraceTest.stackTraceWithCause\\(StacktraceTest.java:\\d\\d\\)\\n" +  // <-- trace of the "upper" exception contains the complete
                          "[\\s\\S]*" +                                                                                                                            //     trace, from the "throw" to the stack beginning
                        "Caused by: java.lang.RuntimeException: Exception in fnc2\\n" +
                          "\\s*at cz.kul.snippets.java.class_not_on_classpath.stacktrace.StacktraceTest.fnc2\\(StacktraceTest.java:\\d\\d\\)\\n" +                 // <-- stacktrace of caused method is trace from "throw:
                          "\\s*at cz.kul.snippets.java.class_not_on_classpath.stacktrace.StacktraceTest.fnc1\\(StacktraceTest.java:\\d\\d\\)\\n" +                 //     to "try", where it was caused
                          "\\s*at cz.kul.snippets.java.class_not_on_classpath.stacktrace.StacktraceTest.stackTraceWithCause\\(StacktraceTest.java:\\d\\d\\)\\n" +
                          "\\s*... \\d\\d more\\n"                                                                                                                 // <-- next stack lines are identical with the wrapping method
                    )                                                                                                                                              //     so they are not present
            );

        }
    }

    private void fnc1() {
        fnc2();
    }

    private void fnc2()
    {
        throw new RuntimeException("Exception in fnc2");
    }

    @Test
    public void stacktraceFromLambda() {

    }

}
