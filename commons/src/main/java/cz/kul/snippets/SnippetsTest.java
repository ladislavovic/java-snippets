package cz.kul.snippets;

import com.google.common.base.Throwables;
import cz.kul.snippets.agent.AgentManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class SnippetsTest {

    @Rule
    public SnippetsTestWatcher watcher = new SnippetsTestWatcher();

    private MemmoryAppenderHelper memmoryAppenderHelper;
    private FilesystemHelper filesystemHelper;

    @Before
    public void snippetsTestBefore() {
        memmoryAppenderHelper = MemmoryAppenderHelper.getInstance();
        filesystemHelper = FilesystemHelper.getInstance();
        AgentManager.clear();
    }

    protected MemmoryAppenderHelper getMemmoryAppenderHelper() {
        return memmoryAppenderHelper;
    }

    protected FilesystemHelper getFilesystemHelper() {
        return filesystemHelper;
    }

    protected MemmoryAppender getMemmoryAppender() {
        return getMemmoryAppenderHelper().getAppender();
    }

    protected final void assertMessageInLog(String message) {
        org.junit.Assert.assertTrue(getMemmoryAppender().findEventsCount(message) > 0);
    }

    protected final void assertMessageCountInLog(int times, String message) {
        Assert.assertEquals(times, getMemmoryAppender().findEventsCount(message));
    }

    protected void testOut(String pattern, Object... args) {
        System.out.printf(watcher.getTestId() + ": " + pattern + "\n", args);
    }

    /**
     * Throws an {@link AssertionError} if the given lambda does not throw an exception or if
     * the root cause of the exception is not the same type or subtype of the given class.
     *
     * @param expectedType lambda must throw an exception which root cause has this type. Can be also subtype.
     * @param r lambda to run
     */
    public static <T extends Throwable> T assertThrowsRootCause(Class<T> expectedType, Executable r) {
        return assertThrownExceptionCore(expectedType, true, r);
    }

    /**
     * Throws an {@link AssertionError} if the given lambda does not throw an exception or if
     * the exception is not the same type or subtype of the given class.
     *
     * @param expectedType lambda must throw an exception of this type. Can be also subtype.
     * @param r lambda to run
     */
    public static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable r) {
        return assertThrownExceptionCore(expectedType, false, r);
    }

    private static <T extends Throwable> T assertThrownExceptionCore(
            Class<T> clazz,
            boolean rootCause,
            Executable r) {
        try {
            r.run();
            throw new AssertionError("No exception was thrown.");
        } catch (Throwable e) {
            Throwable toCheck = rootCause ? Throwables.getRootCause(e) : e;
            if (!clazz.isAssignableFrom(toCheck.getClass())) {
                throw new AssertionError(String.format("An exception of type <%s> should be thrown but <%s> was thrown.",
                        clazz.getName(), toCheck.getClass().getName()));
            }
            return (T) toCheck;
        }
    }

    private static class SnippetsTestWatcher extends TestWatcher {

        private String testId;

        @Override
        protected void starting(Description description) {
            testId = description.getTestClass().getName() + "." + description.getMethodName();
        }

        public String getTestId() {
            return testId;
        }
    }

}
